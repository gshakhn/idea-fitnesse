package fitnesse.idea.highlighting

import com.intellij.psi.impl.source.tree.LeafPsiElement
import fitnesse.idea.parser.FitnesseElementType
import org.scalatest.FunSuite

class FitnesseFoldingBuilderTest extends FunSuite {

  def placeholderText(text: String): String = {
    new FitnesseFoldingBuilder().getPlaceholderText(new LeafPsiElement(FitnesseElementType.COLLAPSIBLE, text))
  }

  test("No content, no folding") {
    assertResult(null) {
      placeholderText("")
    }
  }


  test("No folding with one line") {
    assertResult(null) {
      placeholderText("foo")
    }
  }

  test("Simple folding with two lines") {
    assertResult("!* ... *!") {
      placeholderText("!*\n*!")
    }

    assertResult("!* ... *!") {
      placeholderText("!*    \n     *!")
    }
  }

  test("Simple folding with three lines") {
    assertResult("!* ... *!") {
      placeholderText("!*\nfoo\n*!")
    }

    assertResult("!* foo ... *!") {
      placeholderText("!* foo\nbar\n*!")
    }

    assertResult("!* ... *!") {
      placeholderText("!*    \nfoo\n    *!")
    }

  }

  test("Simple folding with four lines") {
    assertResult("!* title ... *!") {
      placeholderText("!* title\nfoo\nbar\n*!")
    }

  }

}
