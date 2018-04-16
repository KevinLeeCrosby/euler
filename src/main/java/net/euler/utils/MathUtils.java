package net.euler.utils;

import com.google.common.collect.Lists;

import java.math.BigInteger;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;
import static java.math.BigInteger.*;
import static net.euler.utils.NewPrimes.isCoprime;

/**
 * Useful math utilities.
 *
 * @author Kevin Crosby
 */
public class MathUtils {
  private static final BigInteger TWO = valueOf(2L);
  private static final long LONG_ROOT = sqrt(Long.MAX_VALUE);

  /**
   * Long/Integer common logarithm.
   *
   * @param antilogarithm Antilogarithm to take common logarithm of.
   * @return Discrete common logarithm.
   */
  public static long log10(final long antilogarithm) {
    long mantissa = 0;
    long n = antilogarithm / 10;
    while (n > 0) {
      n /= 10;
      mantissa++;
    }
    return mantissa;
  }

  /**
   * BigInteger common logarithm.
   *
   * @param antilogarithm Antilogarithm to take common logarithm of.
   * @return Discrete common logarithm.
   */
  public static BigInteger log10(final BigInteger antilogarithm) {
    BigInteger mantissa = ZERO;
    BigInteger n = antilogarithm.divide(TEN);
    while (n.compareTo(ZERO) == 1) { // i.e. n > 0
      n = n.divide(TEN);
      mantissa = mantissa.add(ONE);
    }
    return mantissa;
  }

  /**
   * Long/Integer log base 2.
   *
   * @param antilogarithm Antilogarithm to take log2 of.
   * @return Discrete logarithm base 2.
   */
  public static long log2(final long antilogarithm) {
    return 63L - Long.numberOfLeadingZeros(antilogarithm);
  }

  /**
   * BigInteger log base 2.
   *
   * @param antilogarithm Antilogarithm to take log2 of.
   * @return Discrete logarithm base 2.
   */
  public static BigInteger log2(final BigInteger antilogarithm) {
    BigInteger mantissa = ZERO;
    BigInteger n = antilogarithm.shiftRight(1); // i.e. antilogarithm / 2
    while (n.compareTo(ZERO) == 1) { // i.e. n > 0
      n = n.shiftRight(1);
      mantissa = mantissa.add(ONE);
    }
    return mantissa;
  }

  /**
   * Long/Integer log base n.
   *
   * @param antilogarithm Antilogarithm to take log base n of.
   * @param base          Base to take logarithm.
   * @return Discrete logarithm base n.
   */
  public static long logBase(final long antilogarithm, final long base) {
    if (base == 2) { return log2(antilogarithm); }
    long mantissa = 0;
    long n = antilogarithm / base;
    while (n > 0) {
      n /= base;
      mantissa++;
    }
    return mantissa;
  }

  /**
   * BigInteger log base n.
   *
   * @param antilogarithm Antilogarithm to take log base n of.
   * @param base          Base to take logarithm.
   * @return Discrete logarithm base n.
   */
  public static BigInteger logBase(final BigInteger antilogarithm, final BigInteger base) {
    if (base.compareTo(ZERO) != 1) { return null; }
    if (base.equals(TWO)) { return log2(antilogarithm); }
    BigInteger mantissa = ZERO;
    BigInteger n = antilogarithm.divide(base);
    while (n.compareTo(ZERO) == 1) { // i.e. n > 0
      n = n.divide(base);
      mantissa = mantissa.add(ONE);
    }
    return mantissa;
  }

  /**
   * Long/Integer power of 2.
   *
   * @param exponent Exponent to raise 2 to.
   * @return 2 raised to the exponent power.
   */
  public static long pow2(long exponent) {
    if (exponent < 0) { return 0; }
    return 1L << exponent;
  }

  /**
   * Long/Integer power.
   *
   * @param base     Base to take power of.
   * @param exponent Exponent to raise base to.
   * @return Base raised to the exponent power.
   */
  public static long pow(long base, long exponent) {
    if (exponent < 0) { return 0; }
    if (exponent == 0 || base == 1) { return 1; }
    if (base == 0) { return 0; }
    if (base == 2) { return pow2(exponent); }
    long product = 1;
    while (exponent > 0) {
      if ((exponent & 1) != 0) { // i.e. if odd
        product *= base;
      }
      exponent >>>= 1;
      base *= base;
    }
    return product;
  }

