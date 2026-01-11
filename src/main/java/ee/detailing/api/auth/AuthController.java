package ee.detailing.api.auth;

import ee.detailing.api.common.exceptions.ErrorResponse;
import ee.detailing.api.user.User;
import ee.detailing.api.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        log.info("=== Login attempt for: {} ===", request.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();

            log.info("=== Login successful for: {} ===", request.getEmail());
            return ResponseEntity.ok(new LoginResponse(user.getEmail(), user.getRole()));
        } catch (Exception e) {
            log.error("=== Login failed: {} ===", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(user.getEmail(), user.getRole()));
    }

    @PutMapping("/password")
    public ResponseEntity<ErrorResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // Verify current password
        log.info("=== Attempting password change for: {} ===", email);
        log.debug("=== Current password length: {}, New password length: {} ===",
                request.getCurrentPassword() != null ? request.getCurrentPassword().length() : "null",
                request.getNewPassword() != null ? request.getNewPassword().length() : "null");

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            log.warn("=== Password change failed for {}: incorrect current password ===", email);
            return ResponseEntity.badRequest().body(new ErrorResponse("password_mismatch", "Current password is incorrect"));
        }

        // Validate new password
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            return ResponseEntity.badRequest().body(new ErrorResponse("validation_error", "New password must be at least 6 characters"));
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("=== Password changed for: {} ===", email);
        return ResponseEntity.ok().build();
    }
}
