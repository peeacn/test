package com.accenture.bars.file;

import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVInputFileImplTest {

    @Test
    public void testReadValidRequestParameter() throws BarsException, IOException {

        String filePath = "C:/BARS_TEST/valid-csv.csv";
        CSVInputFileImpl csvInputFile = new CSVInputFileImpl();
        csvInputFile.setFile(new File(filePath));
        List<Request> requests = csvInputFile.readFile();
        Assertions.assertEquals(2, requests.size(), "Expected 2 requests to be read from input file");
        Request request1 = requests.get(0);
        Assertions.assertEquals(1, request1.getBillingCycle(), "Expected billing cycle of first request to be 1");
        Assertions.assertEquals("2013-01-15", request1.getStartDate().toString(), "Expected start date of first request to be 2013-01-15");
        Assertions.assertEquals("2013-02-14", request1.getEndDate().toString(), "Expected end date of first request to be 2013-02-14");
        Request request2 = requests.get(1);
        Assertions.assertEquals(1, request2.getBillingCycle(), "Expected billing cycle of second request to be 1");
        Assertions.assertEquals("2016-01-15", request2.getStartDate().toString(), "Expected start date of second request to be 2016-01-15");
        Assertions.assertEquals("2016-02-14", request2.getEndDate().toString(), "Expected end date of second request to be 2016-02-14");
    }

    @Test
    void testInvalidBillingCycleParameter() throws IOException {
        File file = new File("C:/BARS_TEST/billing-cycle-not-on-range-csv.csv");
        CSVInputFileImpl csvInputFile = new CSVInputFileImpl();
        csvInputFile.setFile(file);
        BarsException exception = assertThrows(BarsException.class, csvInputFile::readFile);
        assertEquals("ERROR: Billing Cycle not on range at row 4", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testInvalidStartDateFormatParameter() {
        try {
            CSVInputFileImpl inputFile = new CSVInputFileImpl();
            inputFile.setFile(new File("C:/BARS_TEST/invalid-start-date-csv.csv"));
            List<Request> requests = inputFile.readFile();
            fail("Expected BarsException was not thrown");
        } catch (IOException e) {
            fail("Unexpected IOException was thrown: " + e.getMessage());
        } catch (BarsException e) {
            assertEquals("ERROR: Invalid Start Date format at row 1", e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        }
    }

    @Test
    public void testInvalidEndDateFormatParameter() {
        try {
            String filePath = "C:/BARS_TEST/invalid-end-date-csv.csv";
            CSVInputFileImpl csvInputFile = new CSVInputFileImpl();
            csvInputFile.setFile(new File(filePath));

            List<Request> requests = csvInputFile.readFile();
            fail("Expected BarsException was not thrown");
        } catch (IOException e) {
            fail("Unexpected IOException was thrown: " + e.getMessage());
        } catch (BarsException e) {
            assertEquals("ERROR: Invalid End Date format at row 7", e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        }
    }
}