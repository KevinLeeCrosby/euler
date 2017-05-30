package net.euler.problems;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static net.euler.utils.MathUtils.pow;
import static net.euler.utils.MathUtils.sqrt;

/**
 * A number is p-smooth if it has no prime factors larger than p.
 * Let T be the sequence of triangular numbers, ie T(n)=n(n+1)/2.
 * Find the sum of all indices n such that T(n) is 47-smooth.
 *
 * @author Kevin Crosby
 */
public class P581 {
  private static final BigInteger TWO = BigInteger.valueOf(2L);
  private static final BigInteger FIVE_TRILLION = BigInteger.valueOf(5 * pow(10, 12));

  private final long p;
  private final NewPrimes primes;
  private final BiMap<Integer, Long> pi;

  public P581(final long p) {
    this.p = p;
    primes = NewPrimes.getInstance(5_000_000);
    List<Long> primes = StreamSupport.stream(this.primes.spliterator(), false)
        .collect(Collectors.toList());
    pi = IntStream.range(0, primes.size())
        .boxed()
        .collect(ImmutableBiMap.toImmutableBiMap(i -> i + 1, primes::get));
  }

  // all squarefree numbers that are P-smooth
  private Set<Long> squareFree(long p) {
    Set<Long> set = Sets.newTreeSet();
    int n = pi.inverse().get(p);

    long limit = 1 << n;
    assert limit > 0 : "Overflow error!";
    for (long b = 0; b < limit; ++b) {
      long product = 1;
      // adapted from https://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetKernighan
      for (long v = b; v > 0; v &= (v - 1)) {
        product *= pi.get(Long.numberOfTrailingZeros(v) + 1);
      }
      set.add(product);
    }
    return set;
  }

  // obtain fundamental solution to Diophantine equation
  private static Pair<BigInteger, BigInteger> chakravala(final long n) {
    long m = 1, k = 1, root = sqrt(n);
    BigInteger a0 = ONE, b0 = ZERO, a = ZERO, b = ZERO;

    while (k != 1 || b.compareTo(ZERO) == 0) {
      m = k * (m / k + 1) - m;
      m = m - ((m - root) / k) * k;

      a = (BigInteger.valueOf(m).multiply(a0).add(BigInteger.valueOf(n).multiply(b0))).divide(BigInteger.valueOf(abs(k)));
      b = (BigInteger.valueOf(m).multiply(b0).add(a0)).divide(BigInteger.valueOf(abs(k)));
      k = (m * m - n) / k;
      a0 = a;
      b0 = b;
    }
    return Pair.of(a, b);
  }

  // recurrence relationships of Pell's equations
  private static Pair<BigInteger, BigInteger> pell(final long n, final BigInteger x1, final BigInteger y1, final BigInteger xk, final BigInteger yk) {
    return Pair.of(
        x1.multiply(xk).add(BigInteger.valueOf(n).multiply(y1).multiply(yk)),
        x1.multiply(yk).add(y1.multiply(xk))
    );
  }

  private final LoadingCache<Long, Map<Long, Long>> cache = CacheBuilder.newBuilder().maximumSize(50).build(
      new CacheLoader<Long, Map<Long, Long>>() {
        @Override
        public Map<Long, Long> load(final Long n) throws Exception {
          long root = sqrt(n);
          Map<Long, Long> exponents = Maps.newHashMap();
          for (final long prime : primes) {
            if (prime > root) { break; }
            long power = prime;
            for (int exponent = 1; power <= n; power *= prime, exponent += 1) {
              exponents.merge(prime, n / power, (a, b) -> a + b);
            }
          }
          return exponents;
        }
      }
  );

