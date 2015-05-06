package fitnesse.idea.run

import java.awt.Color
import javax.swing.Icon

import com.intellij.openapi.actionSystem.CustomShortcutSet
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler
import fitnesse.idea.lang.filetype.FitnesseFileType
import org.jetbrains.jps.model.JpsDummyElement

class FitNesseRootEditHandler extends ModuleSourceRootEditHandler[JpsDummyElement](FitNesseRootType.INSTANCE) {
  override def getRootsGroupColor = new Color(0x10B0ED)

  override def getRootsGroupTitle = "FitNesse Folders"

  override def getRootTypeName = "FitNesse"

  override def getRootIcon = FitnesseFileType.FILE_ICON

  override def getUnmarkRootButtonText = "Unmark FitNesse Root"

  override def getMarkRootShortcutSet = null

  override def getFolderUnderRootIcon: Icon = null
}
