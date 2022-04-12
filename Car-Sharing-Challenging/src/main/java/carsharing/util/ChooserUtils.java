package carsharing.util;

import carsharing.model.Entity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

@UtilityClass
public class ChooserUtils {

    public <T extends Entity> Optional<T> chooseEntityFrom(List<T> list, Scanner scanner) {
        do {
            ChooserUtils.printEntities(list);
            System.out.println("0. Back");
            try {
                int option = Integer.parseInt(scanner.nextLine());
                return ChooserUtils.parseOption(list, option);
            } catch (IllegalArgumentException e) {
                System.out.println("Incorrect input. Try again or return back.\n");
            }
        } while (true);
    }

    public <T extends Entity> void printEntities(List<T> list) {
        IntStream.iterate(0, i -> i + 1)
                .limit(list.size())
                .forEach(i -> System.out.printf("%d. %s%n", i + 1, list.get(i).getName()));
    }

    private <T extends Entity> Optional<T> parseOption(List<T> list, int option) {
        if (option > 0 && option <= list.size()) {
            return Optional.of(list.get(option - 1));
        } else if (option == 0) {
            return Optional.empty();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
