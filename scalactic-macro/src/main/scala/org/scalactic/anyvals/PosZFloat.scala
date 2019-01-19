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

import scala.collection.immutable.NumericRange

import org.scalactic.NumberCompatHelper

/**
 * An <code>AnyVal</code> for non-negative <code>Float</code>s.
 *
 * <p>
 * Because <code>PosZFloat</code> is an <code>AnyVal</code> it will usually be
 * as efficient as an <code>Float</code>, being boxed only when a
 * <code>Float</code> would have been boxed.
 * </p>
 * 
 * <p>
 * The <code>PosZFloat.apply</code> factory method is
 * implemented in terms of a macro that checks literals for
 * validity at compile time. Calling
 * <code>PosZFloat.apply</code> with a literal
 * <code>Float</code> value will either produce a valid
 * <code>PosZFloat</code> instance at run time or an error at
 * compile time. Here's an example:
 * </p>
 * 
 * <pre class="stREPL">
 * scala&gt; import anyvals._
 * import anyvals._
 *
 * scala&gt; PosZFloat(1.1F)
 * res0: org.scalactic.anyvals.PosZFloat = PosZFloat(1.1)
 *
 * scala&gt; PosZFloat(0.0F)
 * res1: org.scalactic.anyvals.PosZFloat = PosZFloat(0.0)
 *
 * scala&gt; PosZFloat(-1.1F)
 * &lt;console&gt;:14: error: PosZFloat.apply can only be invoked on a non-negative (i &gt;= 0.0F) floating point literal, like PosZFloat(42.0F).
 *               PosZFloat(-1.1F)
 *                        ^
 * </pre>
 *
 * <p>
 * <code>PosZFloat.apply</code> cannot be used if the value
 * being passed is a variable (<em>i.e.</em>, not a literal),
 * because the macro cannot determine the validity of variables
 * at compile time (just literals). If you try to pass a
 * variable to <code>PosZFloat.apply</code>, you'll get a
 * compiler error that suggests you use a different factor
 * method, <code>PosZFloat.from</code>, instead:
 * </p>
 *
 * <pre class="stREPL">
 * scala&gt; val x = 1.1F
 * x: Float = 1.1
 *
 * scala&gt; PosZFloat(x)
 * &lt;console&gt;:15: error: PosZFloat.apply can only be invoked on a floating point literal, like PosZFloat(42.0F). Please use PosZFloat.from instead.
 *               PosZFloat(x)
 *                        ^
 * </pre>
 *
 * <p>
 * The <code>PosZFloat.from</code> factory method will inspect
 * the value at runtime and return an
 * <code>Option[PosZFloat]</code>. If the value is valid,
 * <code>PosZFloat.from</code> will return a
 * <code>Some[PosZFloat]</code>, else it will return a
 * <code>None</code>.  Here's an example:
 * </p>
 *
 * <pre class="stREPL">
 * scala&gt; PosZFloat.from(x)
 * res4: Option[org.scalactic.anyvals.PosZFloat] = Some(PosZFloat(1.1))
 *
 * scala&gt; val y = -1.1F
 * y: Float = -1.1
 *
 * scala&gt; PosZFloat.from(y)
 * res5: Option[org.scalactic.anyvals.PosZFloat] = None
 * </pre>
 * 
 * <p>
 * The <code>PosZFloat.apply</code> factory method is marked implicit, so that
 * you can pass literal <code>Float</code>s into methods that require
 * <code>PosZFloat</code>, and get the same compile-time checking you get when
 * calling <code>PosZFloat.apply</code> explicitly. Here's an example:
 * </p>
 *
 * <pre class="stREPL">
 * scala&gt; def invert(pos: PosZFloat): Float = Float.MaxValue - pos
 * invert: (pos: org.scalactic.anyvals.PosZFloat)Float
 *
 * scala&gt; invert(0.0F)
 * res6: Float = 3.4028235E38
 *
 * scala&gt; invert(Float.MaxValue)
 * res7: Float = 0.0
 *
 * scala&gt; invert(-1.1F)
 * &lt;console&gt;:15: error: PosZFloat.apply can only be invoked on a non-negative (i &gt;= 0.0F) floating point literal, like PosZFloat(42.0F).
   *             invert(-1.1F)
   *                     ^
 * </pre>
 *
 * <p>
 * This example also demonstrates that the
 * <code>PosZFloat</code> companion object also defines
 * implicit widening conversions when a similar conversion is
 * provided in Scala. This makes it convenient to use a
 * <code>PosZFloat</code> where a <code>Float</code> or wider
 * type is needed. An example is the subtraction in the body of
 * the <code>invert</code> method defined above,
 * <code>Float.MaxValue - pos</code>. Although
 * <code>Float.MaxValue</code> is an <code>Float</code>, which
 * has no <code>-</code> method that takes a
 * <code>PosZFloat</code> (the type of <code>pos</code>), you
 * can still subtract <code>pos</code>, because the
 * <code>PosZFloat</code> will be implicitly widened to
 * <code>Float</code>.
 * </p>
 *
 * @param value The <code>Float</code> value underlying this <code>PosZFloat</code>.
 */ 
