package com.tp.batch;

import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

//11
//13 @Component //11 @component au demarrage je sais qu'il y a une composent qui represent itemprocess object
public class BankTransactionItemAnalyticsProcessor implements ItemProcessor<BankTransaction,BankTransaction> {

    @Getter//11 crer la function get methode
    private double totalDebit;
    @Getter
    private double totalCredit;

    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {

       if(bankTransaction.getTransactionType().equals("D"))
           totalDebit+=bankTransaction.getAmount();
       else if(bankTransaction.getTransactionType().equals("C"))
           totalCredit-=bankTransaction.getAmount();

        return bankTransaction;
    }
}
