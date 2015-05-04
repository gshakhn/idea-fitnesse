package fitnesse.idea.run

import com.intellij.ide.projectView.actions.MarkSourceRootAction
import org.jetbrains.jps.model.ex.JpsElementTypeWithDummyProperties
import org.jetbrains.jps.model.module.JpsModuleSourceRootType
import org.jetbrains.jps.model.{JpsDummyElement, JpsElementFactory}

class MarkFitNesseRootAction extends MarkSourceRootAction(FitNesseRootType.INSTANCE) {

}

class FitNesseRootType extends JpsElementTypeWithDummyProperties with JpsModuleSourceRootType[JpsDummyElement] {

  override def createDefaultProperties(): JpsDummyElement = JpsElementFactory.getInstance.createDummyElement()
}

object FitNesseRootType {
  val INSTANCE = new FitNesseRootType
}
