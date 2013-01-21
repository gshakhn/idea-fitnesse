package com.gshakhn.idea.idea.fitnesse.lang.psi

object NameUtils {
  def toJavaClassName(gracefulName: String): String = {
    gracefulName.trim
      .replace(".", "")
      .split(' ')
      .map(s => s.head.toUpper + s.tail)
      .reduce((acc, s) => acc + s)
  }
}
