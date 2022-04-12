package carsharing.model;

import lombok.Getter;
import lombok.Setter;

public class Customer extends Entity{
    @Getter
    @Setter
    private int rentedCarId;

    public Customer(int id, String name, int rentedCarId) {
        super(id, name);
        this.rentedCarId = rentedCarId;
    }
}
