package fitnesse.idea.rt;

import fitnesse.reporting.Formatter;
import fitnesse.testrunner.TestsRunnerListener;
import fitnesse.testrunner.WikiTestPageUtil;
import fitnesse.testsystems.*;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.fs.FileSystemPage;
import org.apache.commons.lang.StringEscapeUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * This Formatter produces output in a format understood by IntelliJ.
 */
public class IntelliJFormatter implements Formatter, TestsRunnerListener {
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String CSI = "\u001B["; // "Control Sequence Initiator"
    private static final Pattern ANSI_ESCAPE_PATTERN = Pattern.compile("\u001B\\[.*?m");

    private final PrintStream out;

    private ExceptionResult exceptionOccurred;

    public IntelliJFormatter() {
        this(System.out);
    }

    public IntelliJFormatter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void testSystemStarted(TestSystem testSystem) {
        log("##teamcity[testSuiteStarted name='%s' locationHint='' captureStandardOutput='true']", testSystem.getName());
    }

    @Override
    public void testStarted(TestPage testPage) {
        log("##teamcity[testStarted name='%s' locationHint='%s' captureStandardOutput='true']", testPage.getFullPath(), locationHint(testPage));
    }

    private String locationHint(TestPage testPage) {
        WikiPage wikiPage = WikiTestPageUtil.getSourcePage(testPage);
        if (wikiPage instanceof FileSystemPage) {
            File fileSystemPath = ((FileSystemPage) wikiPage).getFileSystemPath();
            try {
                return "fitnesse://" + new File(fileSystemPath, "content.txt").getCanonicalPath();
            } catch (IOException e) {
                error("Can not determine canonical file location for " + fileSystemPath, e);
                return "";
            }
        }
        return "";
    }

    @Override
    public void testOutputChunk(String output) {
        try {
            NodeList nodes = new Parser(new Lexer(output)).parse(null);
            print(translate(nodes));
        } catch (ParserException e) {
            log("Unparsable wiki output: %s", output);
        }
    }

    private String translate(NodeList nodes) {
        StringBuilder sb = new StringBuilder();
        translate(nodes, sb);
        return sb.toString();
    }

    private void translate(NodeList nodes, StringBuilder sb) {
        if (nodes == null) return;

        for (Node node : nodeListIterator(nodes)) {
            if (node instanceof TableTag) {
                sb.append(translateTable(node.getChildren()));
            } else if (node instanceof Span) {
                Span span = (Span) node;
                sb.append(colorResult(span.getAttribute("class")))
                    .append(span.getChildrenHTML())
                    .append(CSI + "0m");
            } else if (node instanceof Tag && "BR".equals(((Tag) node).getTagName())) {
                sb.append(NEWLINE);
            } else if (node.getChildren() != null) {
                translate(node.getChildren(), sb);
            } else if (node instanceof Text){
                sb.append(node.getText());
            }
        }
    }

    // See https://en.wikipedia.org/wiki/ANSI_escape_code for the codes
    private String colorResult(String result) {
        if ("pass".equals(result)) {
            return CSI + "30;42m";
        } else if ("fail".equals(result)) {
            return CSI + "30;41m";
        } else if ("error".equals(result)) {
            return CSI + "30;43m";
        } else if ("ignore".equals(result)) {
            return CSI + "30;46m";
        }
        return "";
    }

    private String translateTable(NodeList nodes) {
        List<List<Integer>> table = new ArrayList<List<Integer>>();
        for (Node row : rowIterator(nodes)) {
            List<Integer> tableRow = new ArrayList<Integer>();
            for (Node cell : columnIterator(row)) {
                tableRow.add(cellLength(translate(cell.getChildren())));
            }
            table.add(tableRow);
        }

        TableFormatter tableFormatter = new TableFormatter(table);
        StringBuilder sb = new StringBuilder();
        int rowNr = 0;
        for (Node row : rowIterator(nodes)) {
            int colNr = 0;
            for (Node cell : columnIterator(row)) {
                sb.append("|")
                        .append(tableFormatter.leftPaddingString())
                        .append(translate(cell.getChildren()))
                        .append(tableFormatter.rightPaddingString(rowNr, colNr));
                colNr++;
            }
            sb.append("|\n");
            rowNr++;
        }
        return sb.toString();
    }

    private Iterable<Node> columnIterator(Node row) {
        return nodeListIterator(row.getChildren().extractAllNodesThatMatch(new NodeClassFilter(TableColumn.class)));
    }

    private Iterable<Node> rowIterator(NodeList nodes) {
        return nodeListIterator(nodes.extractAllNodesThatMatch(new NodeClassFilter(TableRow.class)));
    }

    private Iterable<Node> nodeListIterator(NodeList nodes) {
        final SimpleNodeIterator iter = nodes.elements();
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {
                    @Override public boolean hasNext() { return iter.hasMoreNodes(); }
                    @Override public Node next() { return iter.nextNode(); }
                    @Override public void remove() { throw new IllegalStateException("NodeList iterator.remove() should not have been called"); }
                };
            }
        };
    }

    @Override
    public void testComplete(TestPage testPage, TestSummary summary) {
        String fullPath = testPage.getFullPath();
        if (exceptionOccurred != null) {
            log("##teamcity[testFailed name='%s' message='%s' error='true']", fullPath, exceptionOccurred.getMessage() != null ? exceptionOccurred.getMessage().replace("'", "|'") : exceptionOccurred.toString());
            exceptionOccurred = null;
        } else if (summary.getWrong() > 0 || summary.getExceptions() > 0) {
            log("##teamcity[testFailed name='%s' message='Test failed: R:%d W:%d I:%d E:%d']", fullPath, summary.getRight(), summary.getWrong(), summary.getIgnores(), summary.getExceptions());
        } else {
            log("##teamcity[testFinished name='%s']", fullPath);
        }
    }

    @Override
    public void testAssertionVerified(Assertion assertion, TestResult testResult) {
        // Nothing to do here
    }

    @Override
    public void testExceptionOccurred(Assertion assertion, ExceptionResult exceptionResult) {
        if (exceptionOccurred == null) {
            exceptionOccurred = exceptionResult;
        }
    }

    @Override
    public void testSystemStopped(TestSystem testSystem, Throwable cause) {
        log("##teamcity[testSuiteFinished name='%s']", testSystem.getName());
    }


    @Override
    public void announceNumberTestsToRun(int i) {
        log("##teamcity[testCount count='%d']", i);
    }

    @Override
    public void unableToStartTestSystem(String s, Throwable throwable) {
        log("Unable to start test system %s", s);
        throwable.printStackTrace(out);
    }

    private void log(String s, Object... args) {
        out.println(format(s, args));
        out.flush();
    }

    private void print(String s) {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        try {
            StringEscapeUtils.unescapeXml(writer, s);
            writer.flush();
        } catch (IOException e) {
            error("Unable to write output", e);
        }
    }

    private void error(String message, Throwable cause) {
        System.err.println(message);
        cause.printStackTrace(System.err);
    }

    private static int cellLength(String tableCell) {
        return ANSI_ESCAPE_PATTERN.matcher(tableCell.split("\n")[0]).replaceAll("").length();
    }

}