final class PosZFloat private (val value: Float) extends AnyVal {

  /**
   * A string representation of this <code>PosZFloat</code>.
   */
  override def toString: String = s"PosZFloat($value)"

  /**
   * Converts this <code>PosZFloat</code> to a <code>Byte</code>.
   */
  def toByte: Byte = value.toByte

  /**
   * Converts this <code>PosZFloat</code> to a <code>Short</code>.
   */
  def toShort: Short = value.toShort

  /**
   * Converts this <code>PosZFloat</code> to a <code>Char</code>.
   */
  def toChar: Char = value.toChar

  /**
   * Converts this <code>PosZFloat</code> to an <code>Int</code>.
   */
  def toInt: Int = value.toInt

  /**
   * Converts this <code>PosZFloat</code> to a <code>Long</code>.
   */
  def toLong: Long = value.toLong

  /**
   * Converts this <code>PosZFloat</code> to a <code>Float</code>.
   */
  def toFloat: Float = value.toFloat

  /**
   * Converts this <code>PosZFloat</code> to a <code>Double</code>.
   */
  def toDouble: Double = value.toDouble

  /** Returns this value, unmodified. */
  def unary_+ : PosZFloat = this
  /** Returns the negation of this value. */
  def unary_- : Float = -value

  /**
   * Converts this <code>PosZFloat</code>'s value to a string then concatenates the given string.
   */
  def +(x: String): String = value + x

  /** Returns `true` if this value is less than x, `false` otherwise. */
  def <(x: Byte): Boolean = value < x
  /** Returns `true` if this value is less than x, `false` otherwise. */
  def <(x: Short): Boolean = value < x
  /** Returns `true` if this value is less than x, `false` otherwise. */
  def <(x: Char): Boolean = value < x
  /** Returns `true` if this value is less than x, `false` otherwise. */
  def <(x: Int): Boolean = value < x
  /** Returns `true` if this value is less than x, `false` otherwise. */
  def <(x: Long): Boolean = value < x
  /** Returns `true` if this value is less than x, `false` otherwise. */
  def <(x: Float): Boolean = value < x
  /** Returns `true` if this value is less than x, `false` otherwise. */
  def <(x: Double): Boolean = value < x

  /** Returns `true` if this value is less than or equal to x, `false` otherwise. */
  def <=(x: Byte): Boolean = value <= x
  /** Returns `true` if this value is less than or equal to x, `false` otherwise. */
  def <=(x: Short): Boolean = value <= x
  /** Returns `true` if this value is less than or equal to x, `false` otherwise. */
  def <=(x: Char): Boolean = value <= x
  /** Returns `true` if this value is less than or equal to x, `false` otherwise. */
  def <=(x: Int): Boolean = value <= x
  /** Returns `true` if this value is less than or equal to x, `false` otherwise. */
  def <=(x: Long): Boolean = value <= x
  /** Returns `true` if this value is less than or equal to x, `false` otherwise. */
  def <=(x: Float): Boolean = value <= x
  /** Returns `true` if this value is less than or equal to x, `false` otherwise. */
  def <=(x: Double): Boolean = value <= x

  /** Returns `true` if this value is greater than x, `false` otherwise. */
  def >(x: Byte): Boolean = value > x
  /** Returns `true` if this value is greater than x, `false` otherwise. */
  def >(x: Short): Boolean = value > x
  /** Returns `true` if this value is greater than x, `false` otherwise. */
  def >(x: Char): Boolean = value > x
  /** Returns `true` if this value is greater than x, `false` otherwise. */
  def >(x: Int): Boolean = value > x
  /** Returns `true` if this value is greater than x, `false` otherwise. */
  def >(x: Long): Boolean = value > x
  /** Returns `true` if this value is greater than x, `false` otherwise. */
  def >(x: Float): Boolean = value > x
  /** Returns `true` if this value is greater than x, `false` otherwise. */
  def >(x: Double): Boolean = value > x

