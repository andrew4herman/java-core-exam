package machine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Simple enum class that represents the state of the coffee machine
 */
@RequiredArgsConstructor
@Getter
public enum CoffeeMachineState {

    TURNED_OFF("Press the power button to turn the machine on..."),
    CHOOSING_AN_ACTION("Write action (buy, fill, take, remaining, exit):"),
    CHOOSING_A_COFFEE("What do you want to buy? " +
            "1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: "),
    FILL("Write how many ml of water, milk, grams of coffee beans \n" +
            "and disposable cups you want to add(separated by space):");

    private final String message;
}
