package fitnesse.idea.filetype

import com.intellij.openapi.fileTypes.{ExactFileNameMatcher, FileTypeConsumer, FileTypeFactory}

class FitnesseFileTypeFactory extends FileTypeFactory {
  def createFileTypes(consumer: FileTypeConsumer): Unit = {
    consumer.consume(FitnesseFileType.INSTANCE, new ExactFileNameMatcher(FitnesseFileType.CONTENT_TXT_NAME))
    consumer.consume(FitnesseFileType.INSTANCE, FitnesseFileType.WIKI_FILE_EXTENSION_MATCHER)
  }
}
