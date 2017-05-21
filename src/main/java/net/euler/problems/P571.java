package net.euler.problems;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.euler.utils.Permutations;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static net.euler.utils.MathUtils.gcd;

/**
 * A positive number is pandigital in base b if it contains all digits from 0 to b - 1 at least once when written in
 * base b.
 *
 * A n-super-pandigital number is a number that is simultaneously pandigital in all bases from 2 to n inclusively.
 * For example 978 = 1111010010_2 = 1100020_3 = 33102_4 = 12403_5 is the smallest 5-super-pandigital number.
 * Similarly, 1093265784 is the smallest 10-super-pandigital number.
 * The sum of the 10 smallest 10-super-pandigital numbers is 20319792309.
 *
 * What is the sum of the 10 smallest 12-super-pandigital numbers?
 *
 * @author Kevin Crosby
 */
public class P571 {
  private static final List<Integer> PRIMES = Lists.newArrayList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47);
  private static final int N = PRIMES.size();
  private static final Map<Integer, Integer> DIGIT_MAP =
      IntStream.range(0, N)
          .boxed()
          .collect(ImmutableMap.toImmutableMap(i -> i, PRIMES::get));
  private static Map<Character, Integer> CHAR_MAP =
      IntStream.range(0, N)
          .boxed()
          .collect(ImmutableMap.toImmutableMap("0123456789ABCDE"::charAt, i -> i));

  private final int base;
  private final int limit;
  private final List<Long> factorials;
  private final List<Long> primorials;

  public P571(final int base, final int limit) {
    this.base = base;
    this.limit = limit;
    factorials = Lists.newArrayList(1L);
    primorials = Lists.newArrayList(1L);
  }

  private long factorial(final int n) {
    assert n >= 0 && n <= N : String.format("Factorial %d! exceeds limit!", n);
    if (factorials.size() <= n) {
      synchronized (P571.class) {
        if (factorials.size() <= n) {
          factorials.add(n * factorial(n - 1));
        }
      }
    }
    return factorials.get(n);
  }

  private long primorial(final int n) {
    assert n >= 0 && n <= N : String.format("Primorial %d# exceeds limit!", n);
    if (primorials.size() <= n) {
      synchronized (P571.class) {
        if (primorials.size() <= n) {
          primorials.add(PRIMES.get(n - 1) * primorial(n - 1));
        }
      }
    }
    return primorials.get(n);
  }

  private boolean isPandigital(final long n, final int base) {
    long primorial = primorial(base);
    long product = 1;
    long number = n;
    boolean stop = false;
    while (number > 0 && !stop) {
      int digit = (int)(number % base);
      number /= base;
      product *= DIGIT_MAP.get(digit);
      stop = gcd(product, primorial) == primorial;
    }
    return stop;
  }

  private long pandigital(List<Character> permutation) {
    return permutation.stream()
        .mapToLong(c -> CHAR_MAP.get(c))
        .reduce(0, (a, b) -> base * a + b); // Horner's method
  }

  public long solve() {
    long sum = 0;
    int count = 0;
    List<Character> digits = Lists.newArrayList(CHAR_MAP.keySet()).subList(0, base);
    Permutations<Character> permutations = new Permutations<>(digits);
    int multiplier = base < 12 ? 1 : 2;
    for (long i = multiplier * factorial(base - 1); i < factorial(base); ++i) {
      List<Character> permutation = permutations.get(i);
      long pandigital = pandigital(permutation);
      boolean loop = true;
      for (int b = base - 1; b >= 2 && loop; --b) {
        loop = isPandigital(pandigital, b);
      }
      if (loop) {
        System.out.format("Found %d\n", pandigital);
        sum += pandigital;
        count++;
        if (count >= limit) {
          break;
        }
      }
    }
    return sum;
  }

  public static void main(String[] args) {
    final int base = args.length > 0 ? Integer.parseInt(args[0]) : 12; // 30510390701978
    final int limit = args.length > 1 ? Integer.parseInt(args[1]) : 10; //

    P571 problem = new P571(base, limit);

    long sum = problem.solve();
    System.out.format("The sum of the %d smallest %d-super-pandigital numbers is %d.\n", limit, base, sum);
  }
}
