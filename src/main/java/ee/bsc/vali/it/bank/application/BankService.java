package ee.bsc.vali.it.bank.application;

import ee.bsc.vali.it.bank.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Add new customer:
    public long addClient(Client client) {
        //String passwordHash = passwordEncoder.encode(client.getPassword());
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        long clientId =  bankRepository.addClient(client);
        addAccount(clientId);
        return clientId;
    }

    //Add new account
    public String addAccount(long clientId){
        String accountNr = generateAccountNr();
        long id = bankRepository.addAccount(clientId, accountNr);
        return bankRepository.getAccountNr(id);
    }

    //Generate next account number
    public String generateAccountNr (){

        String previousAccountNr = bankRepository.getLastAccountNumber();

        StringBuilder accountNr = new StringBuilder();
        accountNr.append("EE0099");

        if (previousAccountNr != null){
            long num = Long.parseLong(previousAccountNr.substring(6));
            num++;
            accountNr.append(num);
        }
        else{
            accountNr.append(10000000000000L);
        }
        return accountNr.toString();
    }

    //Get Account Balance
    public String getAccountBalance(String accountNr) {
        return bankRepository.getAccountBalance(accountNr);
    }

    //Get account ID form account number
    public BigInteger getAccountId(String accountNr) {
        return bankRepository.getAccountId(accountNr);
    }

    //Get account number form id
    public String getAccountNr(long id){
        return bankRepository.getAccountNr(id);
    }

    //Deposit money to account
    public String depositMoney (DepositWithdraw deposit){
        String accountNrTo = deposit.getAccountNr();
        BigDecimal amount = deposit.getAmount();

        bankRepository.updateBalance(accountNrTo, amount);
        bankRepository.logTransaction(amount, null, accountNrTo);

        return bankRepository.getAccountBalance(accountNrTo);
    }

    //Withdraw money from account
    public String withdrawMoney(DepositWithdraw withdraw){
        String accountNr = withdraw.getAccountNr();
        BigDecimal amount = withdraw.getAmount();

        if (hasMoney(accountNr, amount.abs())){
            bankRepository.updateBalance(accountNr, amount.multiply(new BigDecimal(-1)));
            bankRepository.logTransaction(amount, accountNr, null);
            return bankRepository.getAccountBalance(accountNr);
        }
        else{
            return "Not enough funds!";
        }
    }

    //Transfer money between two accounts
    public String transferMoney(TransfereMoney transfer){

        String accountFrom = transfer.getAccountFrom();
        String accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();

        if (hasMoney(accountFrom, amount)){
            bankRepository.updateBalance(accountFrom, amount.multiply(new BigDecimal(-1)));
            bankRepository.updateBalance(accountTo, amount);
            bankRepository.logTransaction(amount, accountFrom, accountTo);
            return amount.toString();
        }
        else{
            return "Not enough funds for transfer!";
        }
    }

    //Check if account hase enough funds
    public boolean hasMoney(String accountNr, BigDecimal amount){
        BigDecimal currentBalanc = new BigDecimal(bankRepository.getAccountBalance(accountNr));
        return (currentBalanc.compareTo(amount) >= 0); //Shot version of if statement
    }

    //Delete account
    public String deleteAccount(String accountNr){
        bankRepository.deleteAccount(accountNr);
        return String.format("Account %s deleted form database", accountNr);
    }

    //Get all accounts
    public List<Account> getAllAccounts(){
        return bankRepository.getAllAccounts();
    }

    //Make list into string:
    public String accountListToText(List<Account> accounts){
        StringBuilder output = new StringBuilder();

        output.append("-------- ACCOUNTS DATABASE --------");
        for (Account account : accounts) {
            output.append("\n***********************************");
            output.append("\nAccount ID: ").append(account.getId());
            output.append("\nAccount Number: ").append(account.getAccountNr());
            output.append("\nAccount Balance: ").append(account.getAccountBalance()).append("€");
            output.append("\nCustomer ID: ").append(account.getClientId());
        }
        return output.toString();
    }

    //Get Transaction Log
    public List<TransactionLog> getTransactionLog() {
        return bankRepository.getTransactionHistory();
    }

    public String transactionLogToText (List<TransactionLog> log){
        StringBuilder output = new StringBuilder();

        output.append("-------- Transaction Log --------");
        for (TransactionLog row : log) {
            output.append("\n***********************************");
            output.append("\nID: ").append(row.getId());
            output.append("\nDate: ").append(row.getTimeStamp());
            output.append("\nType: ").append(row.getTransactionType());
            output.append("\nAmount: ").append(row.getAmount()).append("€");

            if(row.getAccountFromId()!=0){
                output.append("\nFrom: ").append(bankRepository.getAccountNr(row.getAccountFromId()));
            }

            if(row.getAccountToId()!=0)
            output.append("\nTo: ").append(bankRepository.getAccountNr(row.getAccountToId()));
            }

        return output.toString();
    }

}
