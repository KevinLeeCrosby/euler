package net.euler.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

/**
 * Useful polynomial utilities.
 *
 * @author Kevin Crosby
 */
public class Polynomial {
  private NavigableMap<Long, Rational> coefficients;

  public Polynomial() {
    coefficients = Maps.<Long, Rational>newTreeMap().descendingMap();
  }

  public Polynomial(final Polynomial polynomial) {
    coefficients = Maps.newTreeMap(polynomial.coefficients);
  }

  public Polynomial(final List<Rational> coefficients) {
    this();
    long index = 0;
    for (final Rational coefficient : coefficients) {
      set(index++, coefficient);
    }
  }

  public Polynomial(final Rational... coefficients) {
    this(Lists.newArrayList(coefficients));
  }

  public Polynomial(final Integer... coefficients) {
    this(Lists.transform(Lists.newArrayList(coefficients), Rational::new));
  }

  public Polynomial(final Long... coefficients) {
    this(Lists.transform(Lists.newArrayList(coefficients), Rational::new));
  }

  public boolean isEmpty() {
    return nnz() == 0;
  }

  public long nnz() {
    return coefficients.size();
  }

  public long order() {
    return isEmpty() ? 0 : coefficients.firstKey();
  }

  public Rational get(final long index) {
    return coefficients.getOrDefault(index, Rational.ZERO);
  }

  public void set(final long index, final Rational coefficient) {
    if (coefficient.equals(Rational.ZERO)) {
      coefficients.remove(index);
    } else {
      coefficients.put(index, coefficient);
    }
  }

  public void set(final long index, final long coefficient) {
    set(index, new Rational(coefficient));
  }

  public Rational evaluate(final long argument) {
    return evaluate(this, argument);
  }

  public static Rational evaluate(final Polynomial function, final long argument) {
    Rational result = Rational.ZERO;
    Map<Long, Long> powers = new HashMap<Long, Long>() {{
      put(1L, argument);
    }};
    long n = function.order() + 1;
    for (final Entry<Long, Rational> entry : function.coefficients.entrySet()) {
      long exponent = n - entry.getKey();
      if (!powers.containsKey(exponent)) powers.put(exponent, MathUtils.pow(argument, exponent));
      long multiplier = powers.get(exponent);
      Rational coefficient = entry.getValue();
      result = coefficient.add(result.multiply(multiplier));
      n -= exponent;
    }
    if (n > 0) {
      if (!powers.containsKey(n)) powers.put(n, MathUtils.pow(argument, n));
      result = result.multiply(powers.get(n));
    }
    return result;
  }

  public Rational evaluate(final Rational argument) {
    return evaluate(this, argument);
  }

  public static Rational evaluate(final Polynomial function, final Rational argument) {
    Rational result = Rational.ZERO;
    Map<Long, Rational> powers = new HashMap<Long, Rational>() {{
      put(1L, argument);
    }};
    long n = function.order() + 1;
    for (final Entry<Long, Rational> entry : function.coefficients.entrySet()) {
      long exponent = n - entry.getKey();
      if (!powers.containsKey(exponent)) powers.put(exponent, Rational.pow(argument, exponent));
      Rational multiplier = powers.get(exponent);
      Rational coefficient = entry.getValue();
      result = coefficient.add(result.multiply(multiplier));
      n -= exponent;
    }
    if (n > 0) {
      if (!powers.containsKey(n)) powers.put(n, Rational.pow(argument, n));
      result = result.multiply(powers.get(n));
    }
    return result;
  }

  /**
   * Differentiate a rational polynomial.
   *
   * @return Derivative of polynomial.
   */
  public Polynomial differentiate() {
    return differentiate(this);
  }

  /**
   * Differentiate a rational polynomial.
   *
   * @param antiderivative Polynomial to differentiate.
   * @return Derivative of polynomial.
   */
  public static Polynomial differentiate(final Polynomial antiderivative) {
    Polynomial derivative = new Polynomial();
    for (final Entry<Long, Rational> entry : antiderivative.coefficients.entrySet()) {
      long n = entry.getKey();
      derivative.set(n - 1, antiderivative.get(n).multiply(n));
    }
    return derivative;
  }

