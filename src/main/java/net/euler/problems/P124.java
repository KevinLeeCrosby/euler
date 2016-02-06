package net.euler.problems;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.Map;
import java.util.Set;

/**
 * The radical of n, rad(n), is the product of the distinct prime factors of n. For example, 504 = 23 × 32 × 7, so
 * rad(504) = 2 × 3 × 7 = 42.
 *
 * If we calculate rad(n) for 1 ≤ n ≤ 10, then sort them on rad(n), and sorting on n if the radical values are equal,
 * we get:
 *
 * Let E(k) be the kth element in the sorted n column; for example, E(4) = 8 and E(6) = 9.
 *
 * If rad(n) is sorted for 1 ≤ n ≤ 100000, find E(10000).
 *
 * @author Kevin Crosby
 */
public class P124 {
  private final long limit;
  private final NewPrimes primes;
  private final Map<Long, Long> e;

  private P124(final long limit) {
    this.limit = limit;
    primes = NewPrimes.getInstance(limit);
    e = createMap();
  }

  private Map<Long, Long> createMap() {
    Set<Long> set = Sets.newTreeSet((x, y) -> (rad(x) < rad(y)) ? -1 : ((rad(x) == rad(y))) ? Long.compare(x, y) : 1);
    for(long n = 1; n <= limit; ++n) {
      set.add(n);
    }
    Map<Long, Long> map = Maps.newHashMap();
    long k = 1;
    for(final long n : set) {
      map.put(k++, n);
    }
    return map;
  }

  private Map<Long, Long> rads = Maps.newHashMap();
  private long rad(final long number) {
    if(!rads.containsKey(number)) {
      if(number == 1) {
        rads.put(number, 1L);
      } else {
        rads.put(number, primes.factor(number).stream().distinct().reduce((p, q) -> p * q).get());
      }
    }
    return rads.get(number);
  }

  private long e(final long k) {
    return e.get(k);
  }

  public static void main(String[] args) {
    long limit = args.length > 0 ? Long.parseLong(args[0]) : 100000;
    long k = args.length > 1 ? Long.parseLong(args[1]) : 10000;
    assert k <= limit : String.format("%d is larger than %d!", k, limit);

    P124 problem = new P124(limit);

    System.out.format("E(%d) is %d.\n", k, problem.e(k));
  }
}