  /** Returns `true` if this value is greater than or equal to x, `false` otherwise. */
  def >=(x: Byte): Boolean = value >= x
  /** Returns `true` if this value is greater than or equal to x, `false` otherwise. */
  def >=(x: Short): Boolean = value >= x
  /** Returns `true` if this value is greater than or equal to x, `false` otherwise. */
  def >=(x: Char): Boolean = value >= x
  /** Returns `true` if this value is greater than or equal to x, `false` otherwise. */
  def >=(x: Int): Boolean = value >= x
  /** Returns `true` if this value is greater than or equal to x, `false` otherwise. */
  def >=(x: Long): Boolean = value >= x
  /** Returns `true` if this value is greater than or equal to x, `false` otherwise. */
  def >=(x: Float): Boolean = value >= x
  /** Returns `true` if this value is greater than or equal to x, `false` otherwise. */
  def >=(x: Double): Boolean = value >= x

  /** Returns the sum of this value and `x`. */
  def +(x: Byte): Float = value + x
  /** Returns the sum of this value and `x`. */
  def +(x: Short): Float = value + x
  /** Returns the sum of this value and `x`. */
  def +(x: Char): Float = value + x
  /** Returns the sum of this value and `x`. */
  def +(x: Int): Float = value + x
  /** Returns the sum of this value and `x`. */
  def +(x: Long): Float = value + x
  /** Returns the sum of this value and `x`. */
  def +(x: Float): Float = value + x
  /** Returns the sum of this value and `x`. */
  def +(x: Double): Double = value + x

  /** Returns the difference of this value and `x`. */
  def -(x: Byte): Float = value - x
  /** Returns the difference of this value and `x`. */
  def -(x: Short): Float = value - x
  /** Returns the difference of this value and `x`. */
  def -(x: Char): Float = value - x
  /** Returns the difference of this value and `x`. */
  def -(x: Int): Float = value - x
  /** Returns the difference of this value and `x`. */
  def -(x: Long): Float = value - x
  /** Returns the difference of this value and `x`. */
  def -(x: Float): Float = value - x
  /** Returns the difference of this value and `x`. */
  def -(x: Double): Double = value - x

  /** Returns the product of this value and `x`. */
  def *(x: Byte): Float = value * x
  /** Returns the product of this value and `x`. */
  def *(x: Short): Float = value * x
  /** Returns the product of this value and `x`. */
  def *(x: Char): Float = value * x
  /** Returns the product of this value and `x`. */
  def *(x: Int): Float = value * x
  /** Returns the product of this value and `x`. */
  def *(x: Long): Float = value * x
  /** Returns the product of this value and `x`. */
  def *(x: Float): Float = value * x
  /** Returns the product of this value and `x`. */
  def *(x: Double): Double = value * x

  /** Returns the quotient of this value and `x`. */
  def /(x: Byte): Float = value / x
  /** Returns the quotient of this value and `x`. */
  def /(x: Short): Float = value / x
  /** Returns the quotient of this value and `x`. */
  def /(x: Char): Float = value / x
  /** Returns the quotient of this value and `x`. */
  def /(x: Int): Float = value / x
  /** Returns the quotient of this value and `x`. */
  def /(x: Long): Float = value / x
  /** Returns the quotient of this value and `x`. */
  def /(x: Float): Float = value / x
  /** Returns the quotient of this value and `x`. */
  def /(x: Double): Double = value / x

  /** Returns the remainder of the division of this value by `x`. */
  def %(x: Byte): Float = value % x
  /** Returns the remainder of the division of this value by `x`. */
  def %(x: Short): Float = value % x
  /** Returns the remainder of the division of this value by `x`. */
  def %(x: Char): Float = value % x
  /** Returns the remainder of the division of this value by `x`. */
  def %(x: Int): Float = value % x
  /** Returns the remainder of the division of this value by `x`. */
  def %(x: Long): Float = value % x
  /** Returns the remainder of the division of this value by `x`. */
  def %(x: Float): Float = value % x
  /** Returns the remainder of the division of this value by `x`. */
  def %(x: Double): Double = value % x

