package fitnesse.idea.fixturemethod

object ReturnType extends Enumeration {
  type ReturnType = Value

  val Void = Value("Void")
  val Boolean = Value("Boolean")
  val String = Value("String")
  val List = Value("List")
  val Object = Value("Object")
}