  /**
   * Modular exponentiation.
   *
   * @param base     Base to take power of.
   * @param exponent Exponent to raise base to.
   * @param modulus  Modulus to apply to power.
   * @return Base raised to the exponent power.
   */
  public static long modPow(long base, long exponent, final long modulus) {
    if (exponent == 0 || base == 1) { return 1; }
    if (base == 0) { return 0; }

    // use BigInteger method if in danger of overflow
    if (modulus - 1 > LONG_ROOT) {
      BigInteger product = BigInteger.valueOf(base).modPow(BigInteger.valueOf(exponent), BigInteger.valueOf(modulus));
      return product.longValue();
    }

    boolean invert = exponent < 0;
    if (invert) {
      exponent = -exponent; // TODO check coprimality of base and modulus
    }

    // long method
    long product = 1;
    base = floorMod(base, modulus); // force modulus for negative bases
    while (exponent > 0) {
      if ((exponent & 1) != 0) { // i.e. if odd
        product = floorMod(product * base, modulus);
      }
      exponent >>>= 1;
      base = floorMod(base * base, modulus);
    }
    return invert ? invMod(product, modulus) : product;
  }

  /**
   * Inverse modulus.
   *
   * @param a Base to take power of.
   * @param b Modulus to apply to power.
   * @return
   * @throws ArithmeticException {@code  m} &le; 0, if a and b are not coprime.
   */
  public static long invMod(long a, long b) {
    if (!isCoprime(a, b)) {
      throw new ArithmeticException(String.format("%d and %d are not coprime!  Have common factor %d!", a, b, gcd(a, b)));
    }
    long[] v = new long[]{b, a, 0, 1}; // r, newr, t, newt
    long quotient;

    while (v[1] != 0) {
      quotient = v[0] / v[1];
      System.arraycopy(new int[]{v[1], v[0] - quotient * v[1], v[3], v[2] - quotient * v[3]}, 0, v, 0, v.length);
    }
    if (v[0] > 1) { // shouldn't happen
      throw new ArithmeticException(String.format("%d and %d are not coprime!  Have common factor %d!", a, b, gcd(a, b)));
    }
    return floorMod(v[2], b);
  }

  private static final long goodMask = 0xC840C04048404040L; // computed below
  //{ for (int i=0; i<64; ++i) goodMask |= Long.MIN_VALUE >>> (i*i); }

  //  http://stackoverflow.com/questions/295579/fastest-way-to-determine-if-an-integers-square-root-is-an-integer

  /**
   * Check if number is a perfect square.
   *
   * @param x Number to test.
   * @return True if a perfect square, and false otherwise.
   */
  public static boolean isSquare(long x) {
    // This tests if the 6 least significant bits are right.
    // Moving the to be tested bit to the highest position saves us masking.
    if (goodMask << x >= 0) {
      return false;
    }
    final int numberOfTrailingZeros = Long.numberOfTrailingZeros(x);
    // Each square ends with an even number of zeros.
    if ((numberOfTrailingZeros & 1) != 0) {
      return false;
    }
    x >>= numberOfTrailingZeros;
    // Now x is either 0 or odd.
    // In binary each odd square ends with 001.
    // Postpone the sign test until now; handle zero in the branch.
    if ((x & 7) != 1 | x <= 0) {
      return x == 0;
    }
    // Do it in the classical way.
    // The correctness is not trivial as the conversion from long to double is lossy!
    final long tst = (long)Math.sqrt(x);
    //final long tst = sqrt(x); // replaced floating sqrt with integer sqrt.
    return tst * tst == x;
  }

  /**
   * Check if number is triangular.
   *
   * @param x Number to test.
   * @return True if triangular, and false otherwise.
   */
  public static boolean isTriangular(long x) {
    return isSquare(8 * x + 1);
  }

  /**
   * Long/Integer square root.
   *
   * @param radicand Number to take square root of.
   * @return Long/Integer square root of number.
   */
  public static long sqrt(final long radicand) {
    if (radicand == 0) { return 0; }
    long x = pow2((log2(radicand) >>> 1) + 1);
    long y = (x + radicand / x) >>> 1;
    while (y < x) {
      x = y;
      y = (x + radicand / x) >>> 1;
    }
    return x;
  }

