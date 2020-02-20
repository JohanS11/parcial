package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.MoneyLaunderingException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



@Service("MoneyLaunderingServiceStub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {

    /*public MoneyLaunderingServiceStub() {
        suspects.add(new SuspectAccount("1",100));
        suspects.add(new SuspectAccount("2",130));
        suspects.add(new SuspectAccount("3",110));
        suspects.add(new SuspectAccount("4",110));
    }*/

    private List<SuspectAccount> suspects = new CopyOnWriteArrayList<>();

    @Override
    public void addSuspectAccount(SuspectAccount suspect) throws MoneyLaunderingException {
        for (SuspectAccount suspectAccount:suspects) {
            if(suspectAccount.getAccountId().equals(suspect.getAccountId())){
                throw new MoneyLaunderingException("Este sospechoso ya ha sido registrado antes");
            }
        }
        suspects.add(suspect);

    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {

        return suspects;
    }

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyLaunderingException {

        SuspectAccount suspendAccount1 = getAccountStatus(suspectAccount.getAccountId());
        suspendAccount1.setAmountOfSmallTransactions(suspectAccount.getAmountOfSmallTransactions());

    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingException{

        //SuspectAccount susp = null;
        //SuspectAccount[] sospes;
        for (SuspectAccount sosp:suspects) {
            if (sosp.getAccountId().equals(accountId)) {
                return sosp;
            }
        }
        throw new MoneyLaunderingException("Esta cuenta no está registrada");

    }



}