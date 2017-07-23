package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * An even positive integer N will be called admissible, if it is a power of 2 or its distinct prime factors are
 * consecutive primes.
 * The first twelve admissible numbers are 2,4,6,8,12,16,18,24,30,32,36,48.
 *
 * If N is admissible, the smallest integer M > 1 such that N+M is prime, will be called the pseudo-Fortunate number for
 * N.
 *
 * For example, N=630 is admissible since it is even and its distinct prime factors are the consecutive primes 2,3,5 and
 * 7.
 * The next prime number after 631 is 641; hence, the pseudo-Fortunate number for 630 is M=11.
 * It can also be seen that the pseudo-Fortunate number for 16 is 3.
 *
 * Find the sum of all distinct pseudo-Fortunate numbers for admissible numbers N less than 10^9.
 *
 * @author Kevin Crosby
 */
public class P293 {
  private final int limit;
  private final NewPrimes p;
  private final List<Long> primes;
  private final Set<Long> admissibles;

  private P293(int limit) {
    this.limit = limit;
    this.p = NewPrimes.getInstance(100);
    this.primes = StreamSupport.stream(p.spliterator(), false).collect(Collectors.toList());
    this.admissibles = admissibles();
  }

  private boolean isPrime(long n) {
    return p.isPrime(n);
  }

  private Set<Long> admissibles() {
    Set<Long> admissibles = Sets.newHashSet();
    admissibles(0, 1, admissibles);
    return admissibles;
  }

  private void admissibles(int i, long product, Set<Long> admissibles) {
    long prime = primes.get(i);
    for (long power = prime, prod = power * product; prod < limit; power *= prime, prod = power * product) {
      admissibles.add(prod);
      admissibles(i + 1, prod, admissibles);
    }
  }

  private long solve() {
    Set<Integer> pseudoFortunates = Sets.newHashSet();
    for (long admissible : admissibles) {
      int m = 3;
      while (!isPrime(admissible + m)) {
        m += 2;
      }
      pseudoFortunates.add(m);
    }

    return pseudoFortunates.stream()
        .mapToInt(i -> i)
        .sum();
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000_000; // 2209

    P293 problem = new P293(limit);

    long sum = problem.solve();
    System.out.format("The sum of all distinct pseudo-Fortunate numbers for admissible numbers N less than %d is %d.\n", limit, sum);
  }
}
