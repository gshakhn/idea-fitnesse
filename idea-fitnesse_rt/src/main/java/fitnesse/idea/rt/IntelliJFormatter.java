package fitnesse.idea.rt;

import fitnesse.reporting.Formatter;
import fitnesse.testrunner.TestsRunnerListener;
import fitnesse.testrunner.WikiTestPage;
import fitnesse.testsystems.*;
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

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import static java.lang.String.format;

/*
 * Output:
 *
 * ##teamcity[testCount count='2']
 *
 * ##teamcity[testSuiteStarted name='FitNesseTestRunConfigurationProducerTest' locationHint='scalatest://TopOfClass:fitnesse.idea.run.FitNesseTestRunConfigurationProducerTestTestName:FitNesseTestRunConfigurationProducerTest' captureStandardOutput='true' nodeId='1' parentNodeId='0']
 *
 * ##teamcity[testStarted name='should retrieve wiki page name' locationHint='scalatest://LineInFile:fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest:FitNesseTestRunConfigurationProducerTest.scala:13TestName:should retrieve wiki page name' captureStandardOutput='true' nodeId='2' parentNodeId='1']
 *
 * ##teamcity[testFinished name='should retrieve wiki page name' duration='375'nodeId='2']
 *
 * ##teamcity[testStarted name='should retrieve nested wiki page name' locationHint='scalatest://LineInFile:fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest:FitNesseTestRunConfigurationProducerTest.scala:23TestName:should retrieve nested wiki page name' captureStandardOutput='true' nodeId='3' parentNodeId='1']
 *
 * ##teamcity[testFinished name='should retrieve nested wiki page name' duration='0'nodeId='3']
 *
 * ##teamcity[testSuiteFinished name='FitNesseTestRunConfigurationProducerTest'nodeId='1']
 *
 *
 * In case of an error, testFailed is sent instead of testFinished:
 *
 * ##teamcity[testFailed name='should retrieve nested wiki page name' message='Expected "SuitePage.TestPage[x|]", but got "SuitePage.TestPage[|]"|nScalaTestFailureLocation: fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest$$anonfun$2 at (FitNesseTestRunConfigurationProducerTest.scala:28)' details='org.scalatest.exceptions.TestFailedException: Expected "SuitePage.TestPage[x|]", but got "SuitePage.TestPage[|]"|n  at org.scalatest.Assertions$class.newAssertionFailedException(Assertions.scala:495)|n        at org.scalatest.FunSuite.newAssertionFailedException(FunSuite.scala:1555)|n    at org.scalatest.Assertions$class.assertResult(Assertions.scala:1226)|n      at org.scalatest.FunSuite.assertResult(FunSuite.scala:1555)|n   at fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest$$anonfun$2.apply$mcV$sp(FitNesseTestRunConfigurationProducerTest.scala:28)|n      at fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest$$anonfun$2.apply(FitNesseTestRunConfigurationProducerTest.scala:23)|n  at fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest$$anonfun$2.apply(FitNesseTestRunConfigurationProducerTest.scala:23)|n  at org.scalatest.Transformer$$anonfun$apply$1.apply$mcV$sp(Transformer.scala:22)|n      at org.scalatest.OutcomeOf$class.outcomeOf(OutcomeOf.scala:85)|n        at org.scalatest.OutcomeOf$.outcomeOf(OutcomeOf.scala:104)|n at org.scalatest.Transformer.apply(Transformer.scala:22)|n      at org.scalatest.Transformer.apply(Transformer.scala:20)|n      at org.scalatest.FunSuiteLike$$anon$1.apply(FunSuiteLike.scala:166)|n        at org.scalatest.Suite$class.withFixture(Suite.scala:1122)|n    at org.scalatest.FunSuite.withFixture(FunSuite.scala:1555)|n    at org.scalatest.FunSuiteLike$class.invokeWithFixture$1(FunSuiteLike.scala:163)|n    at org.scalatest.FunSuiteLike$$anonfun$runTest$1.apply(FunSuiteLike.scala:175)|n        at org.scalatest.FunSuiteLike$$anonfun$runTest$1.apply(FunSuiteLike.scala:175)|n        at org.scalatest.SuperEngine.runTestImpl(Engine.scala:306)|n at org.scalatest.FunSuiteLike$class.runTest(FunSuiteLike.scala:175)|n   at org.scalatest.FunSuite.runTest(FunSuite.scala:1555)|n        at org.scalatest.FunSuiteLike$$anonfun$runTests$1.apply(FunSuiteLike.scala:208)|n    at org.scalatest.FunSuiteLike$$anonfun$runTests$1.apply(FunSuiteLike.scala:208)|n       at org.scalatest.SuperEngine$$anonfun$traverseSubNodes$1$1.apply(Engine.scala:413)|n    at org.scalatest.SuperEngine$$anonfun$traverseSubNodes$1$1.apply(Engine.scala:401)|n at scala.collection.immutable.List.foreach(List.scala:381)|n    at org.scalatest.SuperEngine.traverseSubNodes$1(Engine.scala:401)|n     at org.scalatest.SuperEngine.org$scalatest$SuperEngine$$runTestsInBranch(Engine.scala:396)|n at org.scalatest.SuperEngine.runTestsImpl(Engine.scala:483)|n   at org.scalatest.FunSuiteLike$class.runTests(FunSuiteLike.scala:208)|n  at org.scalatest.FunSuite.runTests(FunSuite.scala:1555)|n    at org.scalatest.Suite$class.run(Suite.scala:1424)|n    at org.scalatest.FunSuite.org$scalatest$FunSuiteLike$$super$run(FunSuite.scala:1555)|n  at org.scalatest.FunSuiteLike$$anonfun$run$1.apply(FunSuiteLike.scala:212)|n at org.scalatest.FunSuiteLike$$anonfun$run$1.apply(FunSuiteLike.scala:212)|n    at org.scalatest.SuperEngine.runImpl(Engine.scala:545)|n        at org.scalatest.FunSuiteLike$class.run(FunSuiteLike.scala:212)|n    at fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest.org$scalatest$BeforeAndAfterAll$$super$run(FitNesseTestRunConfigurationProducerTest.scala:7)|n    at org.scalatest.BeforeAndAfterAll$class.liftedTree1$1(BeforeAndAfterAll.scala:257)|n        at org.scalatest.BeforeAndAfterAll$class.run(BeforeAndAfterAll.scala:256)|n     at fitnesse.idea.run.FitNesseTestRunConfigurationProducerTest.run(FitNesseTestRunConfigurationProducerTest.scala:7)|n        at org.scalatest.tools.SuiteRunner.run(SuiteRunner.scala:55)|n  at org.scalatest.tools.Runner$$anonfun$doRunRunRunDaDoRunRun$3.apply(Runner.scala:2563)|n   at org.scalatest.tools.Runner$$anonfun$doRunRunRunDaDoRunRun$3.apply(Runner.scala:2557)|n        at scala.collection.immutable.List.foreach(List.scala:381)|n    at org.scalatest.tools.Runner$.doRunRunRunDaDoRunRun(Runner.scala:2557)|n    at org.scalatest.tools.Runner$$anonfun$runOptionallyWithPassFailReporter$2.apply(Runner.scala:1044)|n   at org.scalatest.tools.Runner$$anonfun$runOptionallyWithPassFailReporter$2.apply(Runner.scala:1043)|nat org.scalatest.tools.Runner$.withClassLoaderAndDispatchReporter(Runner.scala:2722)|n  at org.scalatest.tools.Runner$.runOptionallyWithPassFailReporter(Runner.scala:1043)|n   at org.scalatest.tools.Runner$.run(Runner.scala:883)|n       at org.scalatest.tools.Runner.run(Runner.scala)|n       at org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestRunner.runScalaTest2(ScalaTestRunner.java:138)|n       at org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestRunner.main(ScalaTestRunner.java:28)|n      at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)|n        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)|n   at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)|n      at java.lang.reflect.Method.invoke(Method.java:606)|n   at com.intellij.rt.execution.application.AppMain.main(AppMain.java:134)|n' error = 'true'timestamp='2015-06-26T15:45:13.817'nodeId='3']
 *
 */
