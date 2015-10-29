package fitnesse.idea.etc

import java.util.ResourceBundle

import com.intellij.CommonBundle
import com.intellij.reference.SoftReference
import org.jetbrains.annotations.PropertyKey

object FitnesseBundle {

  final val BUNDLE: String = "fitnesse.idea.etc.FitnesseBundle"
  private var ourBundle: java.lang.ref.Reference[ResourceBundle] = null

  def message(@PropertyKey(resourceBundle = "fitnesse.idea.etc.FitnesseBundle") key: String): String = {
    CommonBundle.message(getBundle, key, Array():_*)
  }

  def message(@PropertyKey(resourceBundle = "fitnesse.idea.etc.FitnesseBundle") key: String, params: AnyRef*): String = {
    CommonBundle.message(getBundle, key, params:_*)
  }

  private def getBundle: ResourceBundle = {
    Option(SoftReference.dereference(ourBundle)) match {
      case Some(bundle) => bundle
      case None =>
        val bundle = ResourceBundle.getBundle(BUNDLE)
        ourBundle = new SoftReference[ResourceBundle](bundle)
        bundle
    }
  }
}
