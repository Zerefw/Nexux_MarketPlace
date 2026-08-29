package application.domain.model.valueobject;

import application.domain.exception.DomainException;
import lombok.Value;

@Value
public class Sku {
    String code;

    public Sku(String code) {
        if (code == null || code.isBlank()) {
            throw new DomainException("SKU cannot be blank");
        }
        this.code = code;
    }
}
