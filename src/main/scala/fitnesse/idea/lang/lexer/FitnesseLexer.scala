package fitnesse.idea.lang.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import fitnesse.wikitext.parser._
import org.apache.commons.collections.IteratorUtils
import scala.collection.JavaConversions._

class FitnesseLexer extends LexerBase {
  var buffer: CharSequence = null
  var startOffset: Int = 0
  var endOffset: Int = 0
  var state: Int = 0

  var currentSymbol: Symbol = null
  var symbolIterator: Iterator[Symbol] = emptyIterator

  var specification: ParseSpecification = null
  var scanner: Scanner = null
  var parser: Parser = null

  override def start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int): Unit = {
    this.buffer = buffer
    this.startOffset = startOffset
    this.endOffset = endOffset
    this state = initialState

    Parser.make(new LexerParsingPage, buffer.subSequence(startOffset, endOffset)).parse

    val currentPage: ParsingPage = new LexerParsingPage
    val input: CharSequence = buffer.subSequence(startOffset, endOffset)

    specification = new ParseSpecification().provider(SymbolProvider.wikiParsingProvider)
    scanner = new Scanner(new TextMaker(currentPage, currentPage.getNamedPage), input)
    parser = new Parser(null, currentPage, scanner, specification)

    advance
  }

  override def advance(): Unit = {
    if (symbolIterator.hasNext) {
      currentSymbol = symbolIterator.next
      if (!currentSymbol.hasOffset) advance
    }
    else {
      val parsedSymbol: Maybe[Symbol] = specification.parseSymbol(parser, scanner)
      if (parsedSymbol.isNothing) {
        currentSymbol = null
      }
      else {
        currentSymbol = parsedSymbol.getValue
//        if (shouldTraverse(currentSymbol)) {
//          symbolIterator = new SymbolChildIterator(currentSymbol.getChildren)
//        }
//        else {
//          symbolIterator = emptyIterator
//        }
      }
    }
  }

  override def getTokenStart: Int = currentSymbol.getStartOffset

  override def getTokenEnd: Int = currentSymbol.getEndOffset

  override def getTokenType: IElementType = {
    if (currentSymbol == null) null else currentSymbol.getType match {
      case _: WikiWord => FitnesseTokenType.WIKI_WORD
      case SymbolType.Whitespace => FitnesseTokenType.WHITE_SPACE
      case SymbolType.Newline => FitnesseTokenType.LINE_TERMINATOR
      case _ => FitnesseTokenType.WORD
    }
  }

  override def getState: Int = {
    throw new IllegalStateException("FitnesseLexer does not have state that can be represented in an integer")
  }

  override def getBufferEnd: Int = buffer.length

  override def getBufferSequence: CharSequence = buffer

  private def shouldTraverse(symbol: Symbol): Boolean = {
    return ("Table" == symbol.getType.toString) || ("Collapsible" == symbol.getType.toString)
  }

  def emptyIterator: java.util.Iterator[Symbol] = {
    IteratorUtils.emptyIterator().asInstanceOf[java.util.Iterator[Symbol]]
  }
}

class LexerParsingPage extends ParsingPage(new LexerSourcePage) {

  override def copyForNamedPage(namedPage: SourcePage): ParsingPage = {
    throw new IllegalStateException("Should not have been called in this context")
  }

  override def putVariable(name: String, value: String) {
    super.putVariable(name, value)
  }

  override def findVariable(name: String): Maybe[String] = {
    super.findVariable(name)
  }
}

class LexerSourcePage extends SourcePage {
  override def getName: String = {
    null
  }

  override def getFullName: String = {
    null
  }

  override def getPath: String = {
    throw new IllegalStateException("Should not have been called in this context")
  }

  override def getFullPath: String = {
    throw new IllegalStateException("Should not have been called in this context")
  }

  override def getContent: String = {
    throw new IllegalStateException("Should not have been called in this context")
  }

  override def targetExists(wikiWordPath: String): Boolean = {
    false
  }

  override def makeFullPathOfTarget(wikiWordPath: String): String = {
    null
  }

  override def findParentPath(targetName: String): String = {
    null
  }

  override def findIncludedPage(pageName: String): Maybe[SourcePage] = {
    Maybe.nothingBecause("not in this context")
  }

  override def getChildren = {
    List[SourcePage]()
  }

  override def hasProperty(propertyKey: String): Boolean = {
    false
  }

  override def getProperty(propertyKey: String): String = {
    throw new IllegalStateException("Should not have been called in this context")
  }

  override def makeUrl(wikiWordPath: String): String = {
    throw new IllegalStateException("Should not have been called in this context")
  }

  override def compareTo(o: SourcePage): Int = {
    throw new IllegalStateException("Should not have been called in this context")
  }
}