  /**
   * Integrate a rational polynomial.
   *
   * @return Integral of polynomial.
   */
  public Polynomial integrate() {
    return integrate(this);
  }

  /**
   * Integrate a rational polynomial.
   *
   * @param constant Constant of integration.
   * @return Integral of polynomial.
   */
  public Polynomial integrate(final Rational constant) {
    return integrate(this, constant);
  }

  /**
   * Integrate a rational polynomial.
   *
   * @param constant Constant of integration.
   * @return Integral of polynomial.
   */
  public Polynomial integrate(final long constant) {
    return integrate(this, constant);
  }

  /**
   * Integrate a rational polynomial.
   *
   * @param integrand Polynomial to integrate.
   * @return Integral of polynomial.
   */
  public static Polynomial integrate(final Polynomial integrand) {
    return integrate(integrand, Rational.ZERO);
  }

  /**
   * Integrate a rational polynomial.
   *
   * @param integrand Polynomial to integrate.
   * @param constant  Constant of integration.
   * @return Integral of polynomial.
   */
  public static Polynomial integrate(final Polynomial integrand, final Rational constant) {
    Polynomial integral = new Polynomial();
    integral.set(0, constant);
    for (final Entry<Long, Rational> entry : integrand.coefficients.entrySet()) {
      long n = entry.getKey();
      integral.set(n + 1, integrand.get(n).divide(n + 1));
    }
    return integral;
  }

  /**
   * Integrate a rational polynomial.
   *
   * @param integrand Polynomial to integrate.
   * @param constant  Constant of integration.
   * @return Integral of polynomial.
   */
  public static Polynomial integrate(final Polynomial integrand, final long constant) {
    return integrate(integrand, new Rational(constant));
  }

  /**
   * Multiply a polynomial by a rational number.
   *
   * @param multiplier Second polynomial to multiply.
   * @return Product of polynomials.
   */
  public Polynomial multiply(final Rational multiplier) {
    return multiply(this, multiplier);
  }

  /**
   * Multiply a polynomial by a rational number.
   *
   * @param multiplicand First polynomial to multiply.
   * @param multiplier   Second polynomial to multiply.
   * @return Product of polynomials.
   */
  public static Polynomial multiply(final Polynomial multiplicand, final Rational multiplier) {
    Polynomial product = new Polynomial();
    for (final Entry<Long, Rational> entry : multiplicand.coefficients.entrySet()) {
      long n = entry.getKey();
      product.set(n, multiplicand.get(n).multiply(multiplier));
    }
    return product;
  }

  /**
   * Multiply two rational polynomials.
   *
   * @param multiplier Second polynomial to multiply.
   * @return Product of polynomials.
   */
  public Polynomial multiply(final Polynomial multiplier) {
    return multiply(this, multiplier);
  }

  /**
   * Multiply two rational polynomials.
   *
   * @param multiplicand First polynomial to multiply.
   * @param multiplier   Second polynomial to multiply.
   * @return Product of polynomials.
   */
  public static Polynomial multiply(final Polynomial multiplicand, final Polynomial multiplier) {
    Polynomial product = new Polynomial();

    for (final Entry<Long, Rational> e1 : multiplicand.coefficients.entrySet()) {
      long k1 = e1.getKey();
      Rational v1 = e1.getValue();
      for (final Entry<Long, Rational> e2 : multiplier.coefficients.entrySet()) {
        long k2 = e2.getKey(), n = k1 + k2;
        Rational v2 = e2.getValue();
        product.set(n, product.get(n).add(v1.multiply(v2)));
      }
    }
    return product;
  }

  /**
   * Divide a polynomial by rational number.
   *
   * @param divisor Rational divisor.
   * @return Pair of quotient and remainder polynomials.
   */
  public Pair<Polynomial, Polynomial> divide(final Rational divisor) {
    return divide(this, divisor);
  }