public class IntelliJFormatter implements Formatter, TestsRunnerListener {

//    private final StringBuilder outputChunks = new StringBuilder();

    private final PrintStream out;

//    private ExecutionResult executionResult;
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
    public void testStarted(WikiTestPage testPage) throws IOException {
        log("##teamcity[testStarted name='%s' locationHint='' captureStandardOutput='true']", testPage.getFullPath());
    }

    @Override
    public void testOutputChunk(String output) throws IOException {
//        outputChunks.append(output);
//        out.write("\033[32;41mERR\033[0m More text".getBytes());
        try {
            NodeList nodes = new Parser(new Lexer(output)).parse(null);
            translate(nodes);
        } catch (ParserException e) {
            log("Unparsable wiki output: %s", output);
        }
    }

    private void translate(NodeList nodes) throws IOException {
        if (nodes == null) return;
        for (Node node : nodeListIterator(nodes)) {
            if (node instanceof TableTag) {
                translateTable(node.getChildren());
            } else if (node instanceof Span) {
                Span span = (Span) node;
                String result = span.getAttribute("class");
                if ("pass".equals(result)) {
                    print("\u001B[42m");
                } else if ("fail".equals(result)) {
                    print("\u001B[41m");
                } else if ("error".equals(result)) {
                    print("\u001B[43m");
                } else if ("ignore".equals(result)) {
                    print("\u001B[46m");
                }
                print(span.getChildrenHTML());
                print("\u001B[0m ");
            } else if (node instanceof Tag) {
                Tag tag = (Tag) node;
                if ("BR".equals(tag.getTagName())) {
                    print("\n");
                } else {
                    print(node.toHtml());
                }
            } else if (node.getChildren() != null) {
                translate(node.getChildren());
            } else {
                print(node.toHtml());
            }
        }
    }

    private void translateTable(NodeList nodes) throws IOException {
        for (Node row : nodeListIterator(nodes.extractAllNodesThatMatch(new NodeClassFilter(TableRow.class)))) {
            for (Node cell : nodeListIterator(row.getChildren().extractAllNodesThatMatch(new NodeClassFilter(TableColumn.class)))) {
                print(" | ");
                translate(cell.getChildren());
            }
            print(" |\n");
        }
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
    public void testComplete(WikiTestPage testPage, TestSummary summary) throws IOException {
        if (exceptionOccurred != null) {
            log("##teamcity[testFailed name='%s' message='%s' error='true']", testPage.getFullPath(), exceptionOccurred.getMessage().replace("'", "|'"));
            exceptionOccurred = null;
        } else if (summary.getWrong() > 0 || summary.getExceptions() > 0) {
            log("##teamcity[testFailed name='%s' message='message' error='true']", testPage.getFullPath());
        } else {
            log("##teamcity[testFinished name='%s']", testPage.getFullPath());
        }
    }

    @Override
    public void testAssertionVerified(Assertion assertion, TestResult testResult) {
//        log("%s %s", assertion.getInstruction(), assertion.getExpectation());
//        if (testResult.getExecutionResult() == ExecutionResult.FAIL ||
//                testResult.getExecutionResult() == ExecutionResult.ERROR) {
//            executionResult = testResult.getExecutionResult();
//        }
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
}
