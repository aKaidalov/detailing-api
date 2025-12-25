package ee.detailing.api.auth;

import ee.detailing.api.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private UserRole role;
}
