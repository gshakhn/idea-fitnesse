package fitnesse.idea.lexer

import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType
import org.scalatest.{FunSuite, Matchers}

import scala.collection.mutable

trait LexerSuite extends FunSuite with Matchers {
  def lex(text: String, lexer: Lexer = new FitnesseLexer) = {
    val lexedTokens = new mutable.ListBuffer[Tuple2[IElementType, String]]()
    lexer.start(text)
    while (lexer.getTokenType != null) {
      val tokenType = lexer.getTokenType
      val tokenText = lexer.getBufferSequence.subSequence(lexer.getTokenStart, lexer.getTokenEnd).toString
      lexedTokens += new Tuple2(tokenType, tokenText)
      lexer.advance()
    }
    lexedTokens.toList
  }
}
