package fitnesse.idea.filetype

import com.intellij.lang.Language

class FitnesseLanguage extends Language("Fitnesse")

object FitnesseLanguage {
  final val INSTANCE = new FitnesseLanguage
}
