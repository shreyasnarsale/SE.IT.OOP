import java.util.*;

public class ATM {
    private double balance;
    private final List<String> history = new ArrayList<>();
    private final int pin = 1212;
    private static final Scanner SC = new Scanner(System.in);

    public ATM(double initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Initial balance cannot be negative.");
        this.balance = initialBalance;
        history.add("Account opened with initial balance: ₹" + String.format("%.2f", initialBalance));
    }

    public boolean verifyPin(int enteredPin) {
        return enteredPin == pin;
    }

    public double getBalance() {
        history.add("Checked balance: ₹" + String.format("%.2f", balance));
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
        balance += amount;
        history.add("Deposited ₹" + String.format("%.2f", amount) + " | Balance: ₹" + String.format("%.2f", balance));
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (amount > balance) throw new ArithmeticException("Insufficient funds. Available balance: ₹" + String.format("%.2f", balance));
        balance -= amount;
        history.add("Withdrew ₹" + String.format("%.2f", amount) + " | Balance: ₹" + String.format("%.2f", balance));
    }

    public void printHistory() {
        System.out.println("=== Transaction History ===");
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (String h : history) {
                System.out.println("- " + h);
            }
        }
        System.out.println("===========================\n");
    }

    // ---------------- Main Method ----------------
    public static void main(String[] args) {
        ATM atm = new ATM(5000.00);

        System.out.println("=== Welcome to the ATM Simulator ===");

        // PIN validation
        int attempts = 3;
        boolean verified = false;
        while (attempts > 0) {
            int enteredPin = readInt("Enter your 4-digit PIN: ");
            if (atm.verifyPin(enteredPin)) {
                System.out.println("PIN verified successfully. Access granted.\n");
                verified = true;
                break;
            } else {
                attempts--;
                System.out.println("Invalid PIN. Attempts left: " + attempts);
            }
        }
        if (!verified) {
            System.out.println("Too many failed attempts. Exiting.");
            return;
        }

        // Main menu loop
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option (1-5): ");

            switch (choice) {
                case 1:
                    try {
                        System.out.printf("Your current balance is: ₹%.2f%n", atm.getBalance());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    } finally {
                        System.out.println("[INFO] Balance check finished.\n");
                    }
                    break;

                case 2:
                    try {
                        double amt = readDouble("Enter amount to deposit: ");
                        atm.deposit(amt);
                        System.out.printf("Deposited ₹%.2f successfully. Balance: ₹%.2f%n", amt, atm.getBalance());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    } finally {
                        System.out.println("[INFO] Deposit operation finished.\n");
                    }
                    break;

                case 3:
                    try {
                        double amt = readDouble("Enter amount to withdraw: ");
                        atm.withdraw(amt);
                        System.out.printf("Withdrew ₹%.2f successfully. Balance: ₹%.2f%n", amt, atm.getBalance());
                    } catch (ArithmeticException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    } finally {
                        System.out.println("[INFO] Withdrawal operation finished.\n");
                    }
                    break;

                case 4:
                    atm.printHistory();
                    break;

                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    running = false;
                    break;

                default:
                    try {
                        throw new IllegalArgumentException("Menu choice must be between 1 and 5.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    } finally {
                        System.out.println("[INFO] Menu validation complete.\n");
                    }
                    break;
            }
        }
    }

    // ---------------- Utility Methods ----------------
    private static void printMenu() {
        System.out.println("------------------------------------");
        System.out.println("1) Check Balance");
        System.out.println("2) Deposit");
        System.out.println("3) Withdraw");
        System.out.println("4) View Transaction History");
        System.out.println("5) Exit");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SC.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException nfe) {
                try {
                    throw new IllegalArgumentException("Please enter a valid integer.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                } finally {
                    System.out.println("[INFO] Integer input attempt handled.");
                }
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SC.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException nfe) {
                try {
                    throw new IllegalArgumentException("Please enter a valid number.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input: " + e.getMessage());
                } finally {
                    System.out.println("[INFO] Numeric input attempt handled.");
                }
            }
        }
    }
}
