package carsharing.activities;

import carsharing.database.DBManager;
import carsharing.database.dao.DaoContainer;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;
import carsharing.util.ChooserUtils;

import java.util.List;
import java.util.Scanner;

public class CustomerActivity extends Activity {

    private final Customer currentCustomer;
    private final DaoContainer daoContainer;
    private final DBManager dbManager;

    public CustomerActivity(Scanner scanner, DaoContainer daoContainer, DBManager dbManager, Customer customer) {
        super(scanner);
        this.daoContainer = daoContainer;
        this.dbManager = dbManager;
        this.currentCustomer = customer;
    }

    @Override
    protected void showMenu() {
        System.out.println("""
                                
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back
                """);
    }

    @Override
    protected void processOption(String option) {
        switch (option) {
            case "0" -> System.out.printf("Logged out from '%s'%n", currentCustomer.getName());
            case "1" -> rentCarOption();
            case "2" -> returnRentedCarOption();
            case "3" -> showRentedCarOption();
            default -> System.out.println("Incorrect option. Try again!");
        }
    }

    private void rentCarOption() {
        if (hasRentedCar()) {
            System.out.println("You've already rented a car!");
        } else {
            List<Company> companies = daoContainer.getCompanyDao().getAll();
            if (companies.isEmpty()) {
                System.out.println("\nThe company list is empty!");
            } else {
                System.out.println("Choose a company:");
                ChooserUtils.chooseEntityFrom(companies, scanner).ifPresent(this::chooseCar);
            }
        }
    }

    private void returnRentedCarOption() {
        if (hasRentedCar()) {
            daoContainer.getCarDao().getById(currentCustomer.getRentedCarId()).ifPresent(this::returnCar);
        } else {
            System.out.println("You didn't rent a car!");
        }
    }

    private void showRentedCarOption() {
        if (hasRentedCar()) {
            daoContainer.getCarDao().getById(currentCustomer.getRentedCarId()).ifPresent(this::outputInfoAbout);
        } else {
            System.out.println("You didn't rent a car!");
        }
    }

    private void chooseCar(Company company) {
        List<Car> cars = daoContainer.getCarDao().getRentedCarsFrom(company.getId());
        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!");
        } else {
            System.out.println("Choose a car:");
            ChooserUtils.chooseEntityFrom(cars, scanner).ifPresent(this::rentCar);
        }
    }

    private void returnCar(Car car) {
        dbManager.makeTransaction(() -> {
            daoContainer.getCustomerDao().updateCustomer(currentCustomer.getId(), 0);
            daoContainer.getCarDao().updateCar(car.getId(), false);
        });
        currentCustomer.setRentedCarId(0);

        System.out.println("You've returned a rented car!");
    }

    private void outputInfoAbout(Car car) {
        String companyName = daoContainer.getCompanyDao().getById(car.getCompanyId()).orElseThrow().getName();
        System.out.printf("""
                                
                Your rented car:
                %s
                Company:
                %s
                """, car.getName(), companyName);
    }

    private boolean hasRentedCar() {
        return currentCustomer.getRentedCarId() != 0;
    }

    private void rentCar(Car car) {
        dbManager.makeTransaction(() -> {
            daoContainer.getCustomerDao().updateCustomer(currentCustomer.getId(), car.getId());
            daoContainer.getCarDao().updateCar(car.getId(), true);
        });
        currentCustomer.setRentedCarId(car.getId());

        System.out.printf("%nYou rented '%s'%n", car.getName());
    }
}
