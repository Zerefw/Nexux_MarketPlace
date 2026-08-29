package application.domain.model.valueobject;

import application.domain.exception.DomainException;
import lombok.Value;
import java.math.BigDecimal;

@Value
public class Money {
    BigDecimal amount;
    String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount == null) throw new DomainException("Amount cannot be null");
        if (currency == null || currency.isBlank()) throw new DomainException("Currency cannot be blank");
        this.amount = amount;
        this.currency = currency;
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) throw new DomainException("Currency mismatch");
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }
}
