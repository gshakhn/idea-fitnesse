package fitnesse.idea.lang.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;

import java.util.List;

public class StubBasedPsiElementBase2<T extends StubElement> extends StubBasedPsiElementBase<T> {
    public StubBasedPsiElementBase2(T stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public StubBasedPsiElementBase2(ASTNode node) {
        super(node);
    }

    @Override
    public <T extends PsiElement> List<T> findChildrenByType(IElementType elementType) {
        return super.findChildrenByType(elementType);
    }
}
