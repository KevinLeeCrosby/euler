package net.euler.problems;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.euler.utils.Primes;

import java.util.HashMap;
import java.util.Map;

/**
 * It is possible to write ten as the sum of primes in exactly five different ways:
 *
 * 7 + 3
 * 5 + 5
 * 5 + 3 + 2
 * 3 + 3 + 2 + 2
 * 2 + 2 + 2 + 2 + 2
 *
 * What is the first value which can be written as the sum of primes in over five thousand different ways?
 *
 * @author Kevin Crosby
 */
public class P077 {
  private static final Primes primes = Primes.getInstance();

  private static Map<Long, Long> counts = new HashMap<Long, Long>() {{ put(0L, 1L); }};
  private static Map<Long, Long> sumFactors = Maps.newHashMap();

  // sum of factors
  private static long sumFactors(final long k) {
    if (!sumFactors.containsKey(k)) {
      long sum = 0;
      for (long factor : Sets.newHashSet(primes.factor(k))) {
        sum += factor;
      }
      sumFactors.put(k, sum);
    }
    return sumFactors.get(k);
  }

  // partition count
  private static long p(final long n) {
    if (n <= 0) { return 1; }
    if (!counts.containsKey(n)) {
      long count = sumFactors(n);  // for divisors, change to primes.sumDivisors(n)
      for (long k = 1; k < n; k++) {
        count += sumFactors(k) * p(n - k); // for divisors, change to primes.sumDivisors(n)
      }
      counts.put(n, count / n);
    }
    return counts.get(n);
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 5000;
    primes.generate(100);

    long n = 0;
    long p = 0;
    while (p <= limit) {
      p = p(++n);
    }
    System.out.println("The first value which can be written as the sum of primes in over " + limit +
        " ways is p(" + n + ") = " + p);
  }
}
