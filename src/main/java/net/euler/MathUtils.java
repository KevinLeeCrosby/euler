package net.euler;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

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

  private static Function<Integer, Long> intToLong() {
    return new Function<Integer, Long>() {
      @Override
      public Long apply(Integer input) {
        return input.longValue();
      }
    };
  }

  public static Long gcd(final long a, final long b) {
    return b == 0 ? a : gcd(b, a % b);
  }

  public static Long gcd(List<Long> list) {
    long gcd = list.get(0);
    for (int i = 1; i < list.size(); i++) {
      gcd = gcd(gcd, list.get(i));
    }
    return gcd;
  }

  public static Long gcd(Long... list) {
    return gcd(Lists.newArrayList(list));
  }


  public static Long gcd(Integer... list) {
    return gcd(Lists.transform(Lists.newArrayList(list), intToLong()));
  }

  public static Long lcm(final long a, final long b) {
    return a * b / gcd(a, b);
  }

  public static Long lcm(List<Long> list) {
    long lcm = list.get(0);
    for (int i = 1; i < list.size(); i++) {
      lcm = lcm(lcm, list.get(i));
    }
    return lcm;
  }

  public static Long lcm(Long... list) {
    return lcm(Lists.newArrayList(list));
  }

  public static Long lcm(Integer... list) {
    return lcm(Lists.transform(Lists.newArrayList(list), intToLong()));
  }
}