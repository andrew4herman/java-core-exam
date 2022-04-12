package machine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class CoffeeMachineTest {

    private ResourcesContainer container;
    private CoffeeMachine coffeeMachine;

    @BeforeEach
    void setUp() {
        container = new ResourcesContainer(1000, 1000, 50, 5);
        coffeeMachine = new CoffeeMachine(container);
    }

    @Test
    void testTurnOnCoffeeMachine() {
        //given

        //when
        coffeeMachine.pressPowerButton();

        //then
        assertEquals(CoffeeMachineState.CHOOSING_AN_ACTION, coffeeMachine.getMachineState());
    }

    @Test
    void testTurnOffCoffeeMachine() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_A_COFFEE);

        //when
        coffeeMachine.pressPowerButton();

        //then
        assertEquals(CoffeeMachineState.TURNED_OFF, coffeeMachine.getMachineState());

    }

    @Test
    void testBuyOption() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_AN_ACTION);

        //when
        coffeeMachine.interact("buy");

        //then
        assertEquals(CoffeeMachineState.CHOOSING_A_COFFEE, coffeeMachine.getMachineState());
    }

    @Test
    void testFillOption() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_AN_ACTION);

        //when
        coffeeMachine.interact("fill");

        //then
        assertEquals(CoffeeMachineState.FILL, coffeeMachine.getMachineState());
    }

    @Test
    void testShowRemainingOption() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_AN_ACTION);

        //when
        coffeeMachine.interact("remaining");

        //then
        assertEquals(CoffeeMachineState.CHOOSING_AN_ACTION, coffeeMachine.getMachineState());
    }

    @Test
    void testTakeOption() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_AN_ACTION);

        //when
        coffeeMachine.interact("take");

        //then
        assertEquals(0, coffeeMachine.getMoney());
    }

    @Test
    void testExitOption() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_AN_ACTION);

        //when
        coffeeMachine.interact("exit");

        //then
        assertEquals(CoffeeMachineState.TURNED_OFF, coffeeMachine.getMachineState());
    }

    @Test
    void testChoosingCorrectCoffee() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_A_COFFEE);

        //when
        coffeeMachine.interact("1");

        //then
        assertEquals(CoffeeMachineState.CHOOSING_AN_ACTION, coffeeMachine.getMachineState());
    }

    @Test
    void testChoosingIncorrectCoffee() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.CHOOSING_A_COFFEE);

        //when
        coffeeMachine.interact("10");

        //then
        assertEquals(CoffeeMachineState.CHOOSING_A_COFFEE, coffeeMachine.getMachineState());
    }

    @Test
    void testFillingCoffeeMachine() {
        //given
        coffeeMachine.setMachineState(CoffeeMachineState.FILL);
        List<Integer> expectedResources = List.of(2000, 2000, 100, 10);

        //when
        coffeeMachine.interact("1000 1000 50 5");
        List<Integer> resultResources = List.of(
                container.getMlOfWater(), container.getMlOfMilk(),
                container.getGrOfCoffeeBeans(), container.getCups());

        //then
        assertEquals(CoffeeMachineState.CHOOSING_AN_ACTION, coffeeMachine.getMachineState());
        assertIterableEquals(expectedResources, resultResources);
    }
}
