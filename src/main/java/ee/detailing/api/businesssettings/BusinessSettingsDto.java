package ee.detailing.api.businesssettings;

import lombok.Data;

@Data
public class BusinessSettingsDto {
    private String name;
    private String phone;
    private String email;
    private String address;
}
