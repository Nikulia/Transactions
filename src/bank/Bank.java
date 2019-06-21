package bank;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Danya on 18.02.2016.
 */
public class Bank {
    private HashMap<String, Account> accounts;
    private final Random random = new Random();

    public Bank(Account... accounts) {
        HashMap<String, Account> accountNumberAccount = new HashMap<>();
        for (Account account : accounts)
            accountNumberAccount.put(account.getAccNumber(), account);
        this.accounts = accountNumberAccount;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    public void transfer(String fromAccountNum, String toAccountNum, long amount) {
        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);
        if (fromAccount.equals(toAccount))
            throw new IllegalStateException("Money don't transfer to itself");
        else if (fromAccount.getMoney() - amount < 0)
            throw new IllegalStateException("Money on the account not enough for transfer");
        if (amount > 50000) {
            try {
                if (isFraud(fromAccountNum, toAccountNum, amount)) {
                    if (fromAccountNum.compareTo(toAccountNum) > 0) {
                        synchronized (fromAccount) {
                            synchronized (toAccount) {
                                fromAccount.setBlocked(true);
                                toAccount.setBlocked(true);
                            }
                        }
                    } else {
                        synchronized (toAccount) {
                            synchronized (fromAccount) {
                                fromAccount.setBlocked(true);
                                toAccount.setBlocked(true);
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (fromAccount.isBlocked())
            throw new IllegalStateException("Account, from that you want to transfer money, is blocked");
        else if (toAccount.isBlocked())
            throw new IllegalStateException("Account, where you want to transfer money, is blocked");
        if (fromAccountNum.compareTo(toAccountNum) > 0) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    doTransfer(fromAccount, toAccount, amount);
                }
            }
        } else

        {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    doTransfer(fromAccount, toAccount, amount);
                }
            }
        }

    }

    private void doTransfer(Account fromAccount, Account toAccount, long amount) {
        fromAccount.setMoney(fromAccount.getMoney() - amount);
        toAccount.setMoney(toAccount.getMoney() + amount);
    }


    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashMap<String, Account> accounts) {
        this.accounts = accounts;
    }

    public long getBalance(String accountNum) {
        synchronized (accounts.get(accountNum)) {
            return accounts.get(accountNum).getMoney();
        }
    }
}
