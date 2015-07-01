package fitnesse.idea

import java.util.ResourceBundle

import com.intellij.CommonBundle
import org.jetbrains.annotations.PropertyKey

object FitnesseBundle {

  final val BUNDLE: String = "fitnesse.idea.FitnesseBundle"
  private var ourBundle: java.lang.ref.Reference[ResourceBundle] = null

  def message(@PropertyKey(resourceBundle = "fitnesse.idea.FitnesseBundle") key: String): String = {
    CommonBundle.message(getBundle, key, Array():_*)
  }

  def message(@PropertyKey(resourceBundle = "fitnesse.idea.FitnesseBundle") key: String, params: AnyRef*): String = {
    CommonBundle.message(getBundle, key, params:_*)
  }

  private def getBundle: ResourceBundle = {
    var bundle: ResourceBundle = com.intellij.reference.SoftReference.dereference(ourBundle)
    if (bundle == null) {
      bundle = ResourceBundle.getBundle(BUNDLE)
      ourBundle = new com.intellij.reference.SoftReference[ResourceBundle](bundle)
    }
    bundle
  }
}
