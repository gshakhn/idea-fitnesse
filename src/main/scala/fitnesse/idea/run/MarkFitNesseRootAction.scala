package fitnesse.idea.run

import java.awt.Color
import javax.swing.Icon

import com.intellij.ide.projectView.actions.MarkSourceRootAction
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler
import fitnesse.idea.lang.filetype.FitnesseFileType
import org.jetbrains.jps.model.JpsDummyElement
import org.jetbrains.jps.model.ex.JpsElementTypeWithDummyProperties
import org.jetbrains.jps.model.module.JpsModuleSourceRootType


/*
 * This code is not used for now, since we need to store the source root config in the module file.
 */

class MarkFitNesseRootAction extends MarkSourceRootAction(FitNesseRootType.INSTANCE) {

}

class FitNesseRootType extends JpsElementTypeWithDummyProperties with JpsModuleSourceRootType[JpsDummyElement] {
  override def createDefaultProperties(): JpsDummyElement = super.createDefaultProperties()
}

object FitNesseRootType {
  val INSTANCE = new FitNesseRootType
}


class FitNesseRootEditHandler extends ModuleSourceRootEditHandler[JpsDummyElement](FitNesseRootType.INSTANCE) {
  override def getRootsGroupColor = new Color(0x10B0ED)

  override def getRootsGroupTitle = "FitNesse Folders"

  override def getRootTypeName = "FitNesse"

  override def getRootIcon = FitnesseFileType.FILE_ICON

  override def getUnmarkRootButtonText = "Unmark FitNesse Root"

  override def getMarkRootShortcutSet = null

  override def getFolderUnderRootIcon: Icon = null
}
