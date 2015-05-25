package net.euler.problems;

import com.google.common.collect.Lists;
import net.euler.utils.Primes;

import java.util.List;

import static net.euler.utils.MathUtils.log10;
import static net.euler.utils.MathUtils.pow;

/**
 * The primes 3, 7, 109, and 673, are quite remarkable. By taking any two primes and concatenating them in any order
 * the result will always be prime. For example, taking 7 and 109, both 7109 and 1097 are prime. The sum of these
 * four primes, 792, represents the lowest sum for a set of four primes with this property.
 *
 * Find the lowest sum for a set of five primes for which any two primes concatenate to produce another prime.
 *
 * @author Kevin Crosby
 */
public class P060 {
  private static final Primes P = Primes.getInstance();
  private static List<Long> primes = Lists.newArrayList();
  private static int size;

  private static boolean isCatenatedPrimePair(final long prime1, final long prime2) {
    long len1 = log10(prime1) + 1, len2 = log10(prime2) + 1;
    long cat1 = prime1 * pow(10, len2) + prime2, cat2 = prime2 * pow(10, len1) + prime1;
    return P.isPrime(cat1) && P.isPrime(cat2);
  }

  private static boolean canAddToPath(final List<Long> set, long newPrime) {
    for (long element : set) {
      if (!isCatenatedPrimePair(element, newPrime)) return false;
    }
    return true;
  }

  private static List<Long> makePath(final List<Long> path) {
    return makePath(path, 0);
  }

  private static List<Long> makePath(final List<Long> lastPath, final int lastIndex) {
    if (lastPath.size() == size) return lastPath;
    System.out.println("Testing " + lastPath);
    for (int index = lastIndex + 1; index < primes.size(); index++) {
      long prime = primes.get(index);
      if (canAddToPath(lastPath, prime)) {
        List<Long> testPath = Lists.newArrayList(lastPath);
        testPath.add(prime);
        List<Long> path = makePath(testPath, index);
        if (!path.isEmpty()) return path;
      }
    }
    return Lists.newArrayList();
  }

  public static void main(String[] args) {
    size = args.length > 0 ? Integer.parseInt(args[0]) : 5;
    P.generate(10007L);

    for (long prime : P) {
      if (prime > 10000) break;
      if (prime == 2L || prime == 5L) continue;
      primes.add(prime);
    }

    List<Long> path = Lists.newArrayList();
    for (int i = 0; path.isEmpty() && i < primes.size(); i++) {
      path = makePath(Lists.newArrayList(primes.get(i)));
    }

    long sum = 0;
    for (long prime : path) {
      sum += prime;
    }

    System.out.println("Found " + path);
    System.out.print("The lowest sum for a set of " + size + " primes for which any two primes concatenate ");
    System.out.println("to produce another prime is " + sum);
  }
}
