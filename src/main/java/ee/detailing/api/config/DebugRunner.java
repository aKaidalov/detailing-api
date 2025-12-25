package ee.detailing.api.config;

import ee.detailing.api.user.User;
import ee.detailing.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DebugRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("=== Checking admin password ===");

        userRepository.findByEmail("admin@detailing.ee").ifPresent(user -> {
            if (!passwordEncoder.matches("admin123", user.getPasswordHash())) {
                log.warn("Admin password hash is incorrect, fixing...");
                user.setPasswordHash(passwordEncoder.encode("admin123"));
                userRepository.save(user);
                log.info("Admin password fixed!");
            } else {
                log.info("Admin password OK");
            }
        });
    }
}
