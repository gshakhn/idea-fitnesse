package fitnesse.idea.run;

import com.intellij.execution.ui.CommonJavaParametersPanel;
import com.intellij.execution.ui.ConfigurationModuleSelector;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.util.ui.UIUtil;
import fitnesse.idea.FitnesseBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

// This is written in Java because...? Field magic happening under the hood?
public class FitnesseApplicationConfigurable extends SettingsEditor<FitnesseRunConfiguration> implements PanelWithAnchor {
  private JComponent myAnchor;
  private final ConfigurationModuleSelector myModuleSelector;

  // These fields are injected from the form:
  private JPanel myWholePanel;
  private LabeledComponent<JComboBox> myModule;
  private LabeledComponent<RawCommandLineEditor> myFitnesseRoot;
  private LabeledComponent<RawCommandLineEditor> myWikiPageName;
  private CommonJavaParametersPanel myCommonProgramParameters;

  public FitnesseApplicationConfigurable(Project project) {
    myModuleSelector = new ConfigurationModuleSelector(project, myModule.getComponent());

    myAnchor = UIUtil.mergeComponentsWithAnchor(myFitnesseRoot, myWikiPageName, myModule, myCommonProgramParameters);

    myWikiPageName.getComponent().setDialogCaption(FitnesseBundle.message("run.configuration.form.wikiPage.title"));
    myFitnesseRoot.getComponent().setDialogCaption(FitnesseBundle.message("run.configuration.form.fitnesseRoot.title"));
  }

  @Override
  public JComponent getAnchor() {
    return myAnchor;
  }

  @Override
  public void setAnchor(@Nullable JComponent anchor) {
    myAnchor = anchor;
    myFitnesseRoot.setAnchor(anchor);
    myWikiPageName.setAnchor(anchor);
    myModule.setAnchor(anchor);
    myCommonProgramParameters.setAnchor(anchor);
  }

  @Override
  protected void resetEditorFrom(FitnesseRunConfiguration configuration) {
    myModuleSelector.reset(configuration);
    myCommonProgramParameters.reset(configuration);
    myFitnesseRoot.getComponent().setText(configuration.getFitnesseRoot());
    myWikiPageName.getComponent().setText(configuration.getWikiPageName());
  }

  @Override
  protected void applyEditorTo(FitnesseRunConfiguration configuration) throws ConfigurationException {
    myCommonProgramParameters.applyTo(configuration);
    myModuleSelector.applyTo(configuration);

    configuration.setFitnesseRoot(myFitnesseRoot.getComponent().getText());
    configuration.setWikiPageName(myWikiPageName.getComponent().getText());
    Module selectedModule = (Module)myModule.getComponent().getSelectedItem();
    configuration.setModule(selectedModule);
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return myWholePanel;
  }
}
