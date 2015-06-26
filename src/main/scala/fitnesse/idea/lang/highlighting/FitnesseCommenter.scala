package fitnesse.idea.lang.highlighting

import com.intellij.lang.Commenter

class FitnesseCommenter extends Commenter {

  override def getCommentedBlockCommentPrefix: String = null

  override def getBlockCommentSuffix: String = null

  override def getBlockCommentPrefix: String = ""

  override def getLineCommentPrefix: String = "#"

  override def getCommentedBlockCommentSuffix: String = null
}
