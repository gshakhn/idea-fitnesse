package fitnesse.idea.lang.lexer

import java.io.Reader

import com.intellij.lexer.FlexAdapter

class FitnesseLexer extends FlexAdapter(new _FitnesseLexer(null.asInstanceOf[Reader])) {}
