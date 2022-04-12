package machine;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ResourcesContainer resourcesContainer = new ResourcesContainer(400, 540, 120, 9);

        CoffeeMachine coffeeMachine = new CoffeeMachine(resourcesContainer);
        coffeeMachine.pressPowerButton();

        do {
            coffeeMachine.interact(scanner.nextLine());
        } while (coffeeMachine.getMachineState() != CoffeeMachineState.TURNED_OFF);

        scanner.close();
    }
}
