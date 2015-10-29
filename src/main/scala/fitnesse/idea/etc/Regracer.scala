package fitnesse.idea.etc

object Regracer {

  /**
   * Regrace a class or method name (lower case with spaces).
   *
   * @param s text to regrace (e.g. "fooBar")
   * @return regraced version of s (e.g. "foo bar")
   */
  def regrace(s: String): String = s.replaceAll("(?<=.)(\\p{Upper})", " $1").toLowerCase

}
