package com.tp.batch;


import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class BankRestController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Autowired
    private BankTransactionItemAnalyticsProcessor analyticsProcessor;

    public BankRestController(JobLauncher jobLauncher, Job job){
        this.job=job;
        this.jobLauncher=jobLauncher;
    }
    @GetMapping("/")
    public BatchStatus load() throws Exception{
        Map<String, JobParameter> params=new HashMap<>();
        //10 parametrer le job par la creation d'un map qui contient des objet de type JobParameter
        params.put("time",new JobParameter(System.currentTimeMillis()));// ce jobParameter contient la date system
        JobParameters jobParameters=new JobParameters(params);
        JobExecution jobExecution=jobLauncher.run(job,jobParameters);
        //10 lancer le job
        while(jobExecution.isRunning()){
            System.out.println("..............");
        }
        //10 return l'etat de job (fini, demarrage, en cour d'exeucution)
        return jobExecution.getStatus();
    }

    //14
    @GetMapping("/analytics")
    public Map<String,Double> analytics(){
        Map<String,Double> map=new HashMap<>();
        map.put("totaCredit",analyticsProcessor.getTotalCredit());
        map.put("totaDebit",analyticsProcessor.getTotalDebit());
        return map;
    }
}
