package net.euler.problems;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.euler.utils.NewPrimes;
import net.euler.utils.Rational;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.euler.utils.MathUtils.gcd;

/**
 * A positive fraction whose numerator is less than its denominator is called a proper fraction. For any denominator, d,
 * there will be d−1 proper fractions; for example, with d = 12: 1/12 , 2/12 , 3/12 , 4/12 , 5/12 , 6/12 , 7/12 , 8/12 ,
 * 9/12 , 10/12 , 11/12 .
 *
 * We shall call a fraction that cannot be cancelled down a resilient fraction. Furthermore we shall define the
 * resilience of a denominator, R(d), to be the ratio of its proper fractions that are resilient; for example, R(12) =
 * 4/11 . In fact, d = 12 is the smallest denominator having a resilience R(d) < 4/10 .
 *
 * Find the smallest denominator d, having a resilience R(d) < 15499/94744 .
 *
 * @author Kevin Crosby.
 */
public class P243 {
  private static final int N = 10;
  private final Rational bound;
  private final List<Long> primes;
  private final Map<Long, Long> phi; // Euler's Totient Function

  private P243(final long numerator, final long denominator) {
    this.bound = new Rational(numerator, denominator);
    this.primes = StreamSupport.stream(NewPrimes.getInstance(denominator).spliterator(), false)
        .collect(Collectors.toList());
    this.phi = sieve();
  }

  private final List<Long> PRIMORIALS = Lists.newArrayList(1L);

  private long primorial(final int n) {
    assert n >= 0 && n <= N : "Cannot find primorial " + n + "#";
    if (PRIMORIALS.size() <= n) {
      PRIMORIALS.add(primes.get(n - 1) * primorial(n - 1));
    }
    return PRIMORIALS.get(n);
  }

  private Map<Long, Long> sieve() {
    Map<Long, Long> phi = Maps.newTreeMap();
    phi.put(1L, 1L);
    long p, primorial, totient = 1, d;
    for (int i = 1; i <= N; ++i) {
      p = primes.get(i - 1);
      phi.put(p, p - 1);
      for (long c = 2 * p; c <= N * N; c += p) {
        if (!phi.containsKey(c)) {
          phi.put(c, c);
        }
        phi.put(c, phi.get(c) / p * (p - 1));
      }
    }
    for (int i = 1; i <= N; ++i) {
      p = primes.get(i - 1);
      primorial = primorial(i); // See A060735
      totient *= p - 1;
      phi.put(primorial, totient);
      for (long m = 2, c = m * primorial; m < primes.get(i); ++m, c += primorial) {
        if (!phi.containsKey(c)) {
          d = gcd(m, primorial);
          phi.putIfAbsent(c, phi.get(m) * totient * d / phi.get(d));
        }
      }
    }
    return phi;
  }

  private long solve() {
    Rational resilience;
    long primorial;
    for (int i = 1; i < N; ++i) {
      primorial = primorial(i); // See A060735
      for (long d = primorial, j = 1; j < primes.get(i); ++j, d += primorial) {
        resilience = new Rational(phi.get(d), d - 1);
        if (resilience.compareTo(bound) < 0) {
          return d;
        }
      }
    }
    return 0;
  }

  public static void main(String[] args) {
    final long numerator = args.length > 0 ? Integer.parseInt(args[0]) : 15499; // 892371480
    final long denominator = args.length > 1 ? Integer.parseInt(args[1]) : 94744;

    P243 problem = new P243(numerator, denominator);

    long d = problem.solve();
    assert d != 0 : "SOLUTION NOT FOUND!";

    System.out.format("The smallest denominator d, having a resilience R(d) < %d/%d is %d.", numerator, denominator, d);
  }
}
