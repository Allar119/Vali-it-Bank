package ee.bsc.vali.it.bank.objects;

import java.math.BigDecimal;

public class DepositWithdraw {
    private String accountNr;
    private BigDecimal amount;

    public String getAccountNr() {
        return accountNr;
    }

    public void setAccountNr(String accountNr) {
        this.accountNr = accountNr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
