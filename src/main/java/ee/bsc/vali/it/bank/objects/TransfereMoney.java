package ee.bsc.vali.it.bank.objects;

import java.math.BigDecimal;

public class TransfereMoney {

    private String accountFrom;
    private String accountTo;
    private BigDecimal amount;

    public String getAccountFrom() {
        return accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
