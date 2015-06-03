package fitnesse.idea.lang.reference;

import fitnesse.idea.lang.filetype.FitnesseFileType;
import fitnesse.idea.lang.psi.FitnesseFile;
import fitnesse.idea.lang.psi.FixtureClass;
import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
        System.out.println("Found a class to search for " + clazz.getName());

//        SearchScope scope = p.getEffectiveSearchScope();
//        if (scope instanceof GlobalSearchScope) {
//            GlobalSearchScope restrictedScope = GlobalSearchScope.getScopeRestrictedByFileTypes((GlobalSearchScope) scope, FitnesseFileType.INSTANCE());
            for (FixtureClass fixtureClass : find(clazz.getProject(), clazz.getName())) {
                System.out.println("Providing reference from " + clazz + " to " + fixtureClass);
                consumer.process(new FixtureClassReference(clazz, fixtureClass));
            }
//            ReferencesSearch.search(new ReferencesSearch.SearchParameters(clazz, restrictedScope, false, p.getOptimizer())).forEach(consumer);
//        }
    }

    public List<FixtureClass> find(Project project, String key) {
        List<FixtureClass> result = null;
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, FitnesseFileType.INSTANCE(),
                GlobalSearchScope.allScope(project));
        PsiManager psiManager = PsiManager.getInstance(project);
        for (VirtualFile virtualFile : virtualFiles) {
            FitnesseFile fitnesseFile = (FitnesseFile) psiManager.findFile(virtualFile);
            if (fitnesseFile != null) {
                FixtureClass[] fixtureClasses = PsiTreeUtil.getChildrenOfType(fitnesseFile, FixtureClass.class);
                System.out.println("Found fixture classes for " + fitnesseFile + ": " + fixtureClasses);
                if (fixtureClasses != null) {
                    for (FixtureClass fixtureClass : fixtureClasses) {
                        if (key.equals(fixtureClass.fixtureClassName())) {
                            if (result == null) {
                                result = new ArrayList<FixtureClass>();
                            }
                            result.add(fixtureClass);
                        }
                    }
                }
            }
        }
        return result != null ? result : Collections.<FixtureClass>emptyList();
    }

}