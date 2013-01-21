package com.gshakhn.idea.idea.fitnesse.lang.psi

import org.scalatest.FunSpec

class NameUtilsSuite extends FunSpec {

  describe("toJavaClassName") {
    it("should generate a class name for a capital word") {
      expectResult("Hello") {
        NameUtils.toJavaClassName("Hello")
      }
    }

    it("should generate a class name for a capital word followed by a space") {
      expectResult("Hello") {
        NameUtils.toJavaClassName("Hello ")
      }
    }

    it("should generate a class name for a capital word preceded by a space") {
      expectResult("Hello") {
        NameUtils.toJavaClassName(" Hello")
      }
    }

    it("should generate a class name for a lower case word") {
      expectResult("Hello") {
        NameUtils.toJavaClassName("hello")
      }
    }

    it("should generate a class name for a capital word followed by a period") {
      expectResult("Hello") {
        NameUtils.toJavaClassName("Hello.")
      }
    }

    it("should generate a class name for 2 camel cased words") {
      expectResult("HelloWorld") {
        NameUtils.toJavaClassName("HelloWorld")
      }
    }

    it("should generate a class name for 2 camel cased words followed by a period") {
      expectResult("HelloWorld") {
        NameUtils.toJavaClassName("HelloWorld.")
      }
    }

    it("should generate a class name for 2 capital words separated by spaces") {
      expectResult("HelloWorld") {
        NameUtils.toJavaClassName("Hello World")
      }
    }

    it("should generate a class name for 2 capital words separated by spaces followed by a period") {
      expectResult("HelloWorld") {
        NameUtils.toJavaClassName("Hello World.")
      }
    }

    it("should generate a class name for 2 lower case words separated by spaces") {
      expectResult("HelloWorld") {
        NameUtils.toJavaClassName("hello world")
      }
    }

    it("should generate a class name for 2 lower case words separated by spaces followed by a period") {
      expectResult("HelloWorld") {
        NameUtils.toJavaClassName("hello world.")
      }
    }
  }
}
