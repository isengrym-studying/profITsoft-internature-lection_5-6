import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.klieshchunov.taskOne.ParsingUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParsingUtilsTest {

    public final String inputPath = "src/main/resources/lawViolations";
    public final String outputPath = "src/main/resources/lawViolations/statistics.xml";

    @Test
    public void testGenerateLawViolationsStatistic() throws IOException {
        ParsingUtils.generateLawViolationStatistics(inputPath, outputPath);

        String actual = Files.readString(Paths.get(outputPath));
        String expected = """
                <lawViolations>
                  <items>
                    <SPEEDING>1.3587426E8</SPEEDING>
                    <ALCOHOL_INTOXICATION>9061000.0</ALCOHOL_INTOXICATION>
                    <NOT_WEARING_SEATBELT>817530.0</NOT_WEARING_SEATBELT>
                    <PARKING>338310.0</PARKING>
                    <NO_DRIVER_LICENSE_AT_ALL>14800.0</NO_DRIVER_LICENSE_AT_ALL>
                    <NO_LICENSE_PLATE>3400.0</NO_LICENSE_PLATE>
                  </items>
                </lawViolations>
                """;

        // For some reason 'expected' and 'actual' have got different line breakers (LF and CRLF).
        // So for the test to be successful, I need to make a little change
        expected = expected.replaceAll("\n", "\r\n");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMethodPerformance_generateLawViolationsStatistic() {
        int numberOfExperiments = 5;
        float[] results = new float[numberOfExperiments];

        for (int j = 0; j < numberOfExperiments; j++) {
            long start = System.nanoTime();

            for (int i = 0; i <= 500; i++)
                ParsingUtils.generateLawViolationStatistics(inputPath, outputPath);

            results[j] = toSeconds(System.nanoTime() - start);
        }


        System.out.println(String.format("Average time: %s s",average(results)));
    }

    private float toSeconds(long nano) {
        return nano/1000000000F;
    }

    private float average(float[] array) {
        int i = 0;
        float sum = 0;
        while (i < array.length) {
            sum += array[i];
            i++;
        }
        return sum/array.length;
    }

}
