package net.euler.problems;

import com.google.common.collect.Maps;
import net.euler.utils.Primes;

import java.util.HashMap;
import java.util.Map;

/**
 * It is possible to write five as a sum in exactly six different ways:
 *
 * 4 + 1
 * 3 + 2
 * 3 + 1 + 1
 * 2 + 2 + 1
 * 2 + 1 + 1 + 1
 * 1 + 1 + 1 + 1 + 1
 *
 * How many different ways can one hundred be written as a sum of at least two positive integers?
 *
 * @author Kevin Crosby
 */
public class P076p1 { // Alternative approach
  private static final Primes primes = Primes.getInstance();

  private static Map<Long, Long> counts = new HashMap<Long, Long>() {{ put(0L, 1L); }};
  private static Map<Long, Long> sumDivisors = Maps.newHashMap();

  // sum of divisors
  private static long sumDivisors(final long k) {
    if (!sumDivisors.containsKey(k)) {
      sumDivisors.put(k, primes.sumDivisors(k));
    }
    return sumDivisors.get(k);
  }

  // partition count
  private static long p(final long n) {
    if (n < 0) { return 0; }
    if (!counts.containsKey(n)) {
      long count = 0;
      for (long k = 0; k < n; k++) {
        count += sumDivisors(n - k) * p(k);
      }
      counts.put(n, count / n);
    }
    return counts.get(n);
  }

  public static void main(String[] args) {
    final long number = args.length > 0 ? Long.parseLong(args[0]) : 100;

    long count = p(number) - 1;  // do not include number itself
    System.out.println(number +" can be written as a sum of at least two positive integers in " + count + " ways.");
  }
}
