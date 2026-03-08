import java.util.*;
import java.text.SimpleDateFormat;

public class BankingSystem {
    private static final Scanner SC = new Scanner(System.in);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    // Store multiple accounts
    private static final Map<String, BankingSystem> accounts = new HashMap<>();

    private String accountHolder;
    private String accountNumber;
    private String mobileNo;
    private String address;
    private double balance;
    private final double DAILY_LIMIT = 10000.0;
    private double todayWithdrawn = 0.0;
    private final List<Transaction> transactions = new ArrayList<>();

    // Transaction inner class
    static class Transaction {
        Date date;
        String type;
        double amount;
        double balanceAfter;

        Transaction(String type, double amount, double balanceAfter) {
            this.date = new Date();
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
        }

        @Override
        public String toString() {
            return sdf.format(date) + " | " + type + " | Rs." + amount + " | Balance: Rs." + balanceAfter;
        }
    }

    // Constructor
    private BankingSystem(String holderName, String accNum, String mobile, String addr, double initialDeposit) {
        this.accountHolder = holderName;
        this.accountNumber = accNum;
        this.mobileNo = mobile;
        this.address = addr;
        this.balance = initialDeposit;
        transactions.add(new Transaction("Account Created", initialDeposit, balance));
    }

    // Deposit
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
        balance += amount;
        transactions.add(new Transaction("Deposit", amount, balance));
        System.out.println("Rs." + amount + " deposited successfully.");
    }

    // Withdraw
    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (amount > balance) throw new ArithmeticException("Insufficient balance.");
        if (todayWithdrawn + amount > DAILY_LIMIT) throw new ArithmeticException("Daily withdrawal limit exceeded.");
        balance -= amount;
        todayWithdrawn += amount;
        transactions.add(new Transaction("Withdrawal", amount, balance));
        System.out.println("Rs." + amount + " withdrawn successfully.");
    }

    // Balance
    public double getBalance() {
        transactions.add(new Transaction("Balance Check", 0, balance));
        return balance;
    }

    // Account info
    public void displayAccountInfo() {
        System.out.println("\n--- Account Information ---");
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Mobile Number: " + mobileNo);
        System.out.println("Address: " + address);
        System.out.println("Current Balance: Rs." + balance);
        System.out.println("Daily Withdrawal Limit: Rs." + DAILY_LIMIT);
        System.out.println("---------------------------\n");
    }

    // Passbook
    public void passbookPrint(Date from, Date to) {
        System.out.println("\n--- Passbook from " + sdf.format(from) + " to " + sdf.format(to) + " ---");
        for (Transaction t : transactions) {
            if (!t.date.before(from) && !t.date.after(to)) {
                System.out.println(t);
            }
        }
        System.out.println("---------------------------\n");
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose option (1-8): ");

            switch (choice) {
                case 1: // Create account
                    createAccountMenu();
                    break;

                case 2: // Deposit
                    BankingSystem depAcc = getAccount();
                    if (depAcc != null) {
                        double dep = readDouble("Enter amount to deposit: ");
                        try {
                            depAcc.deposit(dep);
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;

                case 3: // Withdraw
                    BankingSystem wdAcc = getAccount();
                    if (wdAcc != null) {
                        double wd = readDouble("Enter amount to withdraw: ");
                        try {
                            wdAcc.withdraw(wd);
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;

                case 4: // Daily withdrawal info
                    BankingSystem dwAcc = getAccount();
                    if (dwAcc != null) {
                        System.out.println("Daily withdrawal limit: Rs." + dwAcc.DAILY_LIMIT);
                        System.out.println("Withdrawn today: Rs." + dwAcc.todayWithdrawn);
                    }
                    break;

                case 5: // Check balance
                    BankingSystem balAcc = getAccount();
                    if (balAcc != null) {
                        System.out.println("Current Balance: Rs." + balAcc.getBalance());
                    }
                    break;

                case 6: // Display account info
                    BankingSystem infoAcc = getAccount();
                    if (infoAcc != null) {
                        infoAcc.displayAccountInfo();
                    }
                    break;

                case 7: // Passbook
                      BankingSystem pbAcc = getAccount();
                      if (pbAcc != null) {
                        if (pbAcc.transactions.isEmpty()) {
                             System.out.println("No transactions available.");
                   break;
                    }
                     Date first = pbAcc.transactions.get(0).date;
                     Date last = pbAcc.transactions.get(pbAcc.transactions.size()-1).date;
                     System.out.println("Passbook available from: " + sdf.format(first) + " to " + sdf.format(last));

                        try {
                            System.out.print("Enter From Date (dd-MM-yyyy): ");
                            String fromStr = SC.nextLine();
                            System.out.print("Enter To Date (dd-MM-yyyy): ");
                            String toStr = SC.nextLine();
                
                            Date from = new SimpleDateFormat("dd-MM-yyyy").parse(fromStr);
                            Date to = new SimpleDateFormat("dd-MM-yyyy").parse(toStr);
                            pbAcc.passbookPrint(from, to);
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;


                case 8:
                    running = false;
                    System.out.println("Exiting Banking System. Thank you.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // -----------------------------------------------------
    private static void printMenu() {
        System.out.println("\n===== Banking System Menu =====");
        System.out.println("1) Create Account");
        System.out.println("2) Deposit Money");
        System.out.println("3) Withdraw Money");
        System.out.println("4) Daily Withdrawal Info");
        System.out.println("5) Check Balance");
        System.out.println("6) Display Account Information");
        System.out.println("7) Passbook Print (From - To)");
        System.out.println("8) Exit");
        System.out.println("================================");
    }

    private static void createAccountMenu() {
        try {
            System.out.print("Enter Account Holder Name: ");
            String name = SC.nextLine();
            if (!name.matches("[a-zA-Z ]+")) throw new IllegalArgumentException("Name must contain only letters and spaces.");

            System.out.print("Enter Account Number: ");
            String accNo = SC.nextLine();
            if (!accNo.matches("\\d+")) throw new IllegalArgumentException("Account number must be digits only.");
            if (accounts.containsKey(accNo)) throw new IllegalArgumentException("Account number already exists.");

            System.out.print("Enter Mobile Number: ");
            String mobile = SC.nextLine();
            if (!mobile.matches("\\d{10}")) throw new IllegalArgumentException("Mobile number must be 10 digits.");

            System.out.print("Enter Address: ");
            String address = SC.nextLine();
            if (address.isEmpty()) throw new IllegalArgumentException("Address cannot be empty.");

            double init = readDouble("Enter Initial Deposit: ");
            if (init < 0) throw new IllegalArgumentException("Initial deposit cannot be negative.");

            BankingSystem newAcc = new BankingSystem(name, accNo, mobile, address, init);
            accounts.put(accNo, newAcc);

            System.out.println("Account created successfully.");
            newAcc.displayAccountInfo();

            System.out.println("Currently, system has " + accounts.size() + " account(s).");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static BankingSystem getAccount() {
        System.out.print("Enter Account Number: ");
        String accNo = SC.nextLine();
        BankingSystem acc = accounts.get(accNo);
        if (acc == null) {
            System.out.println("No account found with this number.");
        }
        return acc;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(SC.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(SC.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
