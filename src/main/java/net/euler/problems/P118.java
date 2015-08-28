package net.euler.problems;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.euler.utils.Counter;
import net.euler.utils.NewPrimes;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static net.euler.utils.NewPrimes.isCoprime;

/**
 * Using all of the digits 1 through 9 and concatenating them freely to form decimal integers, different sets can be
 * formed. Interestingly with the set {2,5,47,89,631}, all of the elements belonging to it are prime.
 *
 * How many distinct sets containing each of the digits one through nine exactly once contain only prime elements?
 *
 * @author Kevin Crosby
 */
public class P118 {
  private static final NewPrimes primes = NewPrimes.getInstance(98765431);

  private static final long GOAL = Lists.newArrayList(Iterables.limit(primes, 9)).stream().reduce((i, j) -> i * j).get();

  private static final Map<Integer, Integer> DIGIT_HASH = new ImmutableMap.Builder<Integer, Integer>() {{
    int digit = 0;
    put(digit, 0);
    for (final long prime : Iterables.limit(primes, 9)) {
      put(++digit, (int) prime);
    }
  }}.build();

  private static final Counter<Long> COUNTER = new Counter<Long>() {{
    for (final long prime : Iterables.limit(primes, 5694505)) { // upto 98765431
      long hash = hash(prime);
      if (hash != 0) {
        increment(hash);
      }
    }
  }};

  private static long hash(final long number) {
    long n = number, hash = 1;
    while (n > 0) {
      int digit = (int) n % 10, digitHash = DIGIT_HASH.get(digit);
      if (digit == 0 || !isCoprime(hash, digitHash)) return 0; // skip 0 and duplicate digits
      hash *= digitHash;
      n /= 10;
    }
    return hash;
  }

  private static long count() {
    long count = 0;
    Queue<Long> keys = Queues.newPriorityQueue(COUNTER.keySet());
    while (!keys.isEmpty()) {
      long first = keys.remove();
      List<Long> rest = keys.stream().filter(i -> isCoprime(first, i)).collect(Collectors.toList());
      count += count(COUNTER.getCount(first), first, Queues.newArrayDeque(rest));
    }
    return count;
  }

  private static long count(final long product, final long accumulator, final Queue<Long> keys) {
    if (accumulator == GOAL) {
      return product;
    }
    long count = 0;
    while (!keys.isEmpty()) {
      long first = keys.remove();
      List<Long> rest = keys.stream().filter(i -> isCoprime(first, i)).collect(Collectors.toList());
      count += count(product * COUNTER.getCount(first), first * accumulator, Queues.newArrayDeque(rest));
    }
    return count;
  }

  public static void main(String[] args) {
    long count = count();

    System.out.printf("There are %d distinct sets that contain each of the digits one through nine exactly once and contain only prime elements.", count);
  }
}
