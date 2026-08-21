package application.domain.model.valueobject;

import application.domain.exception.DomainException;
import lombok.Value;

@Value
public class StockLocation {
    String aisle;
    String rack;
    String shelf;

    public StockLocation(String aisle, String rack, String shelf) {
        if (aisle == null || aisle.isBlank()) throw new DomainException("Aisle is required");
        if (rack == null || rack.isBlank()) throw new DomainException("Rack is required");
        if (shelf == null || shelf.isBlank()) throw new DomainException("Shelf is required");
        this.aisle = aisle;
        this.rack = rack;
        this.shelf = shelf;
    }
}
