package ee.bsc.vali.it.bank.application;

import ee.bsc.vali.it.bank.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;


//Principal principal, @AuthenticationPrincipal UserDetails userDetails to get user details.



@RestController
public class BankController {

    @Autowired
    private BankService accountService;

    //Add new client //Principal principal
    @PostMapping("SQL/addclient")
    //public long addClient(@RequestBody Client client, Principal principal, @AuthenticationPrincipal UserDetails userDetails){
    public long addClient(@RequestBody Client client){
        //System.out.println(principal);
        return accountService.addClient(client);
    }

    //Add a new account to client
    @PostMapping("SQL/addaccount")
    public String addAccount(@RequestBody Account account){
        String number = accountService.addAccount(account.getClientId());
        return "\"" + number + "\"";
    }

    //Delete account
    @PostMapping("SQL/deleteaccount")
    public String deleteAccount(@RequestBody String accountToDel){
        return accountService.deleteAccount(accountToDel);
    }

    //Get account balance
    @GetMapping("SQL/{accountNr}")
    public String getAccountBalance(@PathVariable("accountNr") String accountNr) {
        return accountService.getAccountBalance(accountNr);
    }

    //Get account balance
    @PostMapping("SQL/balance")
    public String getAccountBalance2(@RequestBody String accountNr) {
        return accountService.getAccountBalance(accountNr);
    }

    //Get account id
    @GetMapping("SQL/getid/{accountNr}")
    public BigInteger getAccountId(@PathVariable("accountNr") String accountNr){
        return accountService.getAccountId(accountNr);
    }

    //Get account number
    @GetMapping("SQL/getnumber/{id}")
    public String getAccountId(@PathVariable("id") long id){
        return accountService.getAccountNr(id);
    }

    //Deposit money to account
    @PutMapping("SQL/deposit")
    public String depositMoney(@RequestBody DepositWithdraw request) {
        return accountService.depositMoney(request);
    }

    //Withdraw money from account
    @PutMapping("SQL/withdraw")
    public String withdrawMoney(@RequestBody DepositWithdraw request) {
        return accountService.withdrawMoney(request);
    }

    //Transfer money between two accounts
    @PutMapping("SQL/transfer")
    public String transferMoney(@RequestBody TransfereMoney transfer) {
        return accountService.transferMoney(transfer);
    }

    //Get All accounts
    @GetMapping("SQL/getallaccounts")
    public List<Account> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    //Get transaction history
    @GetMapping("SQL/transactionlog")
    public List<TransactionLog> getTransactionLog(){
        return accountService.getTransactionLog();
    }

    //Get transaction history
    @GetMapping("SQL/transactionlogtext")
    public String getTransactionLogText(){
        return accountService.transactionLogToText(accountService.getTransactionLog());
    }

    //
    @GetMapping("SQL/userdetails")
    public UserDetails getUserDetails(Principal principal, @AuthenticationPrincipal UserDetails userDetails){
        System.out.println(principal);
        System.out.println(userDetails);
        System.out.println(userDetails.getUsername());
        return userDetails;
    }
}
