# Frontend Requirements

This document tracks features that require frontend implementation to work properly.

---

## Booking Cancellation Page

**Status:** Pending frontend implementation

**Current behavior:**
- Email notifications contain `{cancellationLink}` placeholder
- Link currently points to API: `GET /api/v1/bookings/{reference}` (returns JSON)
- Link is not clickable (plain text)

**Required frontend implementation:**
1. Create a cancellation page at `/cancel/{reference}` or `/cancel?ref={reference}`
2. Page should:
   - Display booking details
   - Show "Confirm Cancellation" button
   - On confirm, call `DELETE /api/v1/bookings/{reference}`
   - Show success/error message

**Backend changes needed when frontend is ready:**
1. Update `EmailService.java` - change `BASE_URL` to frontend URL
2. Update cancellation link to point to frontend page: `{frontendUrl}/cancel/{reference}`
3. Wrap link in `<a>` tag for clickability

**API endpoint for cancellation:**
```
DELETE /api/v1/bookings/{reference}
```
Returns: `204 No Content` on success

---

## Rebooking Page

**Status:** Pending frontend implementation

**Current behavior:**
- Email notifications contain `{rebookingLink}` placeholder
- Currently points to API base URL

**Required frontend implementation:**
- Link should point to frontend booking page
