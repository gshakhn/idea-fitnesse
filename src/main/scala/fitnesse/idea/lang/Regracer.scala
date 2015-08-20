package fitnesse.idea.lang

object Regracer {

  /**
   * Regrace a class or method name (lower case with spaces).
   *
   * @param s
   * @return
   */
  def regrace(s: String) = s.replaceAll("(.)(\\p{Upper})", "$1 $2").toLowerCase

}
