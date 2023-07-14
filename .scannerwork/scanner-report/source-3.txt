package com.accenture.bars.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.accenture.bars.exception.BarsException;
import com.accenture.bars.domain.Record;
import com.accenture.bars.domain.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BarsController {

    private static final Logger log = LoggerFactory.getLogger(
            BarsController.class);

    @Autowired
    private FileProcessor fileProcessor;

    public BarsController() {
        //default constructor
    }

    @GetMapping("/bars")
    public List<Record> requestBilling(
            @RequestParam("filePath") String filePath)
            throws BarsException, IOException {

        if (filePath == null || filePath.trim().isEmpty()) {
            log.error(BarsException.PATH_DOES_NOT_EXIST);
            throw new BarsException(BarsException.PATH_DOES_NOT_EXIST,
                    HttpStatus.BAD_REQUEST);
        }

        File file = new File(filePath);

        if (!file.exists()) {
            log.error(BarsException.NO_SUPPORTED_FILE);
            throw new BarsException(BarsException.NO_SUPPORTED_FILE,
                    HttpStatus.BAD_REQUEST);
        }

        List<Request> requests = fileProcessor.execute(file);
        List<Record> records = fileProcessor.retrieveRecordfromDB(requests);

        if (records.isEmpty()) {
            log.error(BarsException.NO_REQUESTS_TO_READ);
            throw new BarsException(BarsException.NO_REQUESTS_TO_READ,
                    HttpStatus.BAD_REQUEST);
        }

        return records;
    }
}
