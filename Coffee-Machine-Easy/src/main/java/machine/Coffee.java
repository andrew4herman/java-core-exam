package machine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Simple enum class that represents the different types of coffee.
 */
@RequiredArgsConstructor
@Getter
public enum Coffee {

    ESPRESSO(250, 0, 16),
    LATTE(350, 75, 20),
    CAPPUCCINO(200, 100, 12),
    NONE(0, 0, 0);

    private final int mlOfWater;
    private final int mlOfMilk;
    private final int grOfCoffeeBeans;
}
