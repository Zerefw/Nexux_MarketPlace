package application.domain.model.valueobject;

import application.domain.exception.DomainException;
import lombok.Value;

@Value
public class Email {
    String address;

    public Email(String address) {
        if (address == null || !address.contains("@")) {
            throw new DomainException("Invalid email format");
        }
        this.address = address;
    }
}
