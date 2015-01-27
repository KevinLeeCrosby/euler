package net.euler;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Let p(n) represent the number of different ways in which n coins can be separated into piles. For example, five
 * coins
 * can separated into piles in exactly seven different ways, so p(5)=7.
 *
 * OOOOO
 * OOOO   O
 * OOO   OO
 * OOO   O   O
 * OO   OO   O
 * OO   O   O   O
 * O   O   O   O   O
 *
 * Find the least value of n for which p(n) is divisible by one million.
 *
 * @author Kevin Crosby
 */
public class P078 {
  private static Map<Long, BigInteger> counts = new HashMap<Long, BigInteger>() {{ put(0L, ONE); }};

  // pentagonal numbers (A000326)
  private static long g(final long k) {
    return k * (3 * k - 1) / 2;
  }

  // second pentagon numbers (A005449)
  private static long h(final long k) {
    return k * (3 * k + 1) / 2;
  }

  // partition count
  private static BigInteger p(final long n) {
    if (n < 0) { return ZERO; }
    if (!counts.containsKey(n)) {
      BigInteger count = ZERO;
      for (long k = 1; k <= n; k++) {
        count = count.add((p(n - g(k)).add(p(n - h(k)))).multiply(BigInteger.valueOf(2 * (k % 2) - 1)));
      }
      counts.put(n, count);
    }
    return counts.get(n);
  }

  public static void main(String[] args) {
    final BigInteger divisor = args.length > 0 ? new BigInteger(args[0]) : BigInteger.valueOf(1000000);

    long n = 0;
    BigInteger p = ONE;
    while (!p.mod(divisor).equals(ZERO)) {
      p = p(++n);
    }
    System.out.println("The least value of n for which p(n) is divisible by " + divisor + " is p(" + n + ") = " + p);
  }
}
