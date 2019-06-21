package bank;

import java.util.Random;

public class Loader {
    protected static final int ACCOUNT_COUNT = 10;
    protected static final int MAX_MONEY = 200_000;
    protected static final int THREAD_COUNT = 3;

    public static void main(String[] args) {
        Random random = new Random(System.currentTimeMillis());
        Account[] accounts = new Account[ACCOUNT_COUNT];
        for (int i = 0; i < ACCOUNT_COUNT; i++)
            accounts[i] = new Account(random.nextInt(MAX_MONEY), String.valueOf(i + 1));
        Bank bank = new Bank(accounts);
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++)
                threads[i] = new Thread(getTask(bank, accounts, random));
        for (int i = 0; i < THREAD_COUNT; i++)
            threads[i].start();


    }

    private static Runnable getTask(Bank bank, Account[] accounts, Random random) {
        return new Runnable() {
            @Override
            public void run() {
                Account fromAccount = accounts[random.nextInt(ACCOUNT_COUNT)];
                Account toAccount = accounts[random.nextInt(ACCOUNT_COUNT)];
                int moneyToTransfer = random.nextInt(MAX_MONEY);
                try {
                    bank.transfer(fromAccount.getAccNumber(), toAccount.getAccNumber(), moneyToTransfer);
                    System.out.println(getTransferDecryption(fromAccount, toAccount, moneyToTransfer) + "succesfully!");
                } catch (IllegalStateException e) {
                    System.err.println(getTransferDecryption(fromAccount, toAccount, moneyToTransfer)+ e.getMessage());
                }
            }

            private String getTransferDecryption(Account fromAccount, Account toAccount, int moneyToTransfer) {
                try {
                    Thread.sleep(random.nextInt(500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return new StringBuilder("====================================\n")
                        .append("Money transfer from account ")
                        .append(fromAccount.getAccNumber())
                        .append(" with amount of money on account ")
                        .append(fromAccount.getMoney())
                        .append("\nto account ")
                        .append(toAccount.getAccNumber())
                        .append(" with amount of money on account ")
                        .append(toAccount.getMoney())
                        .append(".\nAmount of money to transfer: ")
                        .append(moneyToTransfer)
                        .append("\n")
                        .toString();

            }

        };
    }
}
