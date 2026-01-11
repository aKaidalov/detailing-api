# Frontend Requirements

This document tracks features that require frontend implementation to work properly.

---

## Booking Cancellation Page

**Status:** ✅ Completed (Phase 9)

**Implementation:**
- Route: `/cancel/:reference`
- Page fetches booking by reference and displays details
- "Confirm Cancellation" button calls `DELETE /api/v1/bookings/{reference}`
- Handles states: loading, success, already cancelled, not found

**Files:**
- `src/pages/CancelBooking.tsx`
- `src/api/hooks/useCancelBooking.ts`
- `src/api/hooks/useBookingByReference.ts`

---

## Rebooking Page

**Status:** Not implemented (low priority)

**Current behavior:**
- Email notifications contain `{rebookingLink}` placeholder
- Currently points to frontend booking page

**Required frontend implementation:**
- Could pre-fill booking form with previous booking details (future enhancement)
