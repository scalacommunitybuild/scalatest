/*
 * Copyright 2001-2014 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalactic.anyvals

import org.scalatest._
import org.scalacheck.Gen._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
// SKIP-SCALATESTJS-START
import scala.collection.immutable.NumericRange
// SKIP-SCALATESTJS-END
import OptionValues._
import scala.collection.mutable.WrappedArray
//import org.scalactic.StrictCheckedEquality
import Double.NaN
import org.scalactic.Equality
import org.scalactic.NumberCompatHelper

class PosZDoubleSpec extends FunSpec with Matchers with GeneratorDrivenPropertyChecks {

  val posZDoubleGen: Gen[PosZDouble] =
    for {i <- choose(0, Double.MaxValue)} yield PosZDouble.from(i).get

  implicit val arbPosZDouble: Arbitrary[PosZDouble] = Arbitrary(posZDoubleGen)

  implicit val doubleEquality: Equality[Double] =
    new Equality[Double] {
      override def areEqual(a: Double, b: Any): Boolean =
        (a, b) match {
          case (a, bDouble: Double) if a.isNaN && bDouble.isNaN  => true
          case _ => a == b
        }
    }

  implicit val floatEquality: Equality[Float] =
    new Equality[Float] {
      override def areEqual(a: Float, b: Any): Boolean =
        (a, b) match {
          case (a, bFloat: Float) if a.isNaN && bFloat.isNaN => true
          case _ => a == b
        }
    }


  describe("A PosZDouble") {
    describe("should offer a from factory method that") {
      it("returns Some[PosZDouble] if the passed Double is greater than or equal to 0") {
        PosZDouble.from(0.0).value.value shouldBe 0.0
        PosZDouble.from(50.23).value.value shouldBe 50.23
        PosZDouble.from(100.0).value.value shouldBe 100.0
      }
      it("returns None if the passed Double is NOT greater than or equal to 0") {
        PosZDouble.from(-0.00001) shouldBe None
        PosZDouble.from(-99.9) shouldBe None
      }
    } 
    it("should offer MaxValue and MinValue factory methods") {
      PosZDouble.MaxValue shouldEqual PosZDouble.from(Double.MaxValue).get
      PosZDouble.MinValue shouldEqual PosZDouble(0.0)
    }
    it("should have a pretty toString") {
      // SKIP-SCALATESTJS-START
      PosZDouble.from(42.0).value.toString shouldBe "PosZDouble(42.0)"
      // SKIP-SCALATESTJS-END
      //SCALATESTJS-ONLY PosZDouble.from(42.0).value.toString shouldBe "PosZDouble(42)"
    }
    it("should return the same type from its unary_+ method") {
      +PosZDouble(3.0) shouldEqual PosZDouble(3.0)
    } 
    it("should be automatically widened to compatible AnyVal targets") {
      "PosZDouble(3.0): Int" shouldNot typeCheck
      "PosZDouble(3.0): Long" shouldNot typeCheck
      "PosZDouble(3.0): Float" shouldNot typeCheck
      (PosZDouble(3.0): Double) shouldEqual 3.0

      "PosZDouble(3.0): PosInt" shouldNot typeCheck
      "PosZDouble(3.0): PosLong" shouldNot typeCheck
      "PosZDouble(3.0): PosFloat" shouldNot typeCheck
      "PosZDouble(3.0): PosDouble" shouldNot typeCheck

      "PosZDouble(3.0): PosZInt" shouldNot typeCheck
      "PosZDouble(3.0): PosZLong" shouldNot typeCheck
      "PosZDouble(3.0): PosZFloat" shouldNot typeCheck
      (PosZDouble(3.0): PosZDouble) shouldEqual PosZDouble(3.0)
    }

    it("should be sortable") {
      val xs = List(PosZDouble(2.2), PosZDouble(0.0), PosZDouble(1.1),
                    PosZDouble(3.3))
      xs.sorted shouldEqual List(PosZDouble(0.0), PosZDouble(1.1),
                                 PosZDouble(2.2), PosZDouble(3.3))
    }

    describe("when a compatible AnyVal is passed to a + method invoked on it") {
      it("should give the same AnyVal type back at compile time, and correct value at runtime") {
        // When adding a "primitive"
        val opInt = PosZDouble(3.0) + 3
        opInt shouldEqual 6.0

        val opLong = PosZDouble(3.0) + 3L
        opLong shouldEqual 6.0

        val opFloat = PosZDouble(3.0) + 3.0F
        opFloat shouldEqual 6.0

        val opDouble = PosZDouble(3.0) + 3.0
        opDouble shouldEqual 6.0

        // When adding a Pos*
        val opPosInt = PosZDouble(3.0) + PosInt(3)
        opPosInt shouldEqual 6.0

        val opPosLong = PosZDouble(3.0) + PosLong(3L)
        opPosLong shouldEqual 6.0

        val opPosFloat = PosZDouble(3.0) + PosFloat(3.0F)
        opPosFloat shouldEqual 6.0

        val opPosDouble = PosZDouble(3.0) + PosDouble(3.0)
        opPosDouble shouldEqual 6.0

        // When adding a *PosZ
        val opPosZ = PosZDouble(3.0) + PosZInt(3)
        opPosZ shouldEqual 6.0

        val opPosZLong = PosZDouble(3.0) + PosZLong(3L)
        opPosZLong shouldEqual 6.0

        val opPosZFloat = PosZDouble(3.0) + PosZFloat(3.0F)
        opPosZFloat shouldEqual 6.0

        val opPosZDouble = PosZDouble(3.0) + PosZDouble(3.0)
        opPosZDouble shouldEqual 6.0
      }
    }

    describe("when created with apply method") {

      it("should compile when 8 is passed in") {
        "PosZDouble(8)" should compile
        PosZDouble(8).value shouldEqual 8.0
        "PosZDouble(8L)" should compile
        PosZDouble(8L).value shouldEqual 8.0
        "PosZDouble(8.0F)" should compile
        PosZDouble(8.0F).value shouldEqual 8.0
        "PosZDouble(8.0)" should compile
        PosZDouble(8.0).value shouldEqual 8.0
      }

      it("should compile when 0 is passed in") {
        "PosZDouble(0)" should compile
        PosZDouble(0).value shouldEqual 0.0
        "PosZDouble(0L)" should compile
        PosZDouble(0L).value shouldEqual 0.0
        "PosZDouble(0.0F)" should compile
        PosZDouble(0.0F).value shouldEqual 0.0
        "PosZDouble(0.0)" should compile
        PosZDouble(0.0).value shouldEqual 0.0
      }

      it("should not compile when -8 is passed in") {
        "PosZDouble(-8)" shouldNot compile
        "PosZDouble(-8L)" shouldNot compile
        "PosZDouble(-8.0F)" shouldNot compile
        "PosZDouble(-8.0)" shouldNot compile
      }
      it("should not compile when x is passed in") {
        val a: Int = -8
        "PosZDouble(a)" shouldNot compile
        val b: Long = -8L
        "PosZDouble(b)" shouldNot compile
        val c: Float = -8.0F
        "PosZDouble(c)" shouldNot compile
        val d: Double = -8.0
        "PosZDouble(d)" shouldNot compile
      }
    }
    describe("when specified as a plain-old Double") {

      def takesPosZDouble(poz: PosZDouble): Double = poz.value

      it("should compile when 8 is passed in") {
        "takesPosZDouble(8)" should compile
        takesPosZDouble(8) shouldEqual 8.0
        "takesPosZDouble(8L)" should compile
        takesPosZDouble(8L) shouldEqual 8.0
        "takesPosZDouble(8.0F)" should compile
        takesPosZDouble(8.0F) shouldEqual 8.0
        "takesPosZDouble(8.0)" should compile
        takesPosZDouble(8.0) shouldEqual 8.0
      }

      it("should compile when 0 is passed in") {
        "takesPosZDouble(0)" should compile
        takesPosZDouble(0) shouldEqual 0.0
        "takesPosZDouble(0L)" should compile
        takesPosZDouble(0L) shouldEqual 0.0
        "takesPosZDouble(0.0F)" should compile
        takesPosZDouble(0.0F) shouldEqual 0.0
        "takesPosZDouble(0.0)" should compile
        takesPosZDouble(0.0) shouldEqual 0.0
      }

      it("should not compile when -8 is passed in") {
        "takesPosZDouble(-8)" shouldNot compile
        "takesPosZDouble(-8L)" shouldNot compile
        "takesPosZDouble(-8.0F)" shouldNot compile
        "takesPosZDouble(-8.0)" shouldNot compile
      }

      it("should not compile when x is passed in") {
        val x: Int = -8
        "takesPosZDouble(x)" shouldNot compile
        val b: Long = -8L
        "takesPosZDouble(b)" shouldNot compile
        val c: Float = -8.0F
        "takesPosZDouble(c)" shouldNot compile
        val d: Double = -8.0
        "takesPosZDouble(d)" shouldNot compile
      }
    }

    it("should offer a unary + method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble) =>
        (+pzdouble).toDouble shouldEqual (+(pzdouble.toDouble))
      }
    }

    it("should offer a unary - method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble) =>
        (-pzdouble) shouldEqual (-(pzdouble.toDouble))
      }
    }

    it("should offer '<' comparison that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        (pzdouble < byte) shouldEqual (pzdouble.toDouble < byte)
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        (pzdouble < short) shouldEqual (pzdouble.toDouble < short)
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        (pzdouble < char) shouldEqual (pzdouble.toDouble < char)
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        (pzdouble < int) shouldEqual (pzdouble.toDouble < int)
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        (pzdouble < long) shouldEqual (pzdouble.toDouble < long)
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        (pzdouble < float) shouldEqual (pzdouble.toDouble < float)
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        (pzdouble < double) shouldEqual (pzdouble.toDouble < double)
      }
    }

    it("should offer '<=' comparison that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        (pzdouble <= byte) shouldEqual (pzdouble.toDouble <= byte)
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        (pzdouble <= char) shouldEqual (pzdouble.toDouble <= char)
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        (pzdouble <= short) shouldEqual (pzdouble.toDouble <= short)
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        (pzdouble <= int) shouldEqual (pzdouble.toDouble <= int)
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        (pzdouble <= long) shouldEqual (pzdouble.toDouble <= long)
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        (pzdouble <= float) shouldEqual (pzdouble.toDouble <= float)
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        (pzdouble <= double) shouldEqual (pzdouble.toDouble <= double)
      }
    }

    it("should offer '>' comparison that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        (pzdouble > byte) shouldEqual (pzdouble.toDouble > byte)
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        (pzdouble > short) shouldEqual (pzdouble.toDouble > short)
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        (pzdouble > char) shouldEqual (pzdouble.toDouble > char)
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        (pzdouble > int) shouldEqual (pzdouble.toDouble > int)
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        (pzdouble > long) shouldEqual (pzdouble.toDouble > long)
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        (pzdouble > float) shouldEqual (pzdouble.toDouble > float)
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        (pzdouble > double) shouldEqual (pzdouble.toDouble > double)
      }
    }

    it("should offer '>=' comparison that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        (pzdouble >= byte) shouldEqual (pzdouble.toDouble >= byte)
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        (pzdouble >= short) shouldEqual (pzdouble.toDouble >= short)
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        (pzdouble >= char) shouldEqual (pzdouble.toDouble >= char)
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        (pzdouble >= int) shouldEqual (pzdouble.toDouble >= int)
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        (pzdouble >= long) shouldEqual (pzdouble.toDouble >= long)
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        (pzdouble >= float) shouldEqual (pzdouble.toDouble >= float)
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        (pzdouble >= double) shouldEqual (pzdouble.toDouble >= double)
      }
    }

    it("should offer a '+' method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        (pzdouble + byte) shouldEqual (pzdouble.toDouble + byte)
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        (pzdouble + short) shouldEqual (pzdouble.toDouble + short)
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        (pzdouble + char) shouldEqual (pzdouble.toDouble + char)
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        (pzdouble + int) shouldEqual (pzdouble.toDouble + int)
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        (pzdouble + long) shouldEqual (pzdouble.toDouble + long)
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        (pzdouble + float) shouldEqual (pzdouble.toDouble + float)
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        (pzdouble + double) shouldEqual (pzdouble.toDouble + double)
      }
    }

    it("should offer a '-' method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        (pzdouble - byte) shouldEqual (pzdouble.toDouble - byte)
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        (pzdouble - short) shouldEqual (pzdouble.toDouble - short)
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        (pzdouble - char) shouldEqual (pzdouble.toDouble - char)
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        (pzdouble - int) shouldEqual (pzdouble.toDouble - int)
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        (pzdouble - long) shouldEqual (pzdouble.toDouble - long)
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        (pzdouble - float) shouldEqual (pzdouble.toDouble - float)
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        (pzdouble - double) shouldEqual (pzdouble.toDouble - double)
      }
    }

    it("should offer a '*' method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        (pzdouble * byte) shouldEqual (pzdouble.toDouble * byte)
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        (pzdouble * short) shouldEqual (pzdouble.toDouble * short)
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        (pzdouble * char) shouldEqual (pzdouble.toDouble * char)
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        (pzdouble * int) shouldEqual (pzdouble.toDouble * int)
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        (pzdouble * long) shouldEqual (pzdouble.toDouble * long)
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        (pzdouble * float) shouldEqual (pzdouble.toDouble * float)
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        (pzdouble * double) shouldEqual (pzdouble.toDouble * double)
      }
    }

    it("should offer a '/' method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        pzdouble / byte shouldEqual pzdouble.toDouble / byte
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        pzdouble / short shouldEqual pzdouble.toDouble / short
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        pzdouble / char shouldEqual pzdouble.toDouble / char
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        pzdouble / int shouldEqual pzdouble.toDouble / int
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        pzdouble / long shouldEqual pzdouble.toDouble / long
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        pzdouble / float shouldEqual pzdouble.toDouble / float
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        pzdouble / double shouldEqual pzdouble.toDouble / double
      }
    }

    // note: since a PosInt % 0 is NaN (as opposed to PosInt / 0, which is Infinity)
    // extra logic is needed to convert to a comparable type (boolean, in this case)
    it("should offer a '%' method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble, byte: Byte) =>
        val res = pzdouble % byte
        if (res.isNaN)
          (pzdouble.toDouble % byte).isNaN shouldBe true
        else
          res shouldEqual pzdouble.toDouble % byte
      }
      forAll { (pzdouble: PosZDouble, short: Short) =>
        val res = pzdouble % short
        if (res.isNaN)
          (pzdouble.toDouble % short).isNaN shouldBe true
        else
          res shouldEqual pzdouble.toDouble % short
      }
      forAll { (pzdouble: PosZDouble, char: Char) =>
        val res = pzdouble % char
        if (res.isNaN)
          (pzdouble.toDouble % char).isNaN shouldBe true
        else
          res shouldEqual pzdouble.toDouble % char
      }
      forAll { (pzdouble: PosZDouble, int: Int) =>
        val res = pzdouble % int
        if (res.isNaN)
          (pzdouble.toDouble % int).isNaN shouldBe true
        else
          res shouldEqual pzdouble.toDouble % int
      }
      forAll { (pzdouble: PosZDouble, long: Long) =>
        val res = pzdouble % long
        if (res.isNaN)
          (pzdouble.toDouble % long).isNaN shouldBe true
        else
          res shouldEqual pzdouble.toDouble % long
      }
      forAll { (pzdouble: PosZDouble, float: Float) =>
        val res = pzdouble % float
        if (res.isNaN)
          (pzdouble.toDouble % float).isNaN shouldBe true
        else
          res shouldEqual pzdouble.toDouble % float
      }
      forAll { (pzdouble: PosZDouble, double: Double) =>
        val res = pzdouble % double
        if (res.isNaN)
          (pzdouble.toDouble % double).isNaN shouldBe true
        else
          res shouldEqual pzdouble.toDouble % double
      }
    }

    it("should offer 'min' and 'max' methods that are consistent with Double") {
      forAll { (pzdouble1: PosZDouble, pzdouble2: PosZDouble) =>
        pzdouble1.max(pzdouble2).toDouble shouldEqual pzdouble1.toDouble.max(pzdouble2.toDouble)
        pzdouble1.min(pzdouble2).toDouble shouldEqual pzdouble1.toDouble.min(pzdouble2.toDouble)
      }
    }

    it("should offer an 'isWhole' method that is consistent with Double") {
      forAll { (pzdouble: PosZDouble) =>
        pzdouble.isWhole shouldEqual pzdouble.toDouble.isWhole
      }
    }

    it("should offer 'round', 'ceil', and 'floor' methods that are consistent with Double") {
      forAll { (pzdouble: PosZDouble) =>
        pzdouble.round.toDouble shouldEqual pzdouble.toDouble.round
        pzdouble.ceil.toDouble shouldEqual pzdouble.toDouble.ceil
        pzdouble.floor.toDouble shouldEqual pzdouble.toDouble.floor
      }
    }

    it("should offer 'toRadians' and 'toDegrees' methods that are consistent with Double") {
      forAll { (pzdouble: PosZDouble) =>
        pzdouble.toRadians shouldEqual pzdouble.toDouble.toRadians
      }
    }

    // SKIP-SCALATESTJS-START
    it("should offer 'to' and 'until' method that is consistent with Double") {
      def rangeEqual(a: NumericRange[_], b: NumericRange[_]): Boolean =
        a.start == b.start && a.end == b.end && a.step == b.step

      forAll { (pzdouble: PosZDouble, end: Double, step: Double) =>
        rangeEqual(pzdouble.until(end).by(1f), NumberCompatHelper.doubleUntil(pzdouble.toDouble, end).by(1f)) shouldBe true
        rangeEqual(pzdouble.until(end, step), NumberCompatHelper.doubleUntil(pzdouble.toDouble, end, step)) shouldBe true
        rangeEqual(pzdouble.to(end).by(1f), NumberCompatHelper.doubleTo(pzdouble.toDouble, end).by(1f)) shouldBe true
        rangeEqual(pzdouble.to(end, step), NumberCompatHelper.doubleTo(pzdouble.toDouble, end, step)) shouldBe true
      }
    }
    // SKIP-SCALATESTJS-END

    it("should offer widening methods for basic types that are consistent with Double") {
      forAll { (pzdouble: PosZDouble) =>
        def widen(value: Double): Double = value
        widen(pzdouble) shouldEqual widen(pzdouble.toDouble)
      }
    }
  }
}