  // Stuff from RichFloat
  def isPosInfinity: Boolean = Float.PositiveInfinity == value

  /**
  * Returns <code>this</code> if <code>this &gt; that</code> or <code>that</code> otherwise.
  */
  def max(that: PosZFloat): PosZFloat = if (math.max(value, that.value) == value) this else that

  /**
  * Returns <code>this</code> if <code>this &lt; that</code> or <code>that</code> otherwise.
  */
  def min(that: PosZFloat): PosZFloat = if (math.min(value, that.value) == value) this else that

  def isWhole = {
    val longValue = value.toLong
    longValue.toFloat == value || longValue == Long.MaxValue && value < Float.PositiveInfinity || longValue == Long.MinValue && value > Float.NegativeInfinity
  }

  def round: PosZInt = PosZInt.from(math.round(value)).get
  def ceil: PosZFloat = PosZFloat.from(math.ceil(value).toFloat).get
  def floor: PosZFloat = PosZFloat.from(math.floor(value).toFloat).get

  /** Converts an angle measured in degrees to an approximately equivalent
  * angle measured in radians.
  *
  * @return the measurement of the angle x in radians.
  */
  def toRadians: PosZFloat = PosZFloat.from(math.toRadians(value).toFloat).get

  /** Converts an angle measured in radians to an approximately equivalent
  * angle measured in degrees.
  * @return the measurement of the angle x in degrees.
  */
  def toDegrees: PosZFloat = PosZFloat.from(math.toDegrees(value).toFloat).get

  // adapted from RichInt:
  /**
  * Create a <code>Range.Partial</code> from this <code>PosZFloat</code> value
  * until the specified <code>end</code> (exclusive) with step value 1.
  *
  * @param end The final bound of the range to make.
  * @return A [[scala.collection.immutable.Range.Partial[Float, NumericRange[Float]]]] from `this` up to but
  * not including `end`.
  */
  @deprecated("This function will be removed in future version of Scalactic, use BigDecimal's until function instead")
  def until(end: Float) = NumberCompatHelper.floatUntil(value, end)

  /**
  * Create a <code>Range</code> from this <code>PosZFloat</code> value
  * until the specified <code>end</code> (exclusive) with the specified <code>step</code> value.
  *
  * @param end The final bound of the range to make.
  * @param end The final bound of the range to make.
  * @param step The number to increase by for each step of the range.
  * @return A [[scala.collection.immutable.NumericRange.Exclusive[Float]]] from `this` up to but
  * not including `end`.
  */
  @deprecated("This function will be removed in future version of Scalactic, use BigDecimal's until function instead")
  def until(end: Float, step: Float) = NumberCompatHelper.floatUntil(value, end, step)

  /**
  * Create an inclusive <code>Range</code> from this <code>PosZFloat</code> value
  * to the specified <code>end</code> with step value 1.
  *
  * @param end The final bound of the range to make.
  * @return A [[scala.collection.immutable.Range.Partial[Float, NumericRange[Float]]]] from `'''this'''` up to
  * and including `end`.
  */
  @deprecated("This function will be removed in future version of Scalactic, use BigDecimal's to function instead")
  def to(end: Float) = NumberCompatHelper.floatTo(value, end)

  /**
  * Create an inclusive <code>Range</code> from this <code>PosZFloat</code> value
  * to the specified <code>end</code> with the specified <code>step</code> value.
  *
  * @param end The final bound of the range to make.
  * @param step The number to increase by for each step of the range.
  * @return A [[scala.collection.immutable.NumericRange.Inclusive[Float]]] from `'''this'''` up to
  * and including `end`.
  */
  @deprecated("This function will be removed in future version of Scalactic, use BigDecimal's to function instead")
  def to(end: Float, step: Float)= NumberCompatHelper.floatTo(value, end, step)
}

/**
 * The companion object for <code>PosZFloat</code> that offers
 * factory methods that produce <code>PosZFloat</code>s, implicit
 * widening conversions from <code>PosZFloat</code> to other
 * numeric types, and maximum and minimum constant values for
 * <code>PosZFloat</code>.
 */
object PosZFloat {
  /**
   * The largest value representable as a non-negative <code>Float</code>,
   * which is <code>PosZFloat(3.4028235E38)</code>.
   */
  final val MaxValue: PosZFloat = PosZFloat.from(Float.MaxValue).get

