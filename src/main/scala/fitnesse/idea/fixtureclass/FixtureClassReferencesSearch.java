package fitnesse.idea.fixtureclass;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import fitnesse.idea.fixtureclass.FixtureClassReference;
import fitnesse.idea.lang.filetype.FitnesseFileType;
import fitnesse.idea.fixtureclass.FixtureClass;
import fitnesse.idea.fixtureclass.FixtureClassIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FixtureClassReferencesSearch extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {

    public FixtureClassReferencesSearch() {
        super(true);
    }

    @Override
    public void processQuery(@NotNull final ReferencesSearch.SearchParameters p, @NotNull final Processor<PsiReference> consumer) {
        final PsiElement myElement = p.getElementToSearch();
        if (!(myElement instanceof PsiClass)) {
            return;
        }
        final PsiClass clazz = (PsiClass)myElement;
        System.out.println("Class to search for " + clazz.getName());

        SearchScope scope = p.getEffectiveSearchScope();
        if (scope instanceof GlobalSearchScope) {
            GlobalSearchScope restrictedScope = GlobalSearchScope.getScopeRestrictedByFileTypes((GlobalSearchScope) scope, FitnesseFileType.INSTANCE());
            for (FixtureClass fixtureClass : find(clazz.getName(), clazz.getProject(), restrictedScope)) {
                System.out.println("Providing reference from " + clazz + " to " + fixtureClass.fixtureClassName());
                consumer.process(new FixtureClassReference(clazz, fixtureClass));
            }
        }
    }

    public Collection<FixtureClass> find(String key, Project project, GlobalSearchScope scope) {
        return FixtureClassIndex.INSTANCE().get(key, project, scope);
    }

}