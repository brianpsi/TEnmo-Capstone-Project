package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Account getAccountById(@NotNull @PathVariable Long id){
        return accountDao.getAccountById(id);
    }

    @RequestMapping(path="/user/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@NotNull @PathVariable Long id) {
        return accountDao.getAccountByUserId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Account createAccount(@Valid @RequestBody Account account) {
        return accountDao.createAccount(account);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteAccount(@NotNull @PathVariable Long id) {
        accountDao.deleteAccount(id);
    }

    @RequestMapping(path = "/user/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@NotNull @PathVariable Long id) {
       return accountDao.getBalanceByUserId(id);
    }

    @RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByAccountId(@NotNull @PathVariable Long id) {
        return accountDao.getBalanceByAccountId(id);
    }

    @Transactional
    @RequestMapping(path = "/send/{sender}/{receiver}/{amount}", method = RequestMethod.POST)
    public void sendTeBucks (@PathVariable Long sender, @PathVariable Long receiver, @PathVariable BigDecimal amount) {
//        if (getBalanceByUserId(sender).compareTo(amount)<0) {
//            return;
//        }
        accountDao.sendTeBucks(sender,receiver,amount);
         Transfer newTransfer = new Transfer();
         newTransfer.setAccountFrom(sender);
         newTransfer.setAccountTo(receiver);
         newTransfer.setAmount(amount);
         newTransfer.setTransferTypeId(2);
         newTransfer.setTransferStatusId(2);
         transferDao.createTransfer(newTransfer);
    }

    @Transactional
    @RequestMapping(path = "/receive/{receiver}/{sender}/{amount}", method = RequestMethod.POST)
    public void receiveTeBucks (@PathVariable Long receiver, @PathVariable Long sender, @PathVariable BigDecimal amount) {
        accountDao.receiveTeBucks(receiver,sender,amount);
        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(receiver);
        newTransfer.setAccountTo(sender);
        newTransfer.setAmount(amount);
        newTransfer.setTransferTypeId(1);
        newTransfer.setTransferStatusId(1);
        transferDao.createTransfer(newTransfer);
    }



}
