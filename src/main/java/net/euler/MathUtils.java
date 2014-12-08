package net.euler;

/**
 * Useful math utilities.
 *
 * @author Kevin Crosby
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
}