package com.gshakhn.idea.idea.fitnesse.lang.psi

import org.scalatest.FunSpec

class FixtureClassSuite extends FunSpec {

  describe("getClassName") {
    it("should generate a class name for a capital word") {
      expect("Hello") {
        FixtureClass.getClassName("Hello")
      }
    }

    it("should generate a class name for a capital word followed by a space") {
      expect("Hello") {
        FixtureClass.getClassName("Hello ")
      }
    }

    it("should generate a class name for a capital word preceded by a space") {
      expect("Hello") {
        FixtureClass.getClassName(" Hello")
      }
    }

    it("should generate a class name for a lower case word") {
      expect("Hello") {
        FixtureClass.getClassName("hello")
      }
    }

    it("should generate a class name for a capital word followed by a period") {
      expect("Hello") {
        FixtureClass.getClassName("Hello.")
      }
    }

    it("should generate a class name for 2 camel cased words") {
      expect("HelloWorld") {
        FixtureClass.getClassName("HelloWorld")
      }
    }

    it("should generate a class name for 2 camel cased words followed by a period") {
      expect("HelloWorld") {
        FixtureClass.getClassName("HelloWorld.")
      }
    }

    it("should generate a class name for 2 capital words separated by spaces") {
      expect("HelloWorld") {
        FixtureClass.getClassName("Hello World")
      }
    }

    it("should generate a class name for 2 capital words separated by spaces followed by a period") {
      expect("HelloWorld") {
        FixtureClass.getClassName("Hello World.")
      }
    }

    it("should generate a class name for 2 lower case words separated by spaces") {
      expect("HelloWorld") {
        FixtureClass.getClassName("hello world")
      }
    }

    it("should generate a class name for 2 lower case words separated by spaces followed by a period") {
      expect("HelloWorld") {
        FixtureClass.getClassName("hello world.")
      }
    }
  }
}
