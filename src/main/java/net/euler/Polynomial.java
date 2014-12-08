package net.euler;

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

  public Polynomial(final int order) {
    coefficients = Lists.newArrayList();
    for (int n = 0; n < order; n++) {
      coefficients.add(0L);
    }
  }

  public Polynomial(final List<Long> coefficients) {
    this.coefficients = Lists.newArrayList(coefficients);
  }

  public Polynomial(final Polynomial polynomial) {
    this.coefficients = Lists.newArrayList(polynomial.coefficients);
  }

  public int size() {
    return coefficients.size();
  }

  public Long get(final int index) {
    return index < size() ? coefficients.get(index) : 0L;
  }

  public void set(final int index, final Long coefficient) {
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
    quotient.trim();

    Polynomial remainder = dividend.minus(divisor.multiply(quotient));

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
    Polynomial a = new Polynomial(Lists.newArrayList(1L, 2L, 3L));
    Polynomial b = new Polynomial(Lists.newArrayList(4L, 5L, 6L));
    System.out.println("a = " + a);
    System.out.println("b = " + b);

    Polynomial c = multiply(a, b);
    System.out.println("c = " + c);

    Pair<Polynomial, Polynomial> qr = divide(c, a);
    Polynomial q = qr.getFirst();
    Polynomial r = qr.getSecond();
    System.out.println("q = " + q);
    System.out.println("r = " + r);

    Polynomial h = new Polynomial(Lists.newArrayList(-8L, -9L, -3L, -1L, -6L, 7L));
    Polynomial f = new Polynomial(Lists.newArrayList(-3L, -6L, -1L,8L,-6L,3L,-1L,-9L,-9L,3L,-2L,5L,2L,-2L,-7L,-1L));
    Polynomial g = new Polynomial(Lists.newArrayList(24L,75L,71L,-34L,3L,22L,-45L,23L,245L,25L,52L,25L,-67L,-96L,96L,31L,55L,36L,29L,-43L,-7L));

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
