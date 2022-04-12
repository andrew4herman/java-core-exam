package banking;

import banking.model.Account;
import banking.model.Card;

import java.util.Optional;
import java.util.Scanner;

/**
 * The class represents a banking system
 */
public class BankingSystem {

    private final Bank bank;
    private final Scanner scanner;
    private Account currentAccount;
    private boolean loggedIn;

    public BankingSystem(Bank bank, Scanner scanner) {
        this.bank = bank;
        this.scanner = scanner;
    }

    /**
     * Display the menu, get the user's choice, process the choice, and sync the account
     */
    public void start() {
        String option;

        do {
            showMenu();
            option = scanner.next();

            processInput(option);
            syncAccount();
        } while (!"0".equals(option));
    }

    private void showMenu() {
        if (!loggedIn) {
            System.out.printf("""
                    %n1. Create an account
                    2. Log into account
                    """);
        } else {
            System.out.printf("""
                    %n1. Balance
                    2. Add income
                    3. Do transfer
                    4. Close account
                    5. Log out
                    """);
        }
        System.out.println("0. Exit\n");
    }

    private void processInput(String input) {
        if (loggedIn) {
            processLoggedOption(input);
        } else {
            processBasicOption(input);
        }
    }

    private void syncAccount() {
        if (currentAccount != null) {
            Optional<Account> account = bank.signIn(currentAccount.card().cardNumber(), currentAccount.card().PIN());

            account.ifPresentOrElse(
                    this::setCurrentAccount,
                    () -> {
                        logOut();
                        System.out.println("\nError, probably card PIN has been changed. Try to sign in again.");
                    }
            );
        }
    }

    private void processLoggedOption(String option) {
        switch (option) {
            case "1" -> showBalanceOption();
            case "2" -> addIncomeOption();
            case "3" -> doTransferOption();
            case "4" -> closeAccountOption();
            case "5" -> logOutOption();
            case "0" -> System.out.println("\nBye!\n");
            default -> System.out.println("\nIncorrect option. Try again.");
        }
    }

    private void processBasicOption(String option) {
        switch (option) {
            case "1" -> createAccountOption();
            case "2" -> logInOption();
            case "0" -> System.out.println("\nBye!\n");
            default -> System.out.println("Incorrect option. Try again.");
        }
    }

    private void showBalanceOption() {
        System.out.println("Balance: " + currentAccount.balance());
    }

    private void addIncomeOption() {
        System.out.println("\nEnter income:");

        try {
            int income = scanner.nextInt();
            if (income < 1) {
                throw new IllegalArgumentException();
            }

            bank.addIncome(currentAccount.card().cardNumber(), income);
            System.out.println("Income was added!");
        } catch (IllegalArgumentException ex) {
            System.out.println("Incorrect input. Enter a number greater than zero!");
        }
    }

    private void doTransferOption() {
        System.out.println("\nTransfer");
        System.out.println("Enter card number:");
        String receiver = scanner.next();

        if (canTransferTo(receiver)) {
            System.out.println("Enter how much money you want to transfer:");
            int moneyToTransfer = scanner.nextInt();

            if (canSend(moneyToTransfer)) {
                bank.doTransfer(currentAccount.card().cardNumber(),
                        receiver,
                        moneyToTransfer);

                System.out.println("Success!");
            }
        }
    }

    private void closeAccountOption() {
        bank.removeAccount(currentAccount.id());
        logOut();

        System.out.println("\nThe account has been closed!");
    }

    private void logOutOption() {
        logOut();
        System.out.println("\nYou have successfully logged out!");
    }

    private void createAccountOption() {
        Card card = bank.registerAccount();
        System.out.printf("""
                %nYour account has been created
                Your card number:
                %s
                Your card PIN:
                %s%n""", card.cardNumber(), card.PIN());
    }

    private void logInOption() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.next();

        System.out.println("Enter your PIN:");
        String PIN = scanner.next();

        Optional<Account> account = bank.signIn(cardNumber, PIN);
        account.ifPresentOrElse(
                acc -> {
                    logIn(acc);
                    System.out.println("You have successfully logged in!");
                },
                () -> System.out.println("Wrong card number or PIN!")
        );
    }

    private boolean canTransferTo(String receiver) {
        try {
            if (receiver.equals(currentAccount.card().cardNumber())) {
                throw new IllegalArgumentException("You can't transfer money to the same account!");
            }
            bank.checkCardNumber(receiver);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean canSend(int moneyToTransfer) {
        try {
            if (moneyToTransfer < 1) {
                throw new IllegalArgumentException("Invalid input. Please try again.");
            } else if (moneyToTransfer > currentAccount.balance()) {
                throw new IllegalArgumentException("Not enough money!");
            } else {
                return true;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void logOut() {
        setCurrentAccount(null);
        loggedIn = false;
    }

    private void logIn(Account account) {
        setCurrentAccount(account);
        loggedIn = true;
    }

    private void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }
}
