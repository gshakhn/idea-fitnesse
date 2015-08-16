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
    state += 1
    symbolList = fetchNextSymbol()

    symbolList.headOption match {
      case Some(symbol) if !symbol.hasOffset => advance()
      case Some(symbol) if symbol.getType eq SymbolType.SymbolList => advance()
      case _ =>
    }
  }
  
  private def fetchNextSymbol(): List[Symbol] = {
    def parseSymbol: List[Symbol] = {
      specification.parseSymbol(parser, scanner) match {
        case parsedSymbol if parsedSymbol.isNothing =>
          Nil
        case parsedSymbol =>
          parsedSymbol.getValue :: Nil
      }
    }

    symbolList match {
      case symbol :: tail if symbol.getType eq SymbolType.SymbolList =>
        symbol.getChildren.toList ::: tail
      case symbol :: tail =>
        FitnesseLexer.terminatorFor(symbol) match {
          case Some(endSymbol) => symbol.getChildren.toList ::: endSymbol :: tail
          case None =>
            tail match {
              case Nil =>
                parseSymbol
              case _ =>
                tail
            }
        }
      case Nil =>
        parseSymbol
    }
  }

  override def getTokenType: IElementType = {
    symbolList match {
      case Nil => null
      case symbol :: _ => symbol.getType match {
        case FitnesseLexer.LexerSymbolType(elementType) => elementType
        case _: WikiWord => FitnesseTokenType.WIKI_WORD
        case _: Collapsible => FitnesseTokenType.COLLAPSIBLE_START
        case SymbolType.Bold => FitnesseTokenType.BOLD
        case SymbolType.Italic => FitnesseTokenType.ITALIC
        case SymbolType.Whitespace => FitnesseTokenType.WHITE_SPACE
        case SymbolType.Newline => FitnesseTokenType.LINE_TERMINATOR
        case _: ColoredSlimTable => FitnesseTokenType.TABLE_START
        case FitnesseLexer.TABLE_END => FitnesseTokenType.TABLE_END
        case Table.tableRow => FitnesseTokenType.ROW_START
        case Table.tableCell => FitnesseTokenType.CELL_START
        case FitnesseLexer.TABLE_CELL_END => FitnesseTokenType.CELL_END
        case SymbolType.Colon => FitnesseTokenType.COLON
        case _ => FitnesseTokenType.WORD
      }
    }
  }

  override def getTokenStart: Int = symbolList match {
    case symbol :: _ => symbol.getStartOffset
    // case Nil is covered by getTokenType()
  }

  override def getTokenEnd: Int = symbolList match {
    case symbol :: _ => symbol.getEndOffset
    // case Nil is covered by getTokenType()
  }

  override def getState: Int = {
    state
  }

  override def getBufferEnd: Int = buffer.length

  override def getBufferSequence: CharSequence = buffer
}

object FitnesseLexer {
  final val TABLE_END = LexerSymbolType(FitnesseTokenType.TABLE_END)
  final val TABLE_ROW_END = LexerSymbolType(FitnesseTokenType.ROW_END)
  final val TABLE_CELL_END = LexerSymbolType(FitnesseTokenType.CELL_END)
  final val COLLAPSIBLE_END = LexerSymbolType(FitnesseTokenType.COLLAPSIBLE_END)

  case class LexerSymbolType(elementType: IElementType) extends SymbolType(elementType.toString)

  def terminatorFor(symbol: Symbol): Option[Symbol] = symbol.getType match {
    case _ : ColoredSlimTable => Some(new Symbol(TABLE_END, "", lastChild(symbol).getEndOffset, symbol.getEndOffset))
    case _ : Collapsible => Some(new Symbol(COLLAPSIBLE_END, "", lastChild(symbol).getEndOffset, symbol.getEndOffset))
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
}

class LexerParsingPage extends ParsingPage(new LexerSourcePage) {

  override def copyForNamedPage(namedPage: SourcePage): ParsingPage = {
    throw new IllegalStateException("FitNesse plugin: method LexerParsingPage.copyForNamedPage() has not been implemented")
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
    "NAME"
  }

  override def getFullName: String = {
    "FULL_NAME"
  }

  override def getPath: String = {
    // TODO: We need to know where the FitNesseRoot is, so we can determine a page path (wiki page name)
    "PATH_PLACEHOLDER"
     //page.getPageCrawler.getFullPath.parentPath.toString
  }

  override def getFullPath: String = {
    // page.getPageCrawler.getFullPath.toString
    throw new IllegalStateException("FitNesse plugin: method LexerParsingPage.getFullPath() has not been implemented")
  }

  override def getContent: String = {
    throw new IllegalStateException("FitNesse plugin: method LexerParsingPage.getContent() has not been implemented")
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
    throw new IllegalStateException("FitNesse plugin: method LexerParsingPage.getProperty() has not been implemented")
  }

  override def makeUrl(wikiWordPath: String): String = {
    throw new IllegalStateException("FitNesse plugin: method LexerParsingPage.makeUrl() has not been implemented")
  }

  override def compareTo(o: SourcePage): Int = {
    throw new IllegalStateException("FitNesse plugin: method LexerParsingPage.compareTo() has not been implemented")
  }
}
