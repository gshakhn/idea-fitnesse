package fitnesse.idea.rt;

import fitnesse.testrunner.WikiTestPage;
import fitnesse.testsystems.*;
import fitnesse.testsystems.fit.CommandRunningFitClient;
import fitnesse.testsystems.fit.FitTestSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntelliJFormatterTest {

    private OutputStream out;
    private IntelliJFormatter formatter;

    @Before
    public void before() {
        out = new ByteArrayOutputStream();
        formatter = new IntelliJFormatter(new PrintStream(out));
    }

    @Test
    public void tellsNumberOfTestsToRun() {
        formatter.announceNumberTestsToRun(3);

        assertThat(out.toString(), is("##teamcity[testCount count='3']\n"));
    }

    @Test
    public void testSystemStarted() throws IOException {
        formatter.testSystemStarted(new FitTestSystem("test system name", new CommandRunningFitClient(null)));

        assertThat(out.toString(), is("##teamcity[testSuiteStarted name='test system name' locationHint='' captureStandardOutput='true']\n"));
    }

    @Test
    public void testSystemStopped() throws IOException {
        formatter.testSystemStopped(new FitTestSystem("test system name", new CommandRunningFitClient(null)), null);

        assertThat(out.toString(), is("##teamcity[testSuiteFinished name='test system name']\n"));
    }

    @Test
    public void testStarted() throws IOException {
        formatter.testStarted(new WikiTestPage(null) {
            @Override
            public String getFullPath() {
                return "FullPath";
            }
        });

        assertThat(out.toString(), is("##teamcity[testStarted name='FullPath' locationHint='' captureStandardOutput='true']\n"));
    }

    @Test
    public void testCompleteSuccessful() throws IOException {
        formatter.testComplete(new WikiTestPage(null) {
            @Override
            public String getFullPath() {
                return "FullPath";
            }
        }, new TestSummary(1, 0, 0, 0));

        assertThat(out.toString(), is("##teamcity[testFinished name='FullPath']\n"));
    }

    @Test
    public void testCompleteWithWrong() throws IOException {
        formatter.testComplete(new WikiTestPage(null) {
            @Override
            public String getFullPath() {
                return "FullPath";
            }
        }, new TestSummary(1, 1, 0, 0));

        assertThat(out.toString(), is("##teamcity[testFailed name='FullPath' message='message' error='true']\n"));
    }

    @Test
    public void testCompleteWithExceptions() throws IOException {
        formatter.testComplete(new WikiTestPage(null) {
            @Override
            public String getFullPath() {
                return "FullPath";
            }
        }, new TestSummary(1, 0, 0, 1));

        assertThat(out.toString(), is("##teamcity[testFailed name='FullPath' message='message' error='true']\n"));
    }

    @Test
    public void testCompleteWithOccurredExceptions() throws IOException {
        formatter.testExceptionOccurred(null, new ExceptionResult() {
            @Override
            public ExecutionResult getExecutionResult() {
                return null;
            }

            @Override
            public String getMessage() {
                return "*message*";
            }
        });

        formatter.testComplete(new WikiTestPage(null) {
            @Override
            public String getFullPath() {
                return "FullPath";
            }
        }, new TestSummary(1, 0, 0, 1));

        assertThat(out.toString(), is("##teamcity[testFailed name='FullPath' message='*message*' error='true']\n"));
    }

}