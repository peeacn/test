package com.accenture.bars.factory;

import com.accenture.bars.exception.BarsException;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.file.CSVInputFileImpl;
import com.accenture.bars.file.TextInputFileImpl;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class InputFileFactoryTest {

    @Test
    void testGetInstance() {
        InputFileFactory instance1 = InputFileFactory.getInstance();
        InputFileFactory instance2 = InputFileFactory.getInstance();
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }
    @Test
    public void testGetInputFileTxt() throws BarsException {
        File inputFile = new File("C:/BARS_TEST/valid-txt.txt");
        InputFileFactory factory = InputFileFactory.getInstance();
        AbstractInputFile inputFileObj = factory.getInputFile(inputFile);
        assertTrue(inputFileObj instanceof TextInputFileImpl);
    }

    @Test
    public void testGetInputFileCsv() throws BarsException {
        File inputFile = new File("C:/BARS_TEST/valid-csv.csv");
        InputFileFactory factory = InputFileFactory.getInstance();
        AbstractInputFile inputFileObj = factory.getInputFile(inputFile);
        assertTrue(inputFileObj instanceof CSVInputFileImpl);
    }

    @Test
    public void testFileNotSupported() throws BarsException {
        File inputFile = new File("C:/BARS_TEST/unsupported-file.png");
        InputFileFactory factory = InputFileFactory.getInstance();
        Exception exception = assertThrows(BarsException.class, () -> {
            factory.getInputFile(inputFile);
        });
        String expectedMessage = "File is not supported for processing.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}