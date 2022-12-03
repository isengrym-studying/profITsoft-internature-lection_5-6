import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.klieshchunov.ParsingUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParsingUtilsTest {

    @Test
    public void testGenerateLawViolationsStatistic() throws IOException {
        String inputPath = "src/main/resources/lawViolations";
        String outputPath = "src/main/resources/lawViolations/statistics.xml";

        ParsingUtils.generateLawViolationStatistics(inputPath, outputPath);

        String actual = Files.readString(Paths.get(outputPath));
        String expected = """
                <lawViolations>
                  <items>
                    <SPEEDING>1.249506E7</SPEEDING>
                    <ALCOHOL_INTOXICATION>697000.0</ALCOHOL_INTOXICATION>
                    <NO_DRIVER_LICENSE_AT_ALL>562400.0</NO_DRIVER_LICENSE_AT_ALL>
                    <NOT_WEARING_SEATBELT>140250.0</NOT_WEARING_SEATBELT>
                    <NO_LICENSE_PLATE>129200.0</NO_LICENSE_PLATE>
                    <PARKING>121590.0</PARKING>
                  </items>
                </lawViolations>
                """;
        // For some reason 'expected' and 'actual' have got different line breakers (LF and CRLF).
        // So for the test to be successful, I need to make a little change
        expected = expected.replaceAll("\n", "\r\n");

        Assertions.assertEquals(expected, actual);

    }
}
