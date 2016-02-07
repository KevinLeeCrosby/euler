package net.euler.problems;

import com.google.common.collect.ImmutableMap;
import net.euler.utils.NewPrimes;

import java.util.Map;

import static net.euler.utils.MathUtils.sqrt;

/**
 * A composite is a number containing at least two prime factors. For example, 15 = 3 × 5; 9 = 3 × 3; 12 = 2 × 2 × 3.
 *
 * There are ten composites below thirty containing precisely two, not necessarily distinct, prime factors: 4, 6, 9,
 * 10, 14, 15, 21, 22, 25, 26.
 *
 * How many composite integers, n < 10^8, have precisely two, not necessarily distinct, prime factors?
 *
 * @author Kevin Crosby
 */
public class P187 {
  private final long limit;
  private final NewPrimes primes;
  private final Map<Long, Integer> map;

  private P187(long limit) {
    this.limit = limit;
    primes = NewPrimes.getInstance(limit);
    ImmutableMap.Builder<Long, Integer> builder = new ImmutableMap.Builder<>();
    int i = 0;
    for(long prime : primes) {
      builder.put(prime, i++);
    }
    map = builder.build();
  }

  private long count() {
    long count = 0;
    for(long p : primes) {
      if(p > sqrt(limit)) {
        break;
      }
      long q = (limit / p - 1) | 1; // force odd
      while(!primes.isPrime(q)) {
        q -= 2;
      }
      int i = map.get(p), j = map.get(q);
      count += j - i + 1;
    }
    return count;
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 100000000;

    P187 problem = new P187(limit);
    long count = problem.count();

    System.out.format("There are %d composite integers, n < %d, with precisely two, not necessarily distinct, prime factors.\n", count, limit);
  }
}
