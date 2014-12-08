package net.euler;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.math3.util.Pair;

import java.util.List;

import static java.lang.Math.*;

/**
 * Useful polynomial utilities.
 *
 * @author Kevin Crosby
 */
public class Polynomial {
  // TODO:  find a way to make generic
  // TODO:    no statics, Polynomial<N extends Number & Comparable<? super N>>
  // TODO:    define ZERO and operators
  private List<Long> coefficients;

  public Polynomial() {
    coefficients = Lists.newArrayList(0L);
  }

  public Polynomial(final int size) {
    coefficients = Lists.newArrayList(0L);
    for (int n = 1; n < size; n++) {
      coefficients.add(0L);
    }
  }

  public Polynomial(final Integer... coefficients) {
    this(Lists.transform(Lists.newArrayList(coefficients), new Function<Integer, Long>() {
      @Override
      public Long apply(Integer input) {
        return input.longValue();
      }
    }));
  }

  public Polynomial(final Long... coefficients) {
    this(Lists.newArrayList(coefficients));
  }

  public Polynomial(final Polynomial polynomial) {
    this(polynomial.coefficients);
  }

  public Polynomial(final List<Long> coefficients) {
    this.coefficients = Lists.newArrayList(coefficients);
  }

  public int size() {
    return coefficients.size();
  }

  public Long get(final int index) {
    return index < size() ? coefficients.get(index) : 0L;
  }

  public void set(final int index, final Integer coefficient) {
    set(index, coefficient.longValue());
  }

  public void set(final int index, final Long coefficient) {
    for (int n = size(); n <= index; n++) {
      coefficients.add(0L);
    }
    coefficients.set(index, coefficient);
  }

  /**
   * Useful for multiplying two integer polynomials.
   *
   * @param multiplier Second polynomial to multiply.
   * @return Product of polynomials.
   */
  public final Polynomial multiply(final Polynomial multiplier) {
    return multiply(this, multiplier);
  }

  /**
   * Useful for multiplying two integer polynomials.
   *
   * @param multiplicand First polynomial to multiply.
   * @param multiplier   Second polynomial to multiply.
   * @return Product of polynomials.
   */
  public static Polynomial multiply(final Polynomial multiplicand, final Polynomial multiplier) {
    int len1 = multiplicand.size();
    int len2 = multiplier.size();
    int len3 = len1 + len2 - 1;
    Polynomial product = new Polynomial(len3);

    for (int n = 0; n < len3; n++) {
      for (int k = max(n - len2 + 1, 0); k <= n; k++) {
        product.set(n, product.get(n) + multiplicand.get(k) * multiplier.get(n - k));
      }
    }
    return product.trim();
  }

  /**
   * Useful for dividing polynomials.
   *
   * @param divisor Polynomial divisor.
   * @return Pair of quotient and remainder polynomials.
   */
  public final Pair<Polynomial, Polynomial> divide(final Polynomial divisor) {
    return divide(this, divisor);
  }

  /**
   * Useful for dividing polynomials.
   *
   * @param dividend Polynomial to divide.
   * @param divisor  Polynomial divisor.
   * @return Pair of quotient and remainder polynomials.
   */
  public static Pair<Polynomial, Polynomial> divide(final Polynomial dividend, final Polynomial divisor) {
    int len1 = dividend.size();
    int len2 = divisor.size();
    int len3 = len1 - len2 + 1;
    Polynomial quotient = new Polynomial(len3);

    for (int n = 0; n < len3; n++) {
      quotient.set(n, dividend.get(n));
      for (int k = max(n - len2 + 1, 0); k < n; k++) {
        quotient.set(n, quotient.get(n) - quotient.get(k) * divisor.get(n - k));
      }
      quotient.set(n, quotient.get(n) / divisor.get(0));
    }

    Polynomial remainder = dividend.minus(divisor.multiply(quotient.trim()));

    return Pair.create(quotient, remainder);
  }

  /**
   * Useful for adding polynomials.
   *
   * @param addend Second polynomial to add.
   * @return Sum of polynomials.
   */
  public final Polynomial plus(final Polynomial addend) {
    return plus(this, addend);
  }

