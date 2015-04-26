package com.gshakhn.idea.idea.fitnesse.scripttable

object ScriptKeywordProvider {

  def KEYWORDS = Array("script", "start", "check", "check not", "reject", "ensure", "show", "note")

  def isKeyword(s: String): Boolean = KEYWORDS.contains(s)

  def isCheckKeyword(s: String): Boolean = s == "check" || s == "check not"
}
