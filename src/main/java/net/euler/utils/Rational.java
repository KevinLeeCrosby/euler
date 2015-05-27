package net.euler.utils;

import com.google.common.base.Preconditions;

import java.util.Comparator;

import static net.euler.utils.MathUtils.gcd;
import static net.euler.utils.MathUtils.lcm;

/**
 * Rational class for keeping track of fractions.
 *
 * @author Kevin Crosby
 */
public class Rational extends Number implements Comparable<Rational>, Comparator<Rational> {
  private long numerator, denominator;

  public Rational(final long numerator) {
    this(numerator, 1);
  }

  public Rational(final long numerator, final long denominator) {
    assert denominator != 0 : "Denominator cannot be zero!";
    if(denominator > 0) {
      this.numerator = numerator;
      this.denominator = denominator;
    } else { // hold sign in numerator
      this.numerator = -numerator;
      this.denominator = -denominator;
    }
  }

  public Rational(final Rational rational) {
    this(rational.numerator, rational.denominator);
  }

  public Rational copy(final Rational rational) {
    return new Rational(rational);
  }

  public static Rational reciprocal(final Rational reciprocal) {
    return new Rational(reciprocal.denominator, reciprocal.numerator);
  }

  public Rational reciprocal() {
    return reciprocal(this);
  }

  public static void reduce(final Rational rational) {
    rational.reduce();
  }

  public void reduce() {
    long gcd = gcd(numerator, denominator);
    numerator /= gcd; // TODO verify gcd is positive
    denominator /= gcd;
  }

  public static Rational multiply(final Rational multiplicand, final Rational multiplier) {
    long n1 = multiplicand.numerator, d1 = multiplicand.denominator;
    long n2 = multiplier.numerator, d2 = multiplier.denominator;
    long gcd1 = gcd(n1, d1), gcd2 = gcd(n2, d2);
    Rational product = new Rational((n1 / gcd1) * (n2 / gcd2), (d1 / gcd1) * (d2 / gcd2));
    product.reduce();
    return product;
  }

  public Rational multiply(final Rational multiplier) {
    return multiply(this, multiplier);
  }

  public static Rational multiply(final Rational multiplicand, final long multiplier) {
    long n1 = multiplicand.numerator, d1 = multiplicand.denominator;
    long gcd1 = gcd(n1, d1);
    Rational product = new Rational(n1 / gcd1 * multiplier, d1 / gcd1);
    product.reduce();
    return product;
  }

  public Rational multiply(final long multiplier) {
    return multiply(this, multiplier);
  }

  public static Rational divide(final Rational dividend, final Rational divisor) {
    long n1 = dividend.numerator, d1 = dividend.denominator;
    long n2 = divisor.numerator, d2 = divisor.denominator;
    long gcd1 = gcd(n1, d1), gcd2 = gcd(n2, d2);
    Rational quotient = new Rational((n1 / gcd1) * (d2 / gcd2), (d1 / gcd1) * (n2 / gcd2));
    quotient.reduce();
    return quotient;
  }

  public Rational divide(final Rational divisor) {
    return divide(this, divisor);
  }

  public static Rational divide(final Rational dividend, final long divisor) {
    long n1 = dividend.numerator, d1 = dividend.denominator;
    long gcd1 = gcd(n1, d1);
    Rational product = new Rational(n1 / gcd1, d1 / gcd1 * divisor);
    product.reduce();
    return product;
  }

  public Rational divide(final long divisor) {
    return divide(this, divisor);
  }

  public static Rational add(final Rational augend, final Rational addend) {
    long n1 = augend.numerator, d1 = augend.denominator;
    long n2 = addend.numerator, d2 = addend.denominator;
    long lcm = lcm(d1, d2);
    long m1 = lcm / d1, m2 = lcm / d2;
    Rational sum = new Rational(n1 * m1 + n2 * m2, lcm);
    sum.reduce();
    return sum;
  }

  public Rational add(final Rational addend) {
    return add(this, addend);
  }

  public static Rational add(final Rational augend, final long addend) {
    long n1 = augend.numerator, d1 = augend.denominator;
    Rational sum = new Rational(n1 + addend * d1, d1);
    sum.reduce();
    return sum;
  }

  public Rational add(final long addend) {
    return add(this, addend);
  }

  public static Rational subtract(final Rational minuend, final Rational subtrahend) {
    long n1 = minuend.numerator, d1 = minuend.denominator;
    long n2 = subtrahend.numerator, d2 = subtrahend.denominator;
    long lcm = lcm(d1, d2);
    long m1 = lcm / d1, m2 = lcm / d2;
    Rational difference = new Rational(n1 * m1 - n2 * m2, lcm);
    difference.reduce();
    return difference;
  }

  public Rational subtract(final Rational subtrahend) {
    return subtract(this, subtrahend);
  }

  public static Rational subtract(final Rational minuend, final long subtrahend) {
    long n1 = minuend.numerator, d1 = minuend.denominator;
    Rational difference = new Rational(n1 - subtrahend * d1, d1);
    difference.reduce();
    return difference;
  }

  public Rational subtract(final long subtrahend) {
    return subtract(this, subtrahend);
  }

  public static Rational negate(final Rational negatend) {
    return new Rational(-negatend.numerator, negatend.denominator);
  }

  public Rational negate() {
    return negate(this);
  }

  public static Rational mediant(final Rational augmend, final Rational addend) {
    return new Rational(augmend.numerator + addend.numerator, augmend.denominator + addend.denominator);
  }

  public Rational mediant(final Rational addend) {
    return mediant(this, addend);
  }

  // TODO:  add Farey sequence

  /**
   * Define how to compare ratios.
   *
   * @param that Ratio to compare to.
   * @return -1 for <, 0 for =, and 1 for >
   */
  @Override
  public int compareTo(final Rational that) {
    Preconditions.checkNotNull(that);
    long n1 = this.numerator, d1 = this.denominator;
    long n2 = that.numerator, d2 = that.denominator;
    if(n1 * d2 < n2 * d1) {
      return -1;
    }
    if(n1 * d2 > n2 * d1) {
      return 1;
    }
    if(d1 < d2) {
      return -1;
    }
    if(d1 > d2) {
      return 1;
    }
    return 0;
  }

  public <T extends Comparable<Number>> int compareTo(final T that) {
    return -that.compareTo(this);
  }

  @Override
  public int compare(final Rational o1, final Rational o2) {
    return o1.compareTo(o2);
  }

  public <T extends Comparable<Number>> int compare(final T o1, final Rational o2) {
    return o1.compareTo(o2);
  }

  public <T extends Comparable<Number>> int compare(final Rational o1, final T o2) {
    return -o2.compareTo(o1);
  }

  public int intValue() {
    return (int) longValue();
  }

  public long longValue() {
    return numerator / denominator;
  }

  @Override
  public float floatValue() {
    return (float) doubleValue();
  }

  @Override
  public double doubleValue() {
    return Long.valueOf(numerator).doubleValue() / Long.valueOf(denominator).doubleValue();
  }

  /**
   * String method.
   *
   * @return String representing ratio and percent.
   */
  @Override
  public String toString() {
    if(denominator == 1) {
      return Long.valueOf(numerator).toString();
    }
    return String.format("%d/%d", numerator, denominator);
  }
}
