package carsharing.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Car extends Entity {

    private final int companyId;
    @Setter
    private boolean rented;

    public Car(int id, String name, int companyId, boolean rented) {
        super(id, name);
        this.companyId = companyId;
        this.rented = rented;
    }
}
