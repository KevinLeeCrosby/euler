package net.euler.problems;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import net.euler.utils.NewPrimes;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static java.lang.Math.abs;
import static net.euler.utils.MathUtils.log10;
import static net.euler.utils.MathUtils.pow;

/**
 * Two positive numbers A and B are said to be connected (denoted by "A ↔ B") if one of these conditions holds:
 * (1) A and B have the same length and differ in exactly one digit; for example, 123 ↔ 173.
 * (2) Adding one digit to the left of A (or B) makes B (or A); for example, 23 ↔ 223 and 123 ↔ 23.
 *
 * We call a prime P a 2's relative if there exists a chain of connected primes between 2 and P and no prime in the
 * chain exceeds P.
 *
 * For example, 127 is a 2's relative. One of the possible chains is shown below:
 * 2 ↔ 3 ↔ 13 ↔ 113 ↔ 103 ↔ 107 ↔ 127
 * However, 11 and 103 are not 2's relatives.
 *
 * Let F(N) be the sum of the primes ≤ N which are not 2's relatives.
 * We can verify that F(10^3) = 431 and F(10^4) = 78728.
 *
 * Find F(10^7).
 *
 * @author Kevin Crosby
 */
public class P425 {
  private static final long INFINITY = Long.MAX_VALUE;

  private final int limitLength;
  private final Set<Long> primes;
  private final Multimap<Long, Long> connected;

  public P425(final int exponent) {
    long limit = pow(10, exponent);
    this.limitLength = exponent + 1;
    this.primes = primes(limit);
    this.connected = connected();
  }

  private Set<Long> primes(long limit) {
    return StreamSupport.stream(NewPrimes.getInstance(limit).spliterator(), false)
        .collect(ImmutableSet.toImmutableSet());
  }

  private boolean isPrime(final long number) {
    return primes.contains(number);
  }

  private void connect(final long prime, final Multimap<Long, Long> connected) {
    List<Long> digits = Lists.newArrayList(); // reversed order
    long p = prime;
    int l = 0;
    while (p > 0) {
      digits.add(p % 10);
      p /= 10;
      l++;
    }
    final int length = l;
    if (length < limitLength) {
      digits.add(0L);
    }
    long multiplier = 1;
    for (long digit : digits) {
      final long m = multiplier;
      final long mask = prime - multiplier * digit;
      LongStream.rangeClosed(0, 9)
          .filter(d -> d != digit)
          .map(d -> mask + m * d)
          .filter(this::isPrime)
          .filter(x -> abs(log10(x) + 1 - length) <= 1)
          .forEach(q -> connected.put(prime, q));
      multiplier *= 10;
    }
  }

  private Multimap<Long, Long> connected() {
    Multimap<Long, Long> connected = TreeMultimap.create();
    for (final long prime : primes) {
      connect(prime, connected);
    }
    return connected;
  }

  private Map<Long, Long> dijkstra() {
    Map<Long, Long> maximums = Maps.newHashMap();

    Queue<Entry<Long, Long>> open =
        new PriorityQueue<Entry<Long, Long>>(Comparator.comparing(Entry::getValue)) {{ add(new SimpleEntry<>(2L, 2L)); }};

    while (!open.isEmpty()) {
      Entry<Long, Long> pair = open.remove();
      long prime = pair.getKey();
      long max = pair.getValue();
      if (max < maximums.getOrDefault(prime, INFINITY)) {
        maximums.put(prime, max);
        for (long child : connected.get(prime)) {
          long nextMax = Math.max(child, max);
          if (nextMax < maximums.getOrDefault(child, INFINITY)) {
            open.add(new SimpleEntry<>(child, nextMax));
          }
        }
      }
    }
    return maximums;
  }

  private long sum() {
    Map<Long, Long> maximums = dijkstra();

    return primes.stream()
        .filter(p -> maximums.getOrDefault(p, INFINITY) > p)
        .mapToLong(l -> l)
        .sum();
  }

  public static void main(String[] args) {
    //final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 2; // 11
    //final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 3; // 431
    //final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 4; // 78728
    //final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 5; // 5134953
    //final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 6; // 532658671
    final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 7; // 46479497324

    P425 problem = new P425(exponent);

    long sum = problem.sum();

    System.out.format("F(10^%d) = %d\n", exponent, sum);
  }
}
