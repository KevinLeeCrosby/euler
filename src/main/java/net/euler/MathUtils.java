package net.euler;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Useful math utilities.
 */
public class MathUtils {
  /**
   * Long/Integer log base 2.
   *
   * @param antilogarithm Antilogarithm to take log2 of.
   * @return Discrete logarithm base 2.
   */
  public static Long log2(long antilogarithm) {
    return 63L - Long.numberOfLeadingZeros(antilogarithm);
  }

  /**
   * Long/Integer power.
   *
   * @param base     Base to take power of.
   * @param exponent Exponent to raise base to.
   * @return Base raised to the exponent power.
   */
  public static Long pow(long base, long exponent) {
    long product = 1;
    while (exponent > 0) {
      if (exponent % 2 == 1) { // i.e. if odd
        product *= base;
      }
      exponent >>= 1;
      base *= base;
    }
    return product;
  }

  /**
   * Long/Integer square root.
   *
   * @param radicand Number to take square root of.
   * @return Long/Integer square root of number.
   */
  public static Long sqrt(long radicand) {
    long x = pow(2, log2(radicand) / 2 + 1);
    long y = (x + radicand / x) / 2;
    while (y < x) {
      x = y;
      y = (x + radicand / x) / 2;
    }
    return x;
  }

  /**
   * Return maximum of two numbers.
   *
   * @param first  First number to compare.
   * @param second Second number to compare.
   * @return Maximum number from comparison.
   */
  public static Long max(long first, long second) {
    return first > second ? first : second;
  }

  /**
   * Return minimum of two numbers.
   *
   * @param first  First number to compare.
   * @param second Second number to compare.
   * @return Minimum number from comparison.
   */
  public static Long min(long first, long second) {
    return max(second, first);
  }

  /**
   * Useful for multiplying two integer polynomials.
   *
   * @param multiplicand First polynomial to multiply.
   * @param multiplier   Second polynomial to multiply.
   * @return Product of polynomials.
   */
  public static List<Long> convolution(final List<Long> multiplicand, final List<Long> multiplier) {
    int len1 = multiplicand.size();
    int len2 = multiplier.size();
    int len3 = len1 + len2 - 1;
    List<Long> product = new ArrayList<>(len3);
    for (int n = 0; n < len3; n++) {
      product.set(n, 0L);
      for (int k = 0; k < max(len1, len2); k++) {
        long element = product.get(n);
        product.set(n, element + (k < len1 ? multiplicand.get(k) : 0) * (n - k < len2 ? multiplier.get(n - k) : 0));
      }
    }
    return product;
  }

  /**
   * Useful for dividing polynomials.
   *
   * @param dividend Dividend to divide.
   * @param divisor  Divisor to divide into dividend.
   * @param maxTerms Maximum number of terms to produce.
   * @return Pair of quotient and remainder as Lists
   */
  public static Pair<List<Long>, List<Long>> deconvolution(final List<Long> dividend, final List<Long> divisor, int maxTerms) {
    int len1 = dividend.size();
    int len2 = divisor.size();
    int len3 = len1 - len2 + 1;
    List<Long> quotient = new ArrayList<>(len3);
    List<Long> remainder = new ArrayList<>(len3);

    for (int n = 0; n < len3; n++) {
      quotient.set(n, dividend.get(n));
      for (int k = max(n - len2 + 1, 0).intValue(); k < n; k++) {
        long element = quotient.get(n);
        quotient.set(n, element - quotient.get(k) * divisor.get(n - k));
      }
      quotient.set(n, quotient.get(n) / divisor.get(0));
    }

    return Pair.create(quotient, remainder);
  }
//  if degree(N) ≥ degree(D) then
//    q ← 0
//    while degree(N) ≥ degree(D)
//      d ← D shifted right by (degree(N) - degree(D))
//      q(degree(N) - degree(D)) ← N(degree(N)) / d(degree(d))
//      // by construction, degree(d) = degree(N) of course
//      d ← d * q(degree(N) - degree(D))
//      N ← N - d
//    endwhile
//    r ← N
//  else
//    q ← 0
//    r ← N
//  endif
//  return (q, r)
}