  /**
   * Divide a polynomial by rational number.
   *
   * @param dividend Polynomial to divide.
   * @param divisor  Rational divisor.
   * @return Pair of quotient and remainder polynomials.
   */
  public static Pair<Polynomial, Polynomial> divide(final Polynomial dividend, final Rational divisor) {
    Polynomial quotient = new Polynomial(), remainder = new Polynomial();
    for (final Entry<Long, Rational> entry : dividend.coefficients.entrySet()) {
      long n = entry.getKey();
      quotient.set(n, dividend.get(n).divide(divisor));
    }
    return Pair.of(quotient, remainder);
  }

  /**
   * Divide two rational polynomials.
   *
   * @param divisor Polynomial divisor.
   * @return Pair of quotient and remainder polynomials.
   */
  public Pair<Polynomial, Polynomial> divide(final Polynomial divisor) {
    return divide(this, divisor);
  }

  /**
   * Divide two rational polynomials.
   *
   * @param dividend Polynomial to divide.
   * @param divisor  Polynomial divisor.
   * @return Pair of quotient and remainder polynomials.
   */
  public static Pair<Polynomial, Polynomial> divide(final Polynomial dividend, final Polynomial divisor) {
    Polynomial quotient = new Polynomial(), remainder = new Polynomial(dividend);
    long q = dividend.order() - divisor.order();

    while (q >= 0) {
      Polynomial shifted = new Polynomial(divisor).shift(q);
      Rational multiplier = remainder.get(remainder.order()).divide(shifted.get(shifted.order()));
      quotient.set(q, multiplier);
      remainder = remainder.subtract(shifted.multiply(multiplier));
      q = remainder.order() - divisor.order();
    }
    return Pair.of(quotient, remainder);
  }

  private Polynomial shift(final long m) {
    Polynomial shifted = new Polynomial();
    for (final Entry<Long, Rational> entry : coefficients.entrySet()) {
      shifted.set(entry.getKey() + m, entry.getValue());
    }
    return shifted;
  }

  /**
   * Add two rational polynomials.
   *
   * @param addend Second polynomial to add.
   * @return Sum of polynomials.
   */
  public Polynomial add(final Polynomial addend) {
    return add(this, addend);
  }

  /**
   * Add two rational polynomials.
   *
   * @param augend First polynomial to add.
   * @param addend Second polynomial to add.
   * @return Sum of polynomials.
   */
  public static Polynomial add(final Polynomial augend, final Polynomial addend) {
    Polynomial sum = new Polynomial(augend);
    for (final Entry<Long, Rational> entry : addend.coefficients.entrySet()) {
      long n = entry.getKey();
      sum.set(n, sum.get(n).add(addend.get(n)));
    }
    return sum;
  }

  /**
   * Subtract two rational polynomials.
   *
   * @param subtrahend Polynomial to subtract.
   * @return Difference of polynomials.
   */
  public Polynomial subtract(final Polynomial subtrahend) {
    return subtract(this, subtrahend);
  }

  /**
   * Subtract two rational polynomials.
   *
   * @param minuend    Polynomial to subtract from.
   * @param subtrahend Polynomial to subtract.
   * @return Difference of polynomials.
   */
  public static Polynomial subtract(final Polynomial minuend, final Polynomial subtrahend) {
    Polynomial difference = new Polynomial(minuend);
    for (final Entry<Long, Rational> entry : subtrahend.coefficients.entrySet()) {
      long n = entry.getKey();
      difference.set(n, difference.get(n).subtract(subtrahend.get(n)));
    }
    return difference;
  }

  /**
   * Negate a rational polynomial.
   *
   * @return Negation of polynomial.
   */
  public Polynomial negate() {
    Polynomial negation = new Polynomial();
    for (final Entry<Long, Rational> entry : coefficients.entrySet()) {
      negation.set(entry.getKey(), entry.getValue().negate());
    }
    return negation;
  }

