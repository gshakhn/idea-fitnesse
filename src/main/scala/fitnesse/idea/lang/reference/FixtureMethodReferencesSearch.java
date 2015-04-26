package fitnesse.idea.lang.reference;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

public class FixtureMethodReferencesSearch extends QueryExecutorBase<PsiReference, MethodReferencesSearch.SearchParameters> {
    public FixtureMethodReferencesSearch() {
        super(true);
    }

    @Override
    public void processQuery(@NotNull final MethodReferencesSearch.SearchParameters p, @NotNull final Processor<PsiReference> consumer) {
        final PsiMethod method = p.getMethod();

//        final PsiAnnotation stepAnnotation = CucumberJavaUtil.getCucumberStepAnnotation(method);
//        final String regexp = stepAnnotation != null ? CucumberJavaUtil.getPatternFromStepDefinition(stepAnnotation) : null;
//        if (regexp == null) {
//            return;
//        }
//
//        final String word = CucumberUtil.getTheBiggestWordToSearchByIndex(regexp);
//        if (StringUtil.isEmpty(word)) {
//            return;
//        }

        System.out.println("Search references for " + method);

//        SearchScope scope = p.getEffectiveSearchScope();
//        if (scope instanceof GlobalSearchScope) {
//            GlobalSearchScope restrictedScope = GlobalSearchScope.getScopeRestrictedByFileTypes((GlobalSearchScope) scope, FitnesseFileType.INSTANCE());
//            ReferencesSearch.search(new ReferencesSearch.SearchParameters(method, restrictedScope, false, p.getOptimizer())).forEach(consumer);
//        }
    }
}