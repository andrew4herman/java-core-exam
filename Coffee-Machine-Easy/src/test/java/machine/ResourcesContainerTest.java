package machine;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourcesContainerTest {

    private ResourcesContainer container;

    @BeforeEach
    void setUp() {
        container = new ResourcesContainer(100, 100, 10, 10);
    }

    @Test
    void addResources() {
        //given
        List<Integer> resourcesToAdd = List.of(10, 10, 5, 5);
        List<Integer> expectedResources = List.of(110, 110, 15, 15);

        //when
        container.addResources(resourcesToAdd.get(0),
                resourcesToAdd.get(1), resourcesToAdd.get(2), resourcesToAdd.get(3));
        List<Integer> resultResources = List.of(
                container.getMlOfWater(), container.getMlOfMilk(), container.getGrOfCoffeeBeans(), container.getCups()
        );

        //then
        assertIterableEquals(expectedResources, resultResources);
    }

    @Test
    @SneakyThrows
    void useResources() {
        //given
        List<Integer> resourcesToUse = List.of(10, 10, 5, 5);
        List<Integer> expectedResources = List.of(90, 90, 5, 5);

        //when
        container.useResources(resourcesToUse.get(0),
                resourcesToUse.get(1), resourcesToUse.get(2), resourcesToUse.get(3));
        List<Integer> resultResources = List.of(
                container.getMlOfWater(), container.getMlOfMilk(), container.getGrOfCoffeeBeans(), container.getCups()
        );

        //then
        assertIterableEquals(expectedResources, resultResources);
    }

    @Test
    void tryToUseTooManyWater() {
        //given
        List<Integer> resourcesToUse = List.of(200, 0, 0, 0);

        //when
        NotEnoughResourcesException exception = assertThrows(NotEnoughResourcesException.class,
                () -> container.useResources(resourcesToUse.get(0),
                        resourcesToUse.get(1), resourcesToUse.get(2), resourcesToUse.get(3)));

        //then
        assertEquals("Sorry, not enough water!", exception.getMessage());
    }

    @Test
    void tryToUseTooManyMilk() {
        //given
        List<Integer> resourcesToUse = List.of(0, 200, 0, 0);

        //when
        NotEnoughResourcesException exception = assertThrows(NotEnoughResourcesException.class,
                () -> container.useResources(resourcesToUse.get(0),
                        resourcesToUse.get(1), resourcesToUse.get(2), resourcesToUse.get(3)));

        //then
        assertEquals("Sorry, not enough milk!", exception.getMessage());
    }

    @Test
    void tryToUseTooManyCoffeeBeans() {
        //given
        List<Integer> resourcesToUse = List.of(0, 0, 20, 0);

        //when
        NotEnoughResourcesException exception = assertThrows(NotEnoughResourcesException.class,
                () -> container.useResources(resourcesToUse.get(0),
                        resourcesToUse.get(1), resourcesToUse.get(2), resourcesToUse.get(3)));

        //then
        assertEquals("Sorry, not enough coffee beans!", exception.getMessage());
    }

    @Test
    void tryToUseTooManyCups() {
        //given
        List<Integer> resourcesToUse = List.of(0, 0, 0, 20);

        //when
        NotEnoughResourcesException exception = assertThrows(NotEnoughResourcesException.class,
                () -> container.useResources(resourcesToUse.get(0),
                        resourcesToUse.get(1), resourcesToUse.get(2), resourcesToUse.get(3)));

        //then
        assertEquals("Sorry, not enough cups!", exception.getMessage());
    }

}
