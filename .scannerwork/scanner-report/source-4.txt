package com.accenture.bars.controller;

import com.accenture.bars.domain.Record;
import com.accenture.bars.domain.Request;
import com.accenture.bars.entity.Billing;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.factory.InputFileFactory;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.repository.BillingRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(FileProcessor.class);

    private BillingRepository billingRepository;

    @Autowired
    public FileProcessor(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }


    public List<Request> execute(File file) throws BarsException, IOException {
        // get the instance of InputFileFactory singleton
        InputFileFactory factory = InputFileFactory.getInstance();

        // get the appropriate file from the factory class
        // by using getInputFile(file) method
        AbstractInputFile inputFile = factory.getInputFile(file);

        // set the file (call setFile(file) method)
        inputFile.setFile(file);

        // get the array list
        List<Request> requests = inputFile.readFile();

        // return list of request
        return requests;
    }


    public List<Record> retrieveRecordfromDB(List<Request> requests)
            throws BarsException {
        // initialize new arraylist
        List<Record> records = new ArrayList<>();

        // get the record from each request
        for (Request request : requests) {
            // get the billing inner join account and customer
            // where the billing cycle, start date and end date exist
            int billingCycle = request.getBillingCycle();
            LocalDate startDate = request.getStartDate();
            LocalDate endDate = request.getEndDate();

            List<Billing> billings =
                    billingRepository.findByBillingCycleAndStartDateAndEndDate
                            (billingCycle, startDate, endDate);

            if (billings.isEmpty()) {
                log.error(BarsException.NO_RECORDS_TO_WRITE);
                throw new BarsException(BarsException.NO_RECORDS_TO_WRITE,
                        HttpStatus.BAD_REQUEST);
            }

            // create a new record for each billing record found
            for (Billing billing : billings) {
                String firstName = billing.getAccountId().getCustomerId().
                        getFirstName();
                String lastName = billing.getAccountId().getCustomerId().
                        getLastName();
                Double amount = billing.getAmount();
                String accountName = billing.getAccountId().getAccountName();

                Record record = new Record();
                record.setFirstName(firstName);
                record.setLastName(lastName);
                record.setAmount(amount);
                record.setAccountName(accountName);
                record.setStartDate(startDate);
                record.setEndDate(endDate);
                record.setBillingCycle(billingCycle);

                records.add(record);
            }
        }

        writeOutput(records);

        return records;
    }

    public void writeOutput(List<Record> records) throws BarsException {
        for (Record record : records){
            log.info(""+record);
        }
    }
}
