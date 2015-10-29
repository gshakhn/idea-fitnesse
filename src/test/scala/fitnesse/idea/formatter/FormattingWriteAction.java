package fitnesse.idea.formatter;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;

// Need to bridge some permission trouble in Scala
public class FormattingWriteAction extends WriteCommandAction.Simple {

    private final JavaCodeInsightTestFixture myFixture;

    public FormattingWriteAction(Project project, JavaCodeInsightTestFixture myFixture) {
        super(project);
        this.myFixture = myFixture;
    }

    @Override
    protected void run() throws Throwable {
        CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile());
    }
}
