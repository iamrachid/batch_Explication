package com.tp.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

//1
@Configuration
//1: cette annotation permet automatiquement d'activer spring batch
@EnableBatchProcessing
public class SpringBatchConfig {
    //3:
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<BankTransaction> bankTransactionItemReader;
    @Autowired private ItemWriter<BankTransaction> bankTransactionItemWriter;
    //12 commenter cette ligne aussi pour travailler avec le compositeItemProcessor
    // @Autowired private ItemProcessor<BankTransaction,BankTransaction> bankTransactionBankTransactionItemProcessor;

    //4:
    @Bean
    public Job bankJob(){
        //4:creer d'abord un step
        //4 gepour donner le nom a un step
        Step step1=stepBuilderFactory.get("step-load")
                .<BankTransaction,BankTransaction>chunk(100)
                .reader(bankTransactionItemReader)
                //12 on a commenter cette ligne parceque on veut travailler avec un seule process
                // .processor(bankTransactionBankTransactionItemProcessor)
                //12 associer tous les process avec cette  step -- etablir le concept de compositeProcess
                .processor(compositeItemProcessor())
                .writer(bankTransactionItemWriter)
                .build();

        //5: retourner le job
        return  jobBuilderFactory.get("bank-data")
                .start(step1)
                .build();
    }

    //6 FlatFileItemReader est parmi les objets de types itemReader
    //6 por le path du fichier que je vais ecrire dans getItemReader comme parametre , je vais ecrire
    //6 dans application.properties sous le nom inputFile
    @Bean //bean c-a-d qu'il va creer au demarrage de l'execution
    public FlatFileItemReader<BankTransaction> getItemReader(@Value("${inputFile}")Resource resource){

        FlatFileItemReader<BankTransaction> flatFileItemReader=new FlatFileItemReader<>();
        //6 donner un nom
        flatFileItemReader.setName("CVS-READER");
        //6 sauter la premiere ligne parceque il ne contient pas de donnée
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(resource);
        //6 lineMapper() est une function qui va traiter une line
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<BankTransaction> lineMapper() {
        //6 creer un objet de type DefaultLineMapper
        DefaultLineMapper<BankTransaction> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
        //6 specifier le separateur
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        //6 determiner les nom avec l'ordre des champs
        //6 ici on travaille avec la variable strTransactionDate et pas transactionDate pour
        //faire la convertir du string a une date
        lineTokenizer.setNames("id","accountID","strTransactionDate","transactionType","amount");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        //6 déterminer le type ciblé, pour lorsque il traite une line il va stocker les données dans
        // un objet de type BankTransaction
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }
    //13
    @Bean
    ItemProcessor<BankTransaction,BankTransaction> compositeItemProcessor(){
        //13 crer une liste de processors
        List<ItemProcessor<BankTransaction,BankTransaction>> itemProcessors=new ArrayList<>();
        itemProcessors.add(itemProcessor1());
        itemProcessors.add(itemProcessor2());
        //13 creer l'objet compositeItemProcessor
        CompositeItemProcessor<BankTransaction,BankTransaction> compositeItemProcessor=new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessors);
        return compositeItemProcessor;
    }

    //13 creer une methode qui fait le travaille de @Component pour appeler
    // la class BankTransactionItemProcessor
    @Bean
    BankTransactionItemProcessor itemProcessor1() {
        return new BankTransactionItemProcessor();
    }
    //13 creer une methode qui fait le travaille de @Component pour appeler
    // la class BankTransactionItemAnalyticsProcessor
    @Bean
    BankTransactionItemAnalyticsProcessor itemProcessor2() {
        return new BankTransactionItemAnalyticsProcessor();
    }


/*
    //7-1
    @Bean
    public ItemProcessor<BankTransaction,BankTransaction> itemProcessor(){
        return new ItemProcessor<BankTransaction, BankTransaction>() {
            @Override
            public BankTransaction process(BankTransaction bankTransaction) throws Exception {
                return null;
            }
        }
    }

 */
}