  /**
   * Useful for adding polynomials.
   *
   * @param augend First polynomial to add.
   * @param addend Second polynomial to add.
   * @return Sum of polynomials.
   */
  public static Polynomial plus(final Polynomial augend, final Polynomial addend) {
    int len1 = augend.size();
    int len2 = addend.size();
    //int len3 = max(len1, len2);
    Polynomial sum = new Polynomial(len1 > len2 ? augend : addend);
    for (int n = 0; n < min(len1, len2); n++) {
      sum.set(n, augend.get(n) + addend.get(n));
    }

    return sum.trim();
  }

  /**
   * Useful for subtracting polynomials.
   *
   * @param subtrahend Polynomial to subtract.
   * @return Difference of polynomials.
   */
  public final Polynomial minus(final Polynomial subtrahend) {
    return minus(this, subtrahend);
  }

  /**
   * Useful for subtracting polynomials.
   *
   * @param minuend    Polynomial to subtract from.
   * @param subtrahend Polynomial to subtract.
   * @return Difference of polynomials.
   */
  public static Polynomial minus(final Polynomial minuend, final Polynomial subtrahend) {
    int len1 = minuend.size();
    int len2 = subtrahend.size();
    //int len3 = max(len1, len2);
    Polynomial difference = new Polynomial(len1 >= len2 ? minuend : negative(subtrahend));
    for (int n = 0; n < min(len1, len2); n++) {
      difference.set(n, minuend.get(n) - subtrahend.get(n));
    }

    return difference.trim();
  }

  /**
   * Useful for negating polynomials.
   *
   * @return Negation of polynomial.
   */
  public final Polynomial negative() {
    return negative(this);
  }

  /**
   * Useful for negating polynomials.
   *
   * @param negatend Polynomial to negate.
   * @return Negation of polynomial.
   */
  public static Polynomial negative(final Polynomial negatend) {
    Polynomial negation = new Polynomial(negatend).trim();
    for (int n = 0; n < negatend.size(); n++) {
      negation.set(n, -negatend.get(n));
    }

    return negation;
  }

  public Polynomial trim() {
    trim(this);
    return this;
  }

  public static void trim(Polynomial polynomial) {
    int n = polynomial.size() - 1;
    while (polynomial.get(n) == 0 && n > 0) {
      polynomial.coefficients.remove(n--);
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    long constant = get(0);
    if (constant != 0 || trim().size() == 1) {
      sb.append(constant);
    }
    for (int exponent = 1; exponent < size(); exponent++) {
      long coefficient = get(exponent);
      if (coefficient == 0L) {
        continue;
      }
      sb.append(coefficient < 0 ? '-' : '+'); // sign
      long magnitude = abs(coefficient);
      if (magnitude != 1L) {
        sb.append(magnitude);
      }
      sb.append("x"); // variable
      if (exponent != 1) {
        sb.append("^").append(exponent);
      }
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    Polynomial p = new Polynomial(1L, 0L, 0L);
    System.out.println("p = " + p);

    p.set(25, -1);
    System.out.println("p = " + p);

    Polynomial a = new Polynomial(1L, 2L, 3L);
    Polynomial b = new Polynomial(4L, 5L, 6L);
    System.out.println("a = " + a);
    System.out.println("b = " + b);

    Polynomial c = multiply(a, b);
    System.out.println("c = " + c);

    Pair<Polynomial, Polynomial> qr = divide(c, a);
    Polynomial q = qr.getFirst();
    Polynomial r = qr.getSecond();
    System.out.println("q = " + q);
    System.out.println("r = " + r);

    Polynomial h = new Polynomial(-8, -9, -3, -1, -6, 7);
    Polynomial f = new Polynomial(-3, -6, -1, 8, -6, 3, -1, -9, -9, 3, -2, 5, 2, -2, -7, -1);
    Polynomial g = new Polynomial(24, 75, 71, -34, 3, 22, -45, 23, 245, 25, 52, 25, -67, -96, 96, 31, 55, 36, 29, -43, -7);

    System.out.println("    h = " + h);

    System.out.println("f * h = " + multiply(h, f));
    System.out.println("    g = " + g);

    Pair<Polynomial, Polynomial> gh = divide(g, h);
    Polynomial div = gh.getFirst();
    Polynomial mod = gh.getSecond();

    System.out.println("    f = " + f);
    System.out.println("g / h = " + div);
    System.out.println("g % h = " + mod);
  }
}
