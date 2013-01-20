package com.gshakhn.idea.idea.fitnesse.lang.filetype

import com.intellij.openapi.fileTypes.{ExactFileNameMatcher, FileTypeConsumer, FileTypeFactory}

class FitnesseFileTypeFactory extends FileTypeFactory {
  def createFileTypes(consumer: FileTypeConsumer) {
    consumer.consume(FitnesseFileType.INSTANCE, new ExactFileNameMatcher(FitnesseFileType.FILE_NAME))
  }
}
