package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.MoneyLaunderingException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;

import org.jboss.logging.Logger.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping( value = "/fraud-bank-accounts")
public class MoneyLaunderingController
{
    @Autowired
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getSuspects()
    {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{accountId}",method = RequestMethod.GET)
    public ResponseEntity<?> getSuspect(@PathVariable String accountId)
    {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addAccount(@RequestBody SuspectAccount suspect)
    {
        try {
            moneyLaunderingService.addSuspectAccount(suspect);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{accountId}",method = RequestMethod.PUT)
    public ResponseEntity<?> putAccount (@PathVariable String accountId,@RequestBody SuspectAccount suspect){
        try {
            moneyLaunderingService.updateAccountStatus(suspect);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
