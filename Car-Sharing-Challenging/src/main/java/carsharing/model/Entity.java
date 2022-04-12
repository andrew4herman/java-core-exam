package carsharing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Entity {

    private final int id;
    private final String name;
}