  // factor n!
  private Map<Long, Long> factorFactorial(final long n) {
    try {
      return cache.get(n);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  // factor T(n) = n * (n + 1) / 2 = (n + 1) C 2
  private Multiset<Long> factorTriangle(final long n) {
    Map<Long, Long> minuend = factorFactorial(n + 1);
    Map<Long, Long> subtrahend = factorFactorial(n - 1);
    subtrahend.merge(2L, 1L, (a, b) -> a + b);

    Multiset<Long> exponents = HashMultiset.create();
    for (final long factor : minuend.keySet()) {
      int exponent = (int)(minuend.get(factor) - subtrahend.getOrDefault(factor, 0L));
      exponents.add(factor, exponent);
    }

    return exponents;
  }

  private long number(final Multiset<Long> exponents) {
    long number = 1;
    for (final Entry<Long> entry : exponents.entrySet()) {
      number *= pow(entry.getElement(), entry.getCount());
    }

    return number;
  }

  private boolean isPSmooth(final long n) {
    Multiset<Long> exponents = factorTriangle(n);
    Set<Long> factors = exponents.elementSet();
    return Sets.filter(factors, p1 -> p1 > this.p).isEmpty();
  }

  // Størmer's theorem
  private Set<Long> stormer() {
    Set<Long> solutions = Sets.newTreeSet();

    for (long q : squareFree(p)) {
      if (q == 2) { continue; }

      // fundamental solution
      Pair<BigInteger, BigInteger> chakravala = chakravala(2 * q);
      //System.out.format("%s^2 – %d×%s^2 = 1\n", chakravala.getLeft(), 2*q, chakravala.getRight());
      BigInteger x0 = chakravala.getLeft(), y0 = chakravala.getRight();
      if (x0.compareTo(FIVE_TRILLION) == 1 || y0.compareTo(FIVE_TRILLION) == 1) {
        continue;
      }
      System.out.format("%d : ", q);
      //System.out.format("%d : (%s, %s)", q, x0, y0);
      long n = x0.shiftRight(1).longValue();
      if (isPSmooth(n)) {
        System.out.format("%d, ", n);
        solutions.add(n);
      }

      // Lehmer's procedure
      BigInteger xi = x0, yi = y0;
      for (long i = 1; i < max(3, (p + 1) / 2); ++i) {
        Pair<BigInteger, BigInteger> pell = pell(2 * q, x0, y0, xi, yi);
        xi = pell.getLeft();
        yi = pell.getRight();
        if (xi.compareTo(FIVE_TRILLION) == 1 || yi.compareTo(FIVE_TRILLION) == 1) {
          break;
        }
        //System.out.format(", (%s, %s)", xi, yi);
        n = xi.shiftRight(1).longValue();
        if (isPSmooth(n)) {
          System.out.format("%d, ", n);
          solutions.add(n);
        }
      }
      System.out.println();
    }

    return solutions;
  }

  public long solve() {
    Set<Long> stormer = stormer();
    return stormer.stream().mapToLong(i -> i).sum();
  }

  public static void main(String[] args) {
    //final long p = args.length > 0 ? Long.parseLong(args[0]) : 2; // T(1), sum = 1
    //final long p = args.length > 0 ? Long.parseLong(args[0]) : 3; // T(8), sum = 14
    //final long p = args.length > 0 ? Long.parseLong(args[0]) : 5; // T(80), sum = 151
    //final long p = args.length > 0 ? Long.parseLong(args[0]) : 7; // T(4374), sum = 7543
    //final long p = args.length > 0 ? Long.parseLong(args[0]) : 11; // T(9800), sum = 22691
    //final long p = args.length > 0 ? Long.parseLong(args[0]) : 13; // T(123200), sum = 180742
    //final long p = args.length > 0 ? Long.parseLong(args[0]) : 17; // T(336140), sum = 868204
    final long p = args.length > 0 ? Long.parseLong(args[0]) : 47; // T(1109496723125), sum = 2227616372734

    P581 problem = new P581(p);

    long solution = problem.solve();
    System.out.format("The sum of all indices n such that T(n) is %d-smooth is %d.\n", p, solution);
  }
}