  /**
   * Negate a rational polynomial.
   *
   * @param negatend Polynomial to negate.
   * @return Negation of polynomial.
   */
  public static Polynomial negate(final Polynomial negatend) {
    return negatend.negate();
  }

  public String toString() {
    return toString('x');
  }

  public String toString(final char variable) {
    if (isEmpty()) return "0";
    StringBuilder sb = new StringBuilder();
    for (final Entry<Long, Rational> entry : coefficients.entrySet()) {
      long exponent = entry.getKey();
      Rational coefficient = entry.getValue();
      char sign = coefficient.compareTo(Rational.ZERO) < 0 ? '-' : '+';
      Rational magnitude = coefficient.abs();
      StringBuilder rational = new StringBuilder();
      if (exponent == 0 || magnitude.compareTo(Rational.ONE) != 0) {
        if (exponent != order()) {
          rational.append(" ").append(sign).append(" ").append(magnitude);
        } else {
          rational.append(coefficient);
        }
      } else {
        if (exponent != order()) {
          rational.append(" ").append(sign).append(" ");
        } else if (sign == '-') {
          rational.append(sign);
        }
      }
      sb.append(rational);
      if (exponent != 0) {
        sb.append(variable); // variable
      }
      if (exponent > 1) {
        sb.append("^").append(exponent);
      }
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    Polynomial p = new Polynomial(-1L, 0L, 0L);
    System.out.println("p = " + p);

    p.set(13, 1);
    System.out.println("p = " + p);
    for (long n = 0; n < 6; ++n) {
      System.out.println("p(" + n + ") = " + p.evaluate(n));
    }
    Pair<Polynomial, Polynomial> st = divide(p, new Polynomial(-1, 1));
    Polynomial s = st.getLeft(), t = st.getRight();
    System.out.println("s = " + s);
    System.out.println("t = " + t);

    Polynomial a = new Polynomial(-1, 1);
    Polynomial b = new Polynomial(1, 1, 1, 1, 1, 1);
    System.out.println("a = " + a);
    System.out.println("b = " + b);

    Polynomial c = multiply(a, b);
    System.out.println("c = " + c);

    Pair<Polynomial, Polynomial> qr = divide(c, b);
    Polynomial q = qr.getLeft();
    Polynomial r = qr.getRight();
    System.out.println("q = " + q);
    System.out.println("r = " + r);

    Polynomial y = new Polynomial(1, 0, -1, -1, 0, 1);
    for (long n = 0; n < 6; ++n) {
      System.out.println("y(" + n + ") = " + y.evaluate(n));
    }

    Polynomial h = new Polynomial(-8, -9, -3, -1, -6, 7);
    Polynomial f = new Polynomial(-3, -6, -1, 8, -6, 3, -1, -9, -9, 3, -2, 5, 2, -2, -7, -1);
    Polynomial g = new Polynomial(24, 75, 71, -34, 3, 22, -45, 23, 245, 25, 52, 25, -67, -96, 96, 31, 55, 36, 29, -43, -7);

    System.out.println("    h = " + h);

    System.out.println("f * h = " + multiply(h, f));
    System.out.println("    g = " + g);

    Pair<Polynomial, Polynomial> gh = divide(g, h);
    Polynomial div = gh.getLeft();
    Polynomial mod = gh.getRight();

    System.out.println("    f = " + f);
    System.out.println("g / h = " + div);
    System.out.println("g % h = " + mod);

    Polynomial dg = g.differentiate();
    Polynomial idg = dg.integrate(g.get(0));
    System.out.println("    g = " + g);
    System.out.println("   dg = " + dg);
    System.out.println("  idg = " + idg);

    Polynomial ig = g.integrate();
    Polynomial dig = ig.differentiate();
    System.out.println("    g = " + g);
    System.out.println("   ig = " + ig);
    System.out.println("  dig = " + dig);
  }
}
