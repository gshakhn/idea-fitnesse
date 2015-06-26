package fitnesse.idea.lang.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import fitnesse.wikitext.parser._
import scala.collection.JavaConversions._

class FitnesseLexer extends LexerBase {

  var buffer: CharSequence = null
  var startOffset: Int = 0
  var endOffset: Int = 0
  var state = 0

  var specification: ParseSpecification = null
  var scanner: Scanner = null
  var parser: Parser = null

  var symbolList: List[Symbol] = Nil

  override def start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int): Unit = {
    this.buffer = buffer
    this.startOffset = startOffset
    this.endOffset = endOffset
    this.state = initialState
    
    val input: CharSequence = buffer.subSequence(startOffset, endOffset)
    val currentPage: ParsingPage = new LexerParsingPage

    specification = new ParseSpecification().provider(SymbolProvider.wikiParsingProvider)
    scanner = new Scanner(new TextMaker(currentPage, currentPage.getNamedPage), input)
    parser = new Parser(null, currentPage, scanner, specification)
    symbolList = Nil

    advance()
  }

  override def advance(): Unit = {
    symbolList = fetchNextSymbol()

    symbolList.headOption match {
      case Some(symbol) if !symbol.hasOffset => advance()
      case _ =>
    }
  }
  
  private def fetchNextSymbol(): List[Symbol] = {
    state += 1
    symbolList match {
      case symbol :: tail if shouldTraverse(symbol) =>
        symbol.getChildren.toList ::: (FitnesseLexer.terminatorFor(symbol) match {
          case Some(s) => s :: tail
          case None => tail
        })
      case Nil | _ :: Nil =>
        specification.parseSymbol(parser, scanner) match {
          case parsedSymbol if parsedSymbol.isNothing =>
            Nil
          case parsedSymbol =>
            parsedSymbol.getValue :: Nil
        }
      case _ :: tail =>
        tail
    }
  }

  override def getTokenStart: Int = symbolList.head.getStartOffset

  override def getTokenEnd: Int = symbolList.head.getEndOffset

  override def getTokenType: IElementType = {
    symbolList match {
      case Nil => null
      case symbol :: _ => symbol.getType match {
        case _: WikiWord => FitnesseTokenType.WIKI_WORD
        case _: Collapsible => FitnesseTokenType.COLLAPSABLE_BLOCK
        case SymbolType.Bold => FitnesseTokenType.BOLD
        case SymbolType.Italic => FitnesseTokenType.ITALIC
        case SymbolType.Whitespace => FitnesseTokenType.WHITE_SPACE
        case SymbolType.Newline => FitnesseTokenType.LINE_TERMINATOR
        case _: ColoredSlimTable => FitnesseTokenType.TABLE_START
        case FitnesseLexer.TABLE_END => FitnesseTokenType.TABLE_END
        case Table.tableRow => FitnesseTokenType.ROW_START
        case FitnesseLexer.TABLE_ROW_END => FitnesseTokenType.ROW_END
        case Table.tableCell => FitnesseTokenType.CELL_START
        case FitnesseLexer.TABLE_CELL_END => FitnesseTokenType.CELL_END
        case SymbolType.Colon => FitnesseTokenType.COLON
        case _ => FitnesseTokenType.WORD
      }
    }
  }

  override def getState: Int = {
    state //throw new IllegalStateException("FitnesseLexer does not have state that can be represented in an integer")
  }

  override def getBufferEnd: Int = buffer.length

  override def getBufferSequence: CharSequence = buffer

  private def shouldTraverse(symbol: Symbol): Boolean = {
    return symbol != null && ((symbol.getType.isInstanceOf[ColoredSlimTable]) ||
              (Table.tableRow eq symbol.getType) ||
              (Table.tableCell eq symbol.getType))
  }

}

object FitnesseLexer {
  final val TABLE_END = _symbolType("TableEnd")
  final val TABLE_ROW_END = _symbolType("TableRowEnd")
  final val TABLE_CELL_END = _symbolType("TableCellEnd")
  final val END = _symbolType("end")

  def terminatorFor(symbol: Symbol): Option[Symbol] = symbol.getType match {
    case _ : ColoredSlimTable => Some(new Symbol(TABLE_END, "", lastChild(symbol).getEndOffset, symbol.getEndOffset))
    case s if s eq Table.tableRow => Some(new Symbol(TABLE_ROW_END, "", lastChild(symbol).getEndOffset, symbol.getEndOffset))
    case s if s eq Table.tableCell => Some(new Symbol(TABLE_CELL_END, "", lastChild(symbol).getEndOffset, symbol.getEndOffset))
    case _ => None
  }

  private def lastChild(symbol: Symbol): Symbol = {
    if (symbol.getType == Variable.symbolType) {
      // Shortcut variables, since it wants to replace the content completely
      symbol
    } else if (symbol.getChildren.isEmpty) {
      symbol
    } else {
      lastChild(symbol.getChildren.last)
    }
  }
  private def _symbolType(s: String) = {
    new SymbolType(s)
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
    List.empty[SourcePage]
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
