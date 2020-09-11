package ee.bsc.vali.it.bank.mappers;

import ee.bsc.vali.it.bank.objects.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet resultSet, int i) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getLong("id"));
        account.setAccountNr(resultSet.getString("account_nr"));
        account.setAccountBalance(resultSet.getBigDecimal("balance"));
        account.setClientId(resultSet.getLong("client_id"));

        return account;
    }
}
