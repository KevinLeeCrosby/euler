package net.euler.problems;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import net.euler.utils.Counter;
import net.euler.utils.NewPrimes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * The proper divisors of a number are all the divisors excluding the number itself. For example, the proper divisors
 * of 28 are 1, 2, 4, 7, and 14. As the sum of these divisors is equal to 28, we call it a perfect number.
 *
 * Interestingly the sum of the proper divisors of 220 is 284 and the sum of the proper divisors of 284 is 220, forming
 * a chain of two numbers. For this reason, 220 and 284 are called an amicable pair.
 *
 * Perhaps less well known are longer chains. For example, starting with 12496, we form a chain of five numbers:
 *
 * 12496 → 14288 → 15472 → 14536 → 14264 (→ 12496 → ...)
 *
 * Since this chain returns to its starting point, it is called an amicable chain.
 *
 * Find the smallest member of the longest amicable chain with no element exceeding one million.
 *
 * @author Kevin Crosby
 */
public class P095 {
  private static NewPrimes primes = NewPrimes.getInstance();
  private static final Map<Long, Long> sums = new TreeMap<Long, Long>() {{
    put(0L, 0L);
    put(1L, 0L);
  }};

  private static long s(final long number) {
    if (!sums.containsKey(number)) {
      sums.put(number, primes.aliquotSum(number));
    }
    return sums.get(number);
  }

  public static void main(String[] args) {
    final int LIMIT = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;

    Counter<Long> periods = new Counter<>();

    for (long n = 4; n <= LIMIT; ++n) {
      Set<Long> sequence = Sets.newHashSet(n);
      long s;
      for (s = s(n); s <= LIMIT && !sequence.contains(s); s = s(s)) {
        sequence.add(s);
      }
      if (s == n) {
        periods.increment(n, sequence.size());
      }
    }

    Entry<Long, Integer> entry = periods.descendingSortByCount().get(0);

    System.out.println("The smallest member of the longest amicable chain with no element exceeding " + LIMIT + " is "
        + entry.getKey() + " with order " + entry.getValue());
  }
}
