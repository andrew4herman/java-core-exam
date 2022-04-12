package carsharing.activities;

import carsharing.database.DBManager;
import carsharing.database.dao.DaoContainer;
import carsharing.model.Customer;
import carsharing.util.ChooserUtils;

import java.util.List;
import java.util.Scanner;

public class MainActivity extends Activity {

    private final DaoContainer daoContainer;
    private final DBManager dbManager;

    public MainActivity(Scanner scanner, DaoContainer daoContainer, DBManager dbManager) {
        super(scanner);
        this.daoContainer = daoContainer;
        this.dbManager = dbManager;
    }

    @Override
    protected void showMenu() {
        System.out.println("""
                                
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit
                """);
    }

    @Override
    protected void processOption(String option) {
        switch (option) {
            case "0" -> System.out.println("Bye!");
            case "1" -> logInAsManager();
            case "2" -> logInAsCustomer();
            case "3" -> createCustomerOption();
            default -> System.out.println("Incorrect option. Try again.");
        }
    }

    private void logInAsManager() {
        new ManagerActivity(scanner, daoContainer).start();
    }

    private void logInAsCustomer() {
        List<Customer> customers = daoContainer.getCustomerDao().getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            System.out.println("The customer list:");
            ChooserUtils.chooseEntityFrom(customers, scanner).ifPresent(
                    customer -> new CustomerActivity(scanner, daoContainer, dbManager,customer).start());
        }
    }

    private void createCustomerOption() {
        System.out.println("\nEnter the customer name:");
        String name = scanner.nextLine();

        daoContainer.getCustomerDao().save(name);
        System.out.println("The customer was added!");
    }
}
