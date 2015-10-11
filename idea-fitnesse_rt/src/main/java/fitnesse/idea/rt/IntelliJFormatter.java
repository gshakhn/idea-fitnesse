package fitnesse.idea.rt;

import fitnesse.reporting.Formatter;
import fitnesse.testrunner.TestsRunnerListener;
import fitnesse.testrunner.WikiTestPageUtil;
import fitnesse.testsystems.*;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.fs.FileSystemPage;
import fitnesse.wiki.fs.FileSystemPageFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;

/**
 * This Formatter produces output in a format understood by IntelliJ.
 */
public class IntelliJFormatter implements Formatter, TestsRunnerListener {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final PrintStream out;

    private ExceptionResult exceptionOccurred;

    public IntelliJFormatter() {
        this(System.out);
    }

    public IntelliJFormatter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void testSystemStarted(TestSystem testSystem) throws IOException {
        log("##teamcity[testSuiteStarted name='%s' locationHint='' captureStandardOutput='true']", testSystem.getName());
    }

    @Override
    public void testStarted(TestPage testPage) throws IOException {
        log("##teamcity[testStarted name='%s' locationHint='%s' captureStandardOutput='true']", testPage.getFullPath(), locationHint(testPage));
    }

    private String locationHint(TestPage testPage) throws IOException {
        WikiPage wikiPage = WikiTestPageUtil.getSourcePage(testPage);
        if (wikiPage instanceof FileSystemPage) {
            return "fitnesse://" + new File(((FileSystemPage) wikiPage).getFileSystemPath(), "content.txt").getCanonicalPath();
        }
        return "";
    }

    @Override
    public void testOutputChunk(String output) throws IOException {
        try {
            NodeList nodes = new Parser(new Lexer(output)).parse(null);
            print(translate(nodes));
        } catch (ParserException e) {
            log("Unparsable wiki output: %s", output);
        }
    }

    private String translate(NodeList nodes) throws IOException {
        if (nodes == null) return "";

        StringBuilder sb = new StringBuilder();

        for (Node node : nodeListIterator(nodes)) {
            if (node instanceof TableTag) {
                sb.append(translateTable(node.getChildren()));
            } else if (node instanceof Span) {
                Span span = (Span) node;
                String result = span.getAttribute("class");
                if ("pass".equals(result)) {
                    sb.append("\u001B[42m");
                } else if ("fail".equals(result)) {
                    sb.append("\u001B[41m");
                } else if ("error".equals(result)) {
                    sb.append("\u001B[43m");
                } else if ("ignore".equals(result)) {
                    sb.append("\u001B[46m");
                }
                sb.append(span.getChildrenHTML());
                sb.append("\u001B[0m ");
            } else if (node instanceof Tag && "BR".equals(((Tag) node).getTagName())) {
                sb.append(NEWLINE);
            } else if (node.getChildren() != null) {
                sb.append(translate(node.getChildren()));
            } else {
                sb.append(node.getText());
            }
        }
        return sb.toString();
    }

    private String translateTable(NodeList nodes) throws IOException {
        List<List<Cell>> table = new ArrayList<List<Cell>>();
        List<Integer> rowWidths = new ArrayList<Integer>();
        for (Node row : nodeListIterator(nodes.extractAllNodesThatMatch(new NodeClassFilter(TableRow.class)))) {
            List<Cell> tableRow = new ArrayList<Cell>();
            int rowNr = 0;
            for (Node cell : nodeListIterator(row.getChildren().extractAllNodesThatMatch(new NodeClassFilter(TableColumn.class)))) {
                Cell tableCell = new Cell(translate(cell.getChildren()), ((TableColumn) cell).getAttribute("colspan"));
                tableRow.add(tableCell);
                if (rowNr < rowWidths.size()) {
                    rowWidths.set(rowNr, Math.max(rowWidths.get(rowNr), tableCell.length));
                } else {
                    rowWidths.add(tableCell.length);
                }
                rowNr++;
            }
            table.add(tableRow);
        }

        StringBuilder sb = new StringBuilder();
        for (List<Cell> row : table) {
            int rowNr = 0;
            for (Cell cell : row) {
                sb.append(" | ").append(cell).append(padding(cell, rowWidths, rowNr));
                rowNr += cell.colspan;
            }
            sb.append(" |\n");
        }
        return sb.toString();
    }

    private char[] padding(Cell cell, List<Integer> rowWidths, int rowNr) {
        int w = 0;
        for (int i = 0; i < cell.colspan && rowNr + i < rowWidths.size(); i++) w += rowWidths.get(rowNr + i);
        w += (cell.colspan - 1) * 3; // add extra width for cell separators (" | ")
        return padding(w - cell.length);
    }

    private char[] padding(int i) {
        char[] chars = new char[i > 0 ? i : 0];
        Arrays.fill(chars, ' ');
        return chars;
    }

    private Iterable<Node> nodeListIterator(NodeList nodes) {
        final SimpleNodeIterator iter = nodes.elements();
        return new Iterable<Node>() {
            @Override
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {
                    @Override public boolean hasNext() { return iter.hasMoreNodes(); }
                    @Override public Node next() { return iter.nextNode(); }
                    @Override public void remove() { }
                };
            }
        };
    }

    @Override
    public void testComplete(TestPage testPage, TestSummary summary) throws IOException {
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
    public void unableToStartTestSystem(String s, Throwable throwable) throws IOException {

    }

    private void log(String s, Object... args) {
        out.println(format(s, args));
    }

    private void print(String s) throws IOException {
        out.print(s);
    }

    private static class Cell {
        private final String cellContent;
        private final int length;
        private final int colspan;

        private Cell(String cellContent, String colspan) {
            this.cellContent = cellContent;
            this.length = cellLength(cellContent);
            this.colspan = parseInt(colspan);
        }

        @Override
        public String toString() {
            return cellContent;
        }

        private static int cellLength(String tableCell) {
            return tableCell.replaceAll("\u001B.*?m", "").split("\n")[0].length();
        }

        private static int parseInt(String colspan) {
            try {
                return Integer.parseInt(colspan);
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }
}
