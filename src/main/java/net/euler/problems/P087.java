package net.euler.problems;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.Map;
import java.util.Set;

import static net.euler.utils.MathUtils.*;

/**
 * The smallest number expressible as the sum of a prime square, prime cube, and prime fourth power is 28. In fact,
 * there are exactly four numbers below fifty that can be expressed in such a way:
 *
 * 28 = 2^2 + 2^3 + 2^4
 * 33 = 3^2 + 2^3 + 2^4
 * 49 = 5^2 + 2^3 + 2^4
 * 47 = 2^2 + 3^3 + 2^4
 *
 * How many numbers below fifty million can be expressed as the sum of a prime square, prime cube, and prime fourth
 * power?
 *
 * @author Kevin Crosby.
 */
public class P087 {
  private static Map<Long, Long> squares = Maps.newHashMap();
  private static Map<Long, Long> cubes = Maps.newHashMap();
  private static Map<Long, Long> tesseracts = Maps.newHashMap();

  private static long square(final long n) {
    if (!squares.containsKey(n)) {
      squares.put(n, pow(n, 2));
    }
    return squares.get(n);
  }

  private static long cube(final long n) {
    if (!cubes.containsKey(n)) {
      cubes.put(n, pow(n, 3));
    }
    return cubes.get(n);
  }

  private static long tesseract(final long n) {
    if (!tesseracts.containsKey(n)) {
      tesseracts.put(n, pow(n, 4));
    }
    return tesseracts.get(n);
  }

  public static void main(String[] args) {
    final long LIMIT = args.length > 0 ? Long.parseLong(args[0]) : 50000000L;

    final Set<Long> set = Sets.newHashSet();
    final NewPrimes primes = NewPrimes.getInstance();

    for (long s : primes) {
      long square = square(s);
      if (square >= LIMIT) break;
      for (long c : primes) {
        long cube = cube(c);
        if (square + cube >= LIMIT) break;
        for (long t : primes) {
          long tesseract = tesseract(t);
          long sum = square + cube + tesseract;
          if (sum >= LIMIT) break;
          set.add(sum);
        }
      }
    }

    int count = set.size();
    System.out.println("There are " + count + " numbers below " + LIMIT + " that can be expressed as the sum of a prime square, prime cube, and prime fourth power.");
  }
}