  /**
   * BigInteger square root.
   *
   * @param radicand Number to take square root of.
   * @return Long/Integer square root of number.
   */
  public static BigInteger sqrt(final BigInteger radicand) {
    if (radicand.compareTo(ZERO) == 0) { return ZERO; }
    BigInteger x = TWO.pow(log2(radicand).shiftRight(1).add(ONE).intValue());
    BigInteger y = (x.add(radicand.divide(x))).shiftRight(1);
    while (y.compareTo(x) == -1) { // i.e. y < x
      x = y;
      y = (x.add(radicand.divide(x))).shiftRight(1);
    }
    return x;
  }

  /**
   * Factorial
   *
   * @param n Number to choose from.
   * @return n!
   */
  public static long factorial(final int n) {
    assert n >= 0 : String.format("%d must be a non-negative integer!", n);
    if (n < 2) {
      return 1;
    }
    return n * factorial(n - 1);
  }

  /**
   * Factorial
   *
   * @param n Number to choose from.
   * @return n!
   */
  public static BigInteger factorial(final long n) {
    assert n >= 0 : String.format("%d must be a non-negative integer!", n);
    if (n < 2) {
      return ONE;
    }
    return BigInteger.valueOf(n).multiply(factorial(n - 1));
  }

  /**
   * Binomial coefficient
   *
   * @param n Number to choose from.
   * @param k How many to choose.
   * @return nCk
   */
  public static long binomial(final int n, final int k) {
    if (n < k || n < 0 || k < 0) { return 0; }
    if (k == 0 || k == n) {
      return 1;
    } else {
      int j = Math.min(n - k, k);
      return binomial(n, j - 1) * (n - j + 1) / j;
    }
  }

  /**
   * Binomial coefficient
   *
   * @param n Number to choose from.
   * @param k How many to choose.
   * @return nCk
   */
  public static BigInteger binomial(final long n, final long k) {
    if (n < k || n < 0 || k < 0) { return ZERO; }
    if (k == 0 || k == n) {
      return ONE;
    } else {
      long j = Math.min(n - k, k);
      return binomial(n, j - 1).multiply(BigInteger.valueOf(n - j + 1)).divide(BigInteger.valueOf(j));
    }
  }

  /**
   * Greatest Common Divisor
   *
   * @param a First number.
   * @param b Second number.
   * @return GCD of numbers.
   */
  public static long gcd(final long a, final long b) {
    return b == 0 ? abs(a) : gcd(b, a % b);
  }

  /**
   * Greatest Common Divisor
   *
   * @param list List of numbers.
   * @return GCD of numbers.
   */
  public static long gcd(List<Long> list) {
    int length = list.size();
    switch (length) {
      case 0:
        return 0;
      case 1:
        return list.get(0);
      case 2:
        return gcd(list.get(0), list.get(1));
      default:
        return gcd(gcd(list.subList(0, length / 2)), gcd(list.subList(length / 2, length)));
    }
  }

  /**
   * Greatest Common Divisor
   *
   * @param list List of numbers.
   * @return GCD of numbers.
   */
  public static long gcd(Long... list) {
    return gcd(Lists.newArrayList(list));
  }


  /**
   * Greatest Common Divisor
   *
   * @param list List of numbers.
   * @return GCD of numbers.
   */
  public static long gcd(Integer... list) {
    return gcd(Lists.transform(Lists.newArrayList(list), Integer::longValue));
  }

  /**
   * Least Common Multiple
   *
   * @param a First number.
   * @param b Second number.
   * @return LCM of numbers.
   */
  public static long lcm(final long a, final long b) {
    return a * b / gcd(a, b);
  }

  /**
   * Least Common Multiple
   *
   * @param list List of numbers.
   * @return LCM of numbers.
   */
  public static long lcm(List<Long> list) {
    int length = list.size();
    switch (length) {
      case 0:
        return 0;
      case 1:
        return list.get(0);
      case 2:
        return lcm(list.get(0), list.get(1));
      default:
        return lcm(lcm(list.subList(0, length / 2)), lcm(list.subList(length / 2, length)));
    }
  }

  /**
   * Least Common Multiple
   *
   * @param list List of numbers.
   * @return LCM of numbers.
   */
  public static long lcm(Long... list) {
    return lcm(Lists.newArrayList(list));
  }

  /**
   * Least Common Multiple
   *
   * @param list List of numbers.
   * @return LCM of numbers.
   */
  public static long lcm(Integer... list) {
    return lcm(Lists.transform(Lists.newArrayList(list), Integer::longValue));
  }
}
