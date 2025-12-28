package ee.detailing.api.booking;

import ee.detailing.api.addon.AddOn;
import ee.detailing.api.addon.AddOnRepository;
import ee.detailing.api.deliverytype.DeliveryType;
import ee.detailing.api.deliverytype.DeliveryTypeRepository;
import ee.detailing.api.notification.BookingEmailEvent;
import ee.detailing.api.notification.NotificationType;
import ee.detailing.api.pkg.Package;
import ee.detailing.api.pkg.PackageRepository;
import ee.detailing.api.timeslot.TimeSlot;
import ee.detailing.api.timeslot.TimeSlotRepository;
import ee.detailing.api.timeslot.TimeSlotStatus;
import ee.detailing.api.vehicletype.VehicleType;
import ee.detailing.api.vehicletype.VehicleTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final PackageRepository packageRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final AddOnRepository addOnRepository;
    private final BookingMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    // === Public Methods ===

    @Transactional
    public BookingDto createBooking(BookingCreateRequest request) {
        // Load all related entities
        VehicleType vehicleType = vehicleTypeRepository.findById(request.getVehicleTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vehicle type not found: " + request.getVehicleTypeId()));

        Package pkg = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Package not found: " + request.getPackageId()));

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Time slot not found: " + request.getTimeSlotId()));

        DeliveryType deliveryType = deliveryTypeRepository.findById(request.getDeliveryTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Delivery type not found: " + request.getDeliveryTypeId()));

        // Load add-ons if provided
        Set<AddOn> addOns = new HashSet<>();
        if (request.getAddOnIds() != null && !request.getAddOnIds().isEmpty()) {
            addOns = new HashSet<>(addOnRepository.findAllById(request.getAddOnIds()));
            if (addOns.size() != request.getAddOnIds().size()) {
                throw new IllegalArgumentException("Some add-ons not found");
            }
        }

        // Validate business rules
        validateTimeSlotAvailable(timeSlot);
        validatePackageForVehicleType(pkg.getId(), vehicleType.getId());
        validateAddOnsForPackage(request.getAddOnIds(), pkg.getId());
        validateDeliveryAddress(deliveryType, request.getAddress());

        // Calculate price
        BigDecimal totalPrice = calculateTotalPrice(vehicleType, pkg, deliveryType, addOns);

        // Generate reference
        String reference = generateReference();

        // Create booking entity
        Booking booking = mapper.toEntity(request);
        booking.setReference(reference);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);
        booking.setVehicleType(vehicleType);
        booking.setPkg(pkg);
        booking.setTimeSlot(timeSlot);
        booking.setDeliveryType(deliveryType);
        booking.setAddOns(addOns);

        // Mark time slot as booked
        timeSlot.setStatus(TimeSlotStatus.BOOKED);
        timeSlotRepository.save(timeSlot);

        // Save and return
        Booking saved = bookingRepository.save(booking);

        // Publish event - email will be sent after transaction commits
        eventPublisher.publishEvent(new BookingEmailEvent(this, saved.getId(), NotificationType.BOOKING_CONFIRMATION));

        return mapper.toDto(bookingRepository.findByIdWithDetails(saved.getId()).orElseThrow());
    }

    @Transactional(readOnly = true)
    public BookingDto getBookingByReference(String reference) {
        return bookingRepository.findByReferenceWithDetails(reference)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + reference));
    }

    @Transactional
    public void cancelBookingByReference(String reference) {
        Booking booking = bookingRepository.findByReference(reference)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + reference));

        if (booking.getStatus().isTerminal()) {
            throw new IllegalArgumentException("Booking cannot be cancelled - already " + booking.getStatus());
        }

        // Update status
        booking.setStatus(BookingStatus.CANCELLED_BY_CUSTOMER);

        // Release time slot
        releaseTimeSlot(booking);

        Booking saved = bookingRepository.save(booking);

        // Publish event - email will be sent after transaction commits
        eventPublisher.publishEvent(new BookingEmailEvent(this, saved.getId(), NotificationType.BOOKING_CANCELLATION));
    }

    // === Admin Methods ===

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAllWithDetails();
        // Load add-ons separately to avoid MultipleBagFetchException
        for (Booking booking : bookings) {
            org.hibernate.Hibernate.initialize(booking.getAddOns());
        }
        return mapper.toDtoList(bookings);
    }

    @Transactional(readOnly = true)
    public BookingDto getBookingById(Integer id) {
        return bookingRepository.findByIdWithDetails(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
    }

    @Transactional
    public BookingDto updateBooking(Integer id, BookingUpdateRequest request) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));

        // Check if time slot is changing
        boolean timeSlotChanging = request.getTimeSlotId() != null &&
                (booking.getTimeSlot() == null || !request.getTimeSlotId().equals(booking.getTimeSlot().getId()));

        // Load new related entities if IDs are provided
        VehicleType vehicleType = request.getVehicleTypeId() != null ?
                vehicleTypeRepository.findById(request.getVehicleTypeId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Vehicle type not found: " + request.getVehicleTypeId()))
                : booking.getVehicleType();

        Package pkg = request.getPackageId() != null ?
                packageRepository.findById(request.getPackageId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Package not found: " + request.getPackageId()))
                : booking.getPkg();

        TimeSlot newTimeSlot = null;
        if (timeSlotChanging) {
            newTimeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Time slot not found: " + request.getTimeSlotId()));
            validateTimeSlotAvailable(newTimeSlot);
        }

        DeliveryType deliveryType = request.getDeliveryTypeId() != null ?
                deliveryTypeRepository.findById(request.getDeliveryTypeId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Delivery type not found: " + request.getDeliveryTypeId()))
                : booking.getDeliveryType();

        // Load add-ons
        Set<AddOn> addOns = booking.getAddOns();
        if (request.getAddOnIds() != null) {
            if (request.getAddOnIds().isEmpty()) {
                addOns = new HashSet<>();
            } else {
                addOns = new HashSet<>(addOnRepository.findAllById(request.getAddOnIds()));
                if (addOns.size() != request.getAddOnIds().size()) {
                    throw new IllegalArgumentException("Some add-ons not found");
                }
            }
        }

        // Validate business rules
        if (request.getPackageId() != null || request.getVehicleTypeId() != null) {
            validatePackageForVehicleType(pkg.getId(), vehicleType.getId());
        }
        if (request.getAddOnIds() != null && !request.getAddOnIds().isEmpty()) {
            validateAddOnsForPackage(request.getAddOnIds(), pkg.getId());
        }
        String addressToValidate = request.getAddress() != null ? request.getAddress() : booking.getAddress();
        validateDeliveryAddress(deliveryType, addressToValidate);

        // Update entity from request
        mapper.updateEntityFromRequest(request, booking);

        // Update relationships
        booking.setVehicleType(vehicleType);
        booking.setPkg(pkg);
        booking.setDeliveryType(deliveryType);
        booking.setAddOns(addOns);

        // Handle time slot change
        if (timeSlotChanging) {
            // Release old time slot
            releaseTimeSlot(booking);
            // Book new time slot
            newTimeSlot.setStatus(TimeSlotStatus.BOOKED);
            timeSlotRepository.save(newTimeSlot);
            booking.setTimeSlot(newTimeSlot);
        }

        // Recalculate price
        BigDecimal totalPrice = calculateTotalPrice(vehicleType, pkg, deliveryType, addOns);
        booking.setTotalPrice(totalPrice);

        Booking saved = bookingRepository.save(booking);

        // Publish event - email will be sent after transaction commits
        eventPublisher.publishEvent(new BookingEmailEvent(this, saved.getId(), NotificationType.BOOKING_MODIFICATION));

        return mapper.toDto(bookingRepository.findByIdWithDetails(saved.getId()).orElseThrow());
    }

    @Transactional
    public BookingDto updateBookingStatus(Integer id, BookingStatusUpdateRequest request) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));

        BookingStatus currentStatus = booking.getStatus();
        BookingStatus newStatus = request.getStatus();

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException(
                    "Cannot transition from " + currentStatus + " to " + newStatus);
        }

        booking.setStatus(newStatus);

        // Release time slot if cancelling
        if (newStatus == BookingStatus.CANCELLED_BY_ADMIN) {
            releaseTimeSlot(booking);
        }

        return mapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public void deleteBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));

        // Note: No email notification on hard delete (booking won't exist after transaction)
        // Use cancel status change instead if customer notification is needed

        // Release time slot before deleting
        releaseTimeSlot(booking);

        bookingRepository.delete(booking);
    }

    // === Private Helper Methods ===

    private BigDecimal calculateTotalPrice(VehicleType vehicleType, Package pkg,
                                           DeliveryType deliveryType, Set<AddOn> addOns) {
        BigDecimal total = BigDecimal.ZERO;

        total = total.add(vehicleType.getBasePrice());
        total = total.add(pkg.getPrice());
        total = total.add(deliveryType.getPrice());

        for (AddOn addOn : addOns) {
            total = total.add(addOn.getPrice());
        }

        return total;
    }

    private static final int REFERENCE_RANDOM_PART_LENGTH = 4;

    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String reference;
        do {
            String randomPart = RANDOM.ints(REFERENCE_RANDOM_PART_LENGTH, 0, ALPHANUMERIC.length())
                    .mapToObj(ALPHANUMERIC::charAt)
                    .map(Object::toString)
                    .collect(Collectors.joining());
            reference = "BK-" + datePart + "-" + randomPart;
        } while (bookingRepository.existsByReference(reference));

        return reference;
    }

    private void validateTimeSlotAvailable(TimeSlot timeSlot) {
        if (timeSlot.getStatus() != TimeSlotStatus.AVAILABLE) {
            throw new IllegalArgumentException("Time slot is not available: " + timeSlot.getId());
        }
    }

    private void validatePackageForVehicleType(Integer packageId, Integer vehicleTypeId) {
        if (!packageRepository.existsByIdAndVehicleTypesId(packageId, vehicleTypeId)) {
            throw new IllegalArgumentException(
                    "Package " + packageId + " is not available for vehicle type " + vehicleTypeId);
        }
    }

    private void validateAddOnsForPackage(List<Integer> addOnIds, Integer packageId) {
        if (addOnIds == null || addOnIds.isEmpty()) {
            return;
        }
        List<AddOn> validAddOns = addOnRepository.findAllByIdInAndPackagesId(addOnIds, packageId);
        if (validAddOns.size() != addOnIds.size()) {
            throw new IllegalArgumentException("Some add-ons are not available for this package");
        }
    }

    private void validateDeliveryAddress(DeliveryType deliveryType, String address) {
        if (deliveryType.getRequiresAddress() && (address == null || address.isBlank())) {
            throw new IllegalArgumentException("Address is required for delivery type: " + deliveryType.getName());
        }
    }

    private void releaseTimeSlot(Booking booking) {
        if (booking.getTimeSlot() != null) {
            TimeSlot timeSlot = booking.getTimeSlot();
            timeSlot.setStatus(TimeSlotStatus.AVAILABLE);
            timeSlotRepository.save(timeSlot);
        }
    }
}
