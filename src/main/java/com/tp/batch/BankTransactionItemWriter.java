package com.tp.batch;

import java.util.List;

import com.tp.batch.BankTransaction;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BankTransactionItemWriter implements ItemWriter<BankTransaction> {

    @Autowired
    private BankTransactionRepository bankTransactionRepository;


    @Override
    public void write(List<? extends BankTransaction> list) throws Exception {

        bankTransactionRepository.saveAll(list);

    }

}