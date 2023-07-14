package com.accenture.bars.file;

import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.factory.InputFileFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextInputFileImplTest {

    @Test
    public void testReadValidRequestParameter() throws IOException, BarsException {
        File inputFile = new File("C:/BARS_TEST/valid-txt.txt");
        TextInputFileImpl textInputFileImpl = new TextInputFileImpl();
        textInputFileImpl.setFile(inputFile);
        List<Request> requests = textInputFileImpl.readFile();

        assertEquals(2, requests.size());
        assertEquals(new Request(1,
                LocalDate.of(2013, 1, 15),
                LocalDate.of(2013, 2, 14)),
                requests.get(0));
        assertEquals(new Request(1,
                LocalDate.of(2016, 1, 15),
                LocalDate.of(2016, 2, 14)),
                requests.get(1));
    }

    @Test
    public void testInvalidBillingCycleParameter() {
        try {
            File file = new File("C:/BARS_TEST/billing-cycle-not-on-range-txt.txt");
            TextInputFileImpl textInputFile = new TextInputFileImpl();
            textInputFile.setFile(file);
            if (file == null) {
                throw new BarsException("File is null.", HttpStatus.BAD_REQUEST);
            }
            List<Request> requests = textInputFile.readFile();
            fail("Expected BarsException to be thrown.");
        } catch (BarsException e) {
            assertEquals("ERROR: Billing Cycle not on range at row 3", e.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
        } catch (IOException e) {
            fail("Unexpected IOException occurred.");
        }
    }

    @Test
    public void testInvalidStartDateFormatParameter() {
        // Arrange
        String filePath = "C:/BARS_TEST/invalid-start-date-txt.txt";
        TextInputFileImpl textInputFile = new TextInputFileImpl();
        textInputFile.setFile(new File(filePath));

        // Act and Assert
        BarsException barsException = Assertions.assertThrows(BarsException.class, () -> {
            List<Request> requests = textInputFile.readFile();
        }, "BarsException to be thrown indicating that the Start Date format is invalid.");
        Assertions.assertEquals(BarsException.INVALID_START_DATE_FORMAT + "3", barsException.getMessage(), "BarsException message to indicate the invalid start date format at line 3");
    }

    @Test
    public void testInvalidEndDateFormatParameter() {
        // Arrange
        String filePath = "C:/BARS_TEST/invalid-end-date-txt.txt";
        TextInputFileImpl textInputFile = new TextInputFileImpl();
        File file = new File(filePath); // create File object from file path
        textInputFile.setFile(file);

        // Act and Assert
        BarsException barsException = Assertions.assertThrows(BarsException.class, () -> {
            List<Request> requests = textInputFile.readFile();
        });
        Assertions.assertEquals(BarsException.INVALID_END_DATE_FORMAT + "1", barsException.getMessage(), "BarsException message to indicate the invalid end date format at line 1");
    }
}