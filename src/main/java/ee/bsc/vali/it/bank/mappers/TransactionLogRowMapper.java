package ee.bsc.vali.it.bank.mappers;

import ee.bsc.vali.it.bank.objects.TransactionLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionLogRowMapper implements RowMapper<TransactionLog> {

    @Override
    public TransactionLog mapRow(ResultSet resultSet, int i) throws SQLException {

        TransactionLog log = new TransactionLog();
        log.setId(resultSet.getLong("id"));
        log.setTimeStamp(resultSet.getTimestamp("time_stamp"));
        log.setAmount(resultSet.getBigDecimal("amount"));
        log.setTransactionType(resultSet.getString("transaction_type"));
        log.setAccountFromId(resultSet.getLong("account_id_from"));
        log.setAccountToId(resultSet.getLong("account_id_to"));

        return log;
    }
}