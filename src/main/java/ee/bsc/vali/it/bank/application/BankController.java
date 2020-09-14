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


@RequestMapping("/api")
@RestController
public class BankController {

    @Autowired
    private BankService accountService;


    //Add new client //Principal principal
    @PostMapping("/addclient")
    public long addClient(@RequestBody Client client){
        return accountService.addClient(client);
    }

    //public long addClient(@RequestBody Client client, Principal principal, @AuthenticationPrincipal UserDetails userDetails){
    //System.out.println(principal);

    //Add a new account to client
    @PostMapping("/addaccount")
    public void addAccount(@RequestBody Account account){
        String number = accountService.addAccount(account.getClientId());
    }

    //Delete account
    @PostMapping("/deleteaccount")
    public String deleteAccount(@RequestBody String accountToDel){
        return accountService.deleteAccount(accountToDel);
    }

    //Get account balance
    @GetMapping("/{accountNr}")
    public String getAccountBalance(@PathVariable("accountNr") String accountNr) {
        return accountService.getAccountBalance(accountNr);
    }

    //Get account balance
    @PostMapping("/balance")
    public String getAccountBalance2(@RequestBody String accountNr) {
        return accountService.getAccountBalance(accountNr);
    }

    //Get account id
    @GetMapping("SQL/getid/{accountNr}")
    public BigInteger getAccountId(@PathVariable("accountNr") String accountNr){
        return accountService.getAccountId(accountNr);
    }

    //Get account id
    @GetMapping("/getnumber/{id}")
    public String getAccountId(@PathVariable("id") long id){
        return accountService.getAccountNr(id);
    }

    //Deposit money to account
    @PutMapping("/deposit")
    public String depositMoney(@RequestBody DepositWithdraw request) {
        return accountService.depositMoney(request);
    }

    //Withdraw money from account
    @PutMapping("/withdraw")
    public String withdrawMoney(@RequestBody DepositWithdraw request) {
        return accountService.withdrawMoney(request);
    }

    //Transfer money between two accounts
    @PutMapping("/transfer")
    public String transferMoney(@RequestBody TransfereMoney transfer) {
        return accountService.transferMoney(transfer);
    }

    //Get All accounts
    @GetMapping("/getallaccounts")
    public List<Account> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    //Get transaction history
    @GetMapping("/transactionlog")
    public List<TransactionLog> getTransactionLog(){
        return accountService.getTransactionLog();
    }

    //Get transaction history
    @GetMapping("/transactionlogtext")
    public String getTransactionLogText(){
        return accountService.transactionLogToText(accountService.getTransactionLog());
    }

    //Get user details
    @GetMapping("/userdetails")
    public UserDetails getUserDetails(Principal principal, @AuthenticationPrincipal UserDetails userDetails){
        System.out.println(principal);
        System.out.println(userDetails);
        System.out.println(userDetails.getUsername());
        return userDetails;
    }
}
