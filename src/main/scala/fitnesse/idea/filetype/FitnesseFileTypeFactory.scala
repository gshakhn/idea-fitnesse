package fitnesse.idea.filetype

import com.intellij.openapi.fileTypes.{ExactFileNameMatcher, FileTypeConsumer, FileTypeFactory}

class FitnesseFileTypeFactory extends FileTypeFactory {
  def createFileTypes(consumer: FileTypeConsumer): Unit = {
    consumer.consume(FitnesseFileType.INSTANCE, new ExactFileNameMatcher(FitnesseFileType.FILE_NAME))
  }
}
