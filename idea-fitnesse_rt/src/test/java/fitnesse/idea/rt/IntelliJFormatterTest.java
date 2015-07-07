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
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntelliJFormatterTest {

    private ByteArrayOutputStream out;
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

    @Test
    public void testOutputChunkWithNewline() throws IOException {
        formatter.testOutputChunk("<br/><br/>Simple example (no namespacing)<br/><br/>");

        assertThat(out.toString(), is("\n\nSimple example (no namespacing)\n\n"));
    }

    @Test
    public void testOutputChunkWithTable() throws IOException {
        formatter.testOutputChunk("<table>\n" +
                "\t<tr class=\"slimRowTitle\">\n" +
                "\t\t<td>import</td>\n" +
                "\t</tr>\n" +
                "\t<tr class=\"slimRowColor0\">\n" +
                "\t\t<td><span class=\"pass\">fixtures</span></td>\n" +
                "\t</tr>\n" +
                "</table>");

        assertThat(out.toString().replace('\u001B', '^'), is(
                " | import    |\n" +
                " | ^[42mfixtures^[0m  |\n"));
    }

    @Test
    public void testOutputChunkWithList() throws IOException {
        formatter.testOutputChunk("<br/><ul>\n"+
                        "\t<li>list item 1</li>\n"+
                        "\t<li>list item 2</li>\n"+
                        "</ul>\n");

        assertThat(out.toString(), is("\n\n\tlist item 1\n\tlist item 2\n\n"));
    }

    @Test
    public void resultStates() throws IOException {
        formatter.testOutputChunk("<table>\n" +
                "\t<tr>\n" +
                "\t\t<td><span class=\"pass\">pass me</span></td>\n" +
                "\t\t<td><span class=\"fail\">fail me</span></td>\n" +
                "\t\t<td><span class=\"error\">error me</span></td>\n" +
                "\t\t<td><span class=\"ignore\">ignore me</span></td>\n" +
                "\t</tr>\n" +
                "</table>");

        System.out.println(out.toString());
        assertThat(out.toString().replace('\u001B', '^'), is(" | ^[42mpass me^[0m  | ^[41mfail me^[0m  | ^[43merror me^[0m  | ^[46mignore me^[0m  |\n"));
    }

    @Test
    public void layoutTable() throws IOException {
        formatter.testOutputChunk("<table>\n" +
                "\t<tr>\n" +
                "\t\t<td colspan=\"2\">Foo</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>one</td>\n" +
                "\t\t<td>longer cell</td>\n" +
                "\t</tr>\n" +
                "\t<tr>\n" +
                "\t\t<td>three</td>\n" +
                "\t\t<td>four</td>\n" +
                "\t</tr>\n" +
                "</table>");

        System.out.println(out.toString());
        assertThat(out.toString().replace('\u001B', '^'), is(
                " | Foo                 |\n" +
                " | one   | longer cell |\n" +
                " | three | four        |\n"));
    }

}