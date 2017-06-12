package net.euler.problems;

import com.google.common.collect.Lists;
import net.euler.utils.NewPrimes;

import java.util.List;

import static net.euler.utils.MathUtils.logBase;

/**
 * A Hamming number is a positive number which has no prime factor larger than 5.
 * So the first few Hamming numbers are 1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15.
 * There are 1105 Hamming numbers not exceeding 10^8.
 *
 * We will call a positive number a generalised Hamming number of type n, if it has no prime factor larger than n.
 * Hence the Hamming numbers are the generalised Hamming numbers of type 5.
 *
 * How many generalised Hamming numbers of type 100 are there which don't exceed 10^9?
 *
 * @author Kevin Crosby
 */
public class P204 {
  private final long t;
  private final int limit;
  private final NewPrimes primes;
  private final List<Long> smooth;

  public P204(final long t, final int limit) {
    this.t = t;
    this.limit = limit;
    this.primes = NewPrimes.getInstance(t);
    this.smooth = smooth(limit);
  }

  private List<Long> smooth(final int limit) {
    List<Long> divisors = Lists.newArrayList(1L);
    for (long prime : primes) {
      if (prime > t) {
        break;
      }
      long product = 1;
      List<Long> results = Lists.newArrayList();
      for (int exponent = 1; exponent <= logBase(limit, prime); ++exponent) {
        product *= prime;
        final long finalProduct = product;
        divisors.stream()
            .map(divisor -> finalProduct * divisor)
            .filter(d -> d <= limit)
            .forEach(results::add);
      }
      divisors.addAll(results);
    }

    return divisors;
  }

  public int solve() {
    return smooth.size();
  }

  public static void main(String[] args) {
    final long t = args.length > 0 ? Long.parseLong(args[0]) : 100;
    final int limit = args.length > 1 ? Integer.parseInt(args[1]) : 1_000_000_000;

    P204 problem = new P204(t, limit);

    long solution = problem.solve();
    System.out.format("There are %d generalised Hamming numbers of type %d that don't exceed %d.\n", solution, t, limit);
  }
}
