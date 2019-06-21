package bank;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Danya on 18.02.2016.
 */
class Account {
    private long money;
    private String accNumber;
    private boolean blocked;

    Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
    }

    long getMoney() {
        return money;
    }

    void setMoney(long money) {
        this.money = money;
    }

    String getAccNumber() {
        return accNumber;
    }

    void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
