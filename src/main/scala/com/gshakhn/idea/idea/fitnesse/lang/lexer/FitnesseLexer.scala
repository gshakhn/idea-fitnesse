package com.gshakhn.idea.idea.fitnesse.lang.lexer

import com.intellij.lexer.FlexAdapter
import java.io.Reader

class FitnesseLexer extends FlexAdapter(new _FitnesseLexer(null.asInstanceOf[Reader])) {}
