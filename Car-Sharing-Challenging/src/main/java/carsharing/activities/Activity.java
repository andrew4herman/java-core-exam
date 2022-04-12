package carsharing.activities;

import java.util.Scanner;

public abstract class Activity {

    public final Scanner scanner;

    Activity(Scanner scanner) {
        this.scanner = scanner;
    }

    public final void start() {
        String option;
        do {
            showMenu();
            option = scanner.nextLine();
            processOption(option);
        } while (!"0".equals(option));
    }

    protected abstract void showMenu();

    protected abstract void processOption(String option);
}