  /**
   * The smallest value representable as a non-negative <code>Float</code>,
   * which is <code>PosZFloat(0.0F)</code>.
   */
  final val MinValue: PosZFloat = PosZFloat.from(0.0f).get // Can't use the macro here

  /**
   * A factory method that produces an <code>Option[PosZFloat]</code> given a
   * <code>Float</code> value.
   *
   * <p>
   * This method will inspect the passed <code>Float</code> value
   * and if it is a non-negative <code>Float</code>,
   * <em>i.e.</em>, a value greater than or equal to 0, it will
   * return a <code>PosZFloat</code> representing that value,
   * wrapped in a <code>Some</code>. Otherwise, the passed
   * <code>Float</code> value is negative, so this method
   * will return <code>None</code>.
   * </p>
   *
   * <p>
   * This factory method differs from the <code>apply</code>
   * factory method in that <code>apply</code> is implemented
   * via a macro that inspects <code>Float</code> literals at
   * compile time, whereas <code>from</code> inspects
   * <code>Float</code> values at run time.
   * </p>
   *
   * @param value the <code>Float</code> to inspect, and if non-negative, return
   *     wrapped in a <code>Some[PosZFloat]</code>.
   * @return the specified <code>Float</code> value wrapped
   *     in a <code>Some[PosZFloat]</code>, if it is positive, else
   *     <code>None</code>.
   */
  def from(value: Float): Option[PosZFloat] =
    if (value >= 0.0F) Some(new PosZFloat(value)) else None

  import language.experimental.macros
  import scala.language.implicitConversions

  /**
   * A factory method, implemented via a macro, that produces a
   * <code>PosZFloat</code> if passed a valid <code>Float</code>
   * literal, otherwise a compile time error.
   *
   * <p>
   * The macro that implements this method will inspect the
   * specified <code>Float</code> expression at compile time. If
   * the expression is a non-negative <code>Float</code> literal,
   * <em>i.e.</em>, with a value greater than or equal to 0, it will return
   * a <code>PosZFloat</code> representing that value.  Otherwise,
   * the passed <code>Float</code> expression is either a literal
   * that is negative, or is not a literal, so this method
   * will give a compiler error.
   * </p>
   *
   * <p>
   * This factory method differs from the <code>from</code>
   * factory method in that this method is implemented via a
   * macro that inspects <code>Float</code> literals at compile
   * time, whereas <code>from</code> inspects <code>Float</code>
   * values at run time.
   * </p>
   *
   * @param value the <code>Float</code> literal expression to inspect at
   *     compile time, and if non-negative, to return wrapped in a
   *     <code>PosZFloat</code> at run time.
   * @return the specified, valid <code>Float</code> literal
   *     value wrapped in a <code>PosZFloat</code>. (If the
   *     specified expression is not a valid <code>Float</code>
   *     literal, the invocation of this method will not
   *     compile.)
   */
  implicit def apply(value: Float): PosZFloat = macro PosZFloatMacro.apply

  /**
   * Implicit widening conversion from <code>PosZFloat</code> to
   * <code>Float</code>.
   *
   * @param pos the <code>PosZFloat</code> to widen
   * @return the <code>Float</code> value underlying the specified
   *     <code>PosZFloat</code>.
   */
  implicit def widenToFloat(poz: PosZFloat): Float = poz.value

  /**
   * Implicit widening conversion from <code>PosZFloat</code> to
   * <code>Double</code>.
   *
   * @param pos the <code>PosZFloat</code> to widen
   * @return the <code>Float</code> value underlying the specified
   *     <code>PosZFloat</code>, widened to <code>Double</code>.
   */
  implicit def widenToDouble(poz: PosZFloat): Double = poz.value

  /**
   * Implicit widening conversion from <code>PosZFloat</code> to
   * <code>PosZDouble</code>.
   *
   * @param pos the <code>PosZFloat</code> to widen
   * @return the <code>Float</code> value underlying the specified
   *     <code>PosZFloat</code>, widened to <code>Double</code>
   *     and wrapped in a <code>PosZDouble</code>.
   */
  implicit def widenToPosZDouble(poz: PosZFloat): PosZDouble = PosZDouble.from(poz.value).get

  /**
   * Implicit Ordering instance.
   */
  implicit val posZFloatOrd: Ordering[PosZFloat] =
    new Ordering[PosZFloat] {
      def compare(x: PosZFloat, y: PosZFloat): Int = x.toFloat.compare(y)
    } 
}

