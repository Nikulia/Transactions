package bank;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {
    private Bank bank;
    private Account firstAccount;
    private Account secondAccount;
    private static final int THREAD_COUNT = 4;
    private static final int MONEY_TO_TRANSFER = 6_000;
    private static final long INITIAL_MONEY_ON_FIRST_ACCOUNT = 300_000;
    private static final long INITIAL_MONEY_ON_SECOND_ACCOUNT = 10_000;
    private CopyOnWriteArrayList<Long> testBalances = new CopyOnWriteArrayList<>();

    public BankTest() {
        initialiseAccounts();
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++)
            threads[i] = new Thread(() -> doTransferAndTestBalance());
        for (int i = 0; i < THREAD_COUNT; i++)
            threads[i].start();
    }

    protected void initialiseAccounts() {
        firstAccount = new Account(INITIAL_MONEY_ON_FIRST_ACCOUNT, "1");
        secondAccount = new Account(INITIAL_MONEY_ON_SECOND_ACCOUNT, "2");
        bank = new Bank(firstAccount, secondAccount);
    }


    protected void doTransferAndTestBalance() {
        bank.transfer(firstAccount.getAccNumber(), secondAccount.getAccNumber(), MONEY_TO_TRANSFER);
        testBalances.add(bank.getBalance(firstAccount.getAccNumber()));
        bank.transfer(secondAccount.getAccNumber(), firstAccount.getAccNumber(), MONEY_TO_TRANSFER);
        testBalances.add(bank.getBalance(secondAccount.getAccNumber()));
        bank.transfer(firstAccount.getAccNumber(), secondAccount.getAccNumber(), MONEY_TO_TRANSFER);
        testBalances.add(bank.getBalance(firstAccount.getAccNumber()));
    }

    @Test
    public void testTransfer() {
        assertAll("money on accounts",
                () -> assertEquals(INITIAL_MONEY_ON_FIRST_ACCOUNT -
                        MONEY_TO_TRANSFER * THREAD_COUNT, firstAccount.getMoney()),
                () -> assertEquals(INITIAL_MONEY_ON_SECOND_ACCOUNT +
                        MONEY_TO_TRANSFER * THREAD_COUNT, secondAccount.getMoney())
        );

    }

    @Test
    public void testGetBalance() {
        for (int i = 0; i < testBalances.size(); i++)
            assertNotNull(testBalances.get(i));
    }
}
