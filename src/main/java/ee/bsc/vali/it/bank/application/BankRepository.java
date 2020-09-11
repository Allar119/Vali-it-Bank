package ee.bsc.vali.it.bank.application;


import ee.bsc.vali.it.bank.mappers.AccountRowMapper;
import ee.bsc.vali.it.bank.mappers.TransactionLogRowMapper;
import ee.bsc.vali.it.bank.objects.Account;
import ee.bsc.vali.it.bank.objects.Client;
import ee.bsc.vali.it.bank.objects.TransactionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BankRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    //Add client
    public long addClient(Client client, String password) {
        //String sql2 = "SELECT nextval(pg_get_serial_sequence('client','id'))";
        String sql = "INSERT INTO client (first_name, last_name, user_name, password) VALUES (:firstName, :lastName, :userName, :password)";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstName", client.getFirstName());
        paramMap.put("lastName", client.getLastName());
        paramMap.put("userName", client.getUserName());
        paramMap.put("password", password);

        //add new client to database and return its ID key
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new MapSqlParameterSource(paramMap);
        jdbcTemplate.update(sql, parameterSource, generatedKeyHolder);
        return (long) generatedKeyHolder.getKeys().get("id");
    }

    //Get last account number form database
    public String getLastAccountNumber(){
        String sql = "SELECT account_nr FROM account WHERE ID = (SELECT MAX(ID) FROM account)";

        try {
            return jdbcTemplate.queryForObject(sql, new HashMap<>(),String.class);
        }
        catch(Exception e) {
            return null;
        }
    }

    //Add account to client
    public long addAccount(long clientID, String accountNr) {

        String getNextId = "SELECT nextval(pg_get_serial_sequence('account','id'))";
        long nextID= jdbcTemplate.queryForObject(getNextId, new HashMap<>(), long.class);

        String sql = "INSERT INTO account (id, account_nr, balance, client_id) VALUES (:id, :accountNr, :balance, :clientID)";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", nextID);
        paramMap.put("accountNr", accountNr);
        paramMap.put("balance", 0);
        paramMap.put("clientID", clientID);

        jdbcTemplate.update(sql, paramMap);
        return nextID;
    }

    //Update balance in SQL Database
    public void updateBalance(String accountNr, BigDecimal amount){
        String sql = "UPDATE account SET balance = balance + :amount WHERE account_nr = :accountNr";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accountNr", accountNr);
        paramMap.put("amount", amount);

        jdbcTemplate.update(sql, paramMap);
    }

    //Get account balance form SQL Database
    public String getAccountBalance(String accountNr){
        String sql = "SELECT balance FROM account where account_nr = :accountNr";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("accountNr", accountNr);

        return jdbcTemplate.queryForObject(sql, paramMap, String.class);
    }

    //Get account id
    public BigInteger getAccountId(String accountNr){

        String sql = "SELECT id FROM account where account_nr = :accountNr";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("accountNr", accountNr);

        return jdbcTemplate.queryForObject(sql, paramMap, BigInteger.class);
    }

    //Get account number
    public String getAccountNr(long id) {
        String sql = "SELECT account_nr FROM account where id = :id";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        return jdbcTemplate.queryForObject(sql, paramMap, String.class);
    }

    //Log transactions
    public void logTransaction(BigDecimal amount, String accountFrom, String accountTo){
        String sql = "INSERT INTO transaction_log (time_stamp, transaction_type, amount, account_id_from, account_id_to) " +
                "VALUES (:time, :transactionType, :amount, :accountIdFrom, :accountIdTo)";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("time", new Date());
        paramMap.put("amount", amount);

        if (accountFrom == null && accountTo != null){
            paramMap.put("transactionType", "Deposit");
            paramMap.put("accountIdFrom", null);
            paramMap.put("accountIdTo", getAccountId(accountTo));
        }
        else if(accountFrom != null && accountTo == null){
            paramMap.put("transactionType", "Withdraw");
            paramMap.put("accountIdFrom", getAccountId(accountFrom));
            paramMap.put("accountIdTo", null);
        }
        else{
            paramMap.put("transactionType", "Transfer");
            paramMap.put("accountIdFrom", getAccountId(accountFrom));
            paramMap.put("accountIdTo", getAccountId(accountTo));
        }

        jdbcTemplate.update(sql, paramMap);
    }

    //Delete account from SQL Database //DELETE FROM accounts WHERE id = 16;
    public void deleteAccount(String accountNr){
        String sql = "DELETE FROM accounts WHERE account_nr = :accountNr";

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("accountNr", accountNr);

        jdbcTemplate.update(sql, paramMap);
    }

    //Get all account from SQL Database
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM account ORDER BY id";
        return jdbcTemplate.query(sql, new HashMap<>(), new AccountRowMapper());
    }

    //Get transaction log
    public List<TransactionLog> getTransactionHistory() {
        String sql = "SELECT * FROM transaction_log ORDER BY id";
        return jdbcTemplate.query(sql, new HashMap<>(), new TransactionLogRowMapper());
    }

    //Get user password hash from database
    public String loginRequest(String userName){
        String sql = "SELECT password FROM client where user_name = :userName";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userName", userName);
        return jdbcTemplate.queryForObject(sql, paramMap, String.class);
    }

    //

}
