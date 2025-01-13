import java.util.*;

abstract class Account {
    protected String accountNumber;
    protected String owner;
    protected double balance;
    protected List<String> transactions;

    public Account(String accountNumber, String owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposited: " + amount);
            System.out.println("Deposit successful. New balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public abstract boolean withdraw(double amount);

    public boolean transfer(double amount, Account recipient) {
        if (amount > 0 && this.withdraw(amount)) {
            recipient.deposit(amount);
            transactions.add("Transferred: " + amount + " to " + recipient.accountNumber);
            recipient.transactions.add("Received: " + amount + " from " + this.accountNumber);
            return true;
        }
        return false;
    }

    public void getStatement() {
        System.out.println("\nAccount Statement for " + accountNumber + ":");
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
    }
}

class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 500;
    private static final double DAILY_WITHDRAWAL_LIMIT = 2000;
    private double dailyWithdrawn = 0;

    public SavingsAccount(String accountNumber, String owner, double balance) {
        super(accountNumber, owner, balance);
        if (balance < MIN_BALANCE) {
            throw new IllegalArgumentException("Error: Minimum balance for savings account is 500.");
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= MIN_BALANCE) {
            if ((dailyWithdrawn + amount) <= DAILY_WITHDRAWAL_LIMIT) {
                balance -= amount;
                dailyWithdrawn += amount;
                transactions.add("Withdrawn: " + amount);
                System.out.println("Withdrawal successful. New balance: " + balance);
                return true;
            } else {
                System.out.println("Withdrawal failed: Exceeds daily limit.");
            }
        } else {
            System.out.println("Withdrawal failed: Insufficient funds or below minimum balance.");
        }
        return false;
    }
}

class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 1000;

    public CurrentAccount(String accountNumber, String owner, double balance) {
        super(accountNumber, owner, balance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= -OVERDRAFT_LIMIT) {
            balance -= amount;
            transactions.add("Withdrawn: " + amount);
            System.out.println("Withdrawal successful. New balance: " + balance);
            return true;
        }
        System.out.println("Withdrawal failed: Exceeds overdraft limit.");
        return false;
    }
}

class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        this.accounts = new HashMap<>();
    }

    public void createAccount(String type, String accountNumber, String owner, double balance) {
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Error: Account number already exists.");
            return;
        }

        Account account;
        if (type.equalsIgnoreCase("savings")) {
            try {
                account = new SavingsAccount(accountNumber, owner, balance);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return;
            }
        } else if (type.equalsIgnoreCase("current")) {
            account = new CurrentAccount(accountNumber, owner, balance);
        } else {
            System.out.println("Invalid account type.");
            return;
        }

        accounts.put(accountNumber, account);
        System.out.println(type.toUpperCase() + " account created for " + owner + ". Account Number: " + accountNumber);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
}

public class BankingSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // Creating accounts
        bank.createAccount("savings", "1275933424", "koustubh", 1000);  // Success
        bank.createAccount("savings", "1275983263", "Pritam", 400);     // Error: Minimum balance
        bank.createAccount("current", "1127598532", "Yuraj", 500); // Success

        Account aliceAcc = bank.getAccount("1275933424");
        Account charlieAcc = bank.getAccount("1127598532");

        // Performing operations
        if (aliceAcc != null) {
            aliceAcc.deposit(500);
            aliceAcc.withdraw(2000); // Within daily limit
            aliceAcc.withdraw(1000); // Exceeds daily limit
            aliceAcc.getStatement();
        }

        if (charlieAcc != null) {
            charlieAcc.withdraw(1300); // Overdraft allowed
            charlieAcc.getStatement();
        }
    }
}
