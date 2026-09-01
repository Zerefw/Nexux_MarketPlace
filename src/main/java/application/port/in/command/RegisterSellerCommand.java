package application.port.in.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterSellerCommand {
    String identificationDocument;
    String fullName;
    String email;
    String rawPassword;
    String storeName;
    String taxId;
}
