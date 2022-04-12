package machine;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A class that represents the resources available to make a cup of coffee
 */
@AllArgsConstructor
@Getter
public class ResourcesContainer {

    private int mlOfWater;
    private int mlOfMilk;
    private int grOfCoffeeBeans;
    private int cups;

    /**
     * Add the given resources to the current resources
     *
     * @param mlOfWater       The amount of water in milliliters.
     * @param mlOfMilk        The amount of ml of milk to add to the coffee machine.
     * @param grOfCoffeeBeans The amount of coffee beans the coffee machine has.
     * @param cups            The number of disposable cups of coffee the coffee machine has.
     */
    public void addResources(int mlOfWater, int mlOfMilk, int grOfCoffeeBeans, int cups) {
        this.mlOfWater += mlOfWater;
        this.mlOfMilk += mlOfMilk;
        this.grOfCoffeeBeans += grOfCoffeeBeans;
        this.cups += cups;
    }

    /**
     * Takes resources from the container
     *
     * @param mlOfWater       The amount of water to use.
     * @param mlOfMilk        The amount of milk in milliliters (mL) to use.
     * @param grOfCoffeeBeans The number of grams of coffee beans needed to make the cup of coffee.
     * @param cups            The number of cups of coffee the user wants to take.
     * @throws NotEnoughResourcesException If there aren't enough resources
     */
    public void useResources(int mlOfWater, int mlOfMilk, int grOfCoffeeBeans, int cups)
            throws NotEnoughResourcesException {
        String error = "";

        if (mlOfWater > this.getMlOfWater()) {
            error = "water";
        } else if (mlOfMilk > this.getMlOfMilk()) {
            error = "milk";
        } else if (grOfCoffeeBeans > this.getGrOfCoffeeBeans()) {
            error = "coffee beans";
        } else if (cups > this.cups) {
            error = "cups";
        }

        if (!error.isEmpty()) {
            throw new NotEnoughResourcesException(String.format("Sorry, not enough %s!", error));
        } else {
            addResources(-mlOfWater, -mlOfMilk, -grOfCoffeeBeans, -cups);
        }
    }
}
