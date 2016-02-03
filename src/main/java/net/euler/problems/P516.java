package net.euler.problems;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import net.euler.utils.NewPrimes;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static net.euler.utils.MathUtils.logBase;

/**
 * 5-smooth numbers are numbers whose largest prime factor doesn't exceed 5.
 * 5-smooth numbers are also called Hamming numbers.
 * Let S(L) be the sum of the numbers n not exceeding L such that Euler's totient function φ(n) is a Hamming number.
 * S(100)=3728.
 *
 * Find S(10^12). Give your answer modulo 2^32.
 *
 * @author Kevin Crosby
 */
public class P516 {
  private final long limit;
  private final NewPrimes primes;
  private List<Long> hamming = null;

  private P516(final long limit) {
    this.limit = limit;
    this.primes = NewPrimes.getInstance(100);
  }

  private List<Long> hamming() {
    if(hamming == null) {
      hamming = StreamSupport.stream(primes.spliterator(), false).limit(3)
          .map(factor -> LongStream.iterate(1, i -> i * factor).limit(logBase(limit, factor) + 1))
          .map(LongStream::boxed)
          .map(stream -> stream.collect(Collectors.toList()))
          .reduce(this::sortedLimitedKronecker).get()
          .stream()
          .collect(Collectors.toList());
    }
    return hamming;
  }

  private List<Long> sortedLimitedKronecker(final List<Long> as, final List<Long> bs) {
    assert Ordering.natural().isOrdered(as) && Ordering.natural().isOrdered(bs) : "Inputs not sorted!";
    List<Long> cs = Lists.newArrayList();
    for(final long b : bs) {
      for(final long a : as) {
        long product = a * b;
        if(product < a || product < b || product > limit) {
          break;
        }
        cs.add(product);
      }
    }
    Collections.sort(cs);
    return cs;
  }

  private long sum(final List<Long> as, final List<Long> bs, final long mask) {
    assert Ordering.natural().isOrdered(as) && Ordering.natural().isOrdered(bs) : "Inputs not sorted!";
    long sum = 0;
    for(final long a : as) {
      for(final long b : bs) {
        long product = a * b;
        if(product < a || product < b || product > limit) {
          break;
        }
        sum = (sum + product) & mask;
      }
    }
    return sum;
  }

  private long sum() {
    long mask = (1L << 32) - 1;
    List<Long> ps = hamming().stream()
        .filter(h -> h > 5)
        .map(h -> h + 1)
        .filter(primes::isPrime)
        .map(x -> Lists.<Long>newArrayList(1L, x))
        .map(list -> (List<Long>) list)
        .reduce(this::sortedLimitedKronecker).get()
        .stream()
        .collect(Collectors.toList());
    return sum(ps, hamming(), mask);
  }

  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 1000000000000L;

    P516 problem = new P516(limit);
    long sum = problem.sum();
    System.out.format("S(%d) modulo 2^32 is %d.\n", limit, sum);
  }
}
