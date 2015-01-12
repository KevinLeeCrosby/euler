package net.euler;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.math.BigInteger;
import java.util.List;

import static java.math.BigInteger.*;

/**
 * Useful math utilities.
 *
 * @author Kevin Crosby
 */
public class MathUtils {
  private static final BigInteger TWO = valueOf(2L);
  private static final Long LONG_ROOT = sqrt(Long.MAX_VALUE);

  /**
   * Long/Integer common logarithm.
   *
   * @param antilogarithm Antilogarithm to take common logarithm of.
   * @return Discrete common logarithm.
   */
  public static Long log10(final long antilogarithm) {
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
  public static Long log2(final long antilogarithm) {
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
   * Modular exponentiation.
   *
   * @param base     Base to take power of.
   * @param exponent Exponent to raise base to.
   * @param modulus  Modulus to apply to power.
   * @return Base raised to the exponent power.
   */
  public static Long modPow(long base, long exponent, long modulus) {
    // use BigInteger method if in danger of overflow
    if (modulus - 1 > LONG_ROOT) {
      BigInteger product = BigInteger.valueOf(base).modPow(BigInteger.valueOf(exponent), BigInteger.valueOf(modulus));
      return product.longValue();
    }

    // long method
    long product = 1;
    base = base % modulus;
    while (exponent > 0) {
      if (exponent % 2 == 1) { // i.e. if odd
        product = (product * base) % modulus;
      }
      exponent >>= 1;
      base = (base * base) % modulus;
    }
    return product;
  }

  /**
   * Long/Integer square root.
   *
   * @param radicand Number to take square root of.
   * @return Long/Integer square root of number.
   */
  public static Long sqrt(final long radicand) {
    long x = pow(2, log2(radicand) / 2 + 1);
    long y = (x + radicand / x) / 2;
    while (y < x) {
      x = y;
      y = (x + radicand / x) / 2;
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
    BigInteger x = TWO.pow(log2(radicand).shiftRight(1).add(ONE).intValue());
    BigInteger y = (x.add(radicand.divide(x))).shiftRight(1);
    while (y.compareTo(x) == -1) { // i.e. y < x
      x = y;
      y = (x.add(radicand.divide(x))).shiftRight(1);
    }
    return x;
  }

  private static Function<Integer, Long> intToLong() {
    return new Function<Integer, Long>() {
      @Override
      public Long apply(Integer input) {
        return input.longValue();
      }
    };
  }


  /**
   * Greatest Common Divisor
   *
   * @param a First number.
   * @param b Second number.
   * @return GCD of numbers.
   */
  public static Long gcd(final long a, final long b) {
    return b == 0 ? a : gcd(b, a % b);
  }

  /**
   * Greatest Common Divisor
   *
   * @param list List of numbers.
   * @return GCD of numbers.
   */
  public static Long gcd(List<Long> list) {
    long gcd = list.get(0);
    for (int i = 1; i < list.size(); i++) {
      gcd = gcd(gcd, list.get(i));
    }
    return gcd;
  }

  /**
   * Greatest Common Divisor
   *
   * @param list List of numbers.
   * @return GCD of numbers.
   */
  public static Long gcd(Long... list) {
    return gcd(Lists.newArrayList(list));
  }


  /**
   * Greatest Common Divisor
   *
   * @param list List of numbers.
   * @return GCD of numbers.
   */
  public static Long gcd(Integer... list) {
    return gcd(Lists.transform(Lists.newArrayList(list), intToLong()));
  }

  /**
   * Least Common Multiple
   *
   * @param a First number.
   * @param b Second number.
   * @return LCM of numbers.
   */
  public static Long lcm(final long a, final long b) {
    return a * b / gcd(a, b);
  }

  /**
   * Least Common Multiple
   *
   * @param list List of numbers.
   * @return LCM of numbers.
   */
  public static Long lcm(List<Long> list) {
    long lcm = list.get(0);
    for (int i = 1; i < list.size(); i++) {
      lcm = lcm(lcm, list.get(i));
    }
    return lcm;
  }

  /**
   * Least Common Multiple
   *
   * @param list List of numbers.
   * @return LCM of numbers.
   */
  public static Long lcm(Long... list) {
    return lcm(Lists.newArrayList(list));
  }

  /**
   * Least Common Multiple
   *
   * @param list List of numbers.
   * @return LCM of numbers.
   */
  public static Long lcm(Integer... list) {
    return lcm(Lists.transform(Lists.newArrayList(list), intToLong()));
  }
}