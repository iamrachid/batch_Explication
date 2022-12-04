package com.tp.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

//7-2
//13 on va enlever pour ne pas instancier lors du demarrage et pour faire le travail
// dans le springBatchConfi.java
//@Component //7-2 @component au demarrage je sais qu'il y a une composent qui represent itemprocess object
public class BankTransactionItemProcessor implements ItemProcessor<BankTransaction,BankTransaction> {

    private SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/YYYY-HH:mm");
    //7-2 la methodprocess va prendre l'objet bankTransaction et affecter la date au variable de type
    //    date, apres qu'on affecter la bonne format on return ce objet
    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {
        bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
        return bankTransaction;
    }
}
