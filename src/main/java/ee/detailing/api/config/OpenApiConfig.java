package ee.detailing.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
public class OpenApiConfig {

    static {
        SpringDocUtils.getConfig()
                .replaceWithSchema(LocalTime.class,
                        new Schema<String>().type("string").format("time").example("09:00:00"));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Detailing API")
                        .version("1.0.0")
                        .description("Vehicle Detailing Services Booking API"));
    }
}
