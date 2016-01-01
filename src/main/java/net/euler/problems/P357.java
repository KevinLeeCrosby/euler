package net.euler.problems;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Consider the divisors of 30: 1,2,3,5,6,10,15,30.
 * It can be seen that for every divisor d of 30, d+30/d is prime.
 *
 * Find the sum of all positive integers n not exceeding 100 000 000 such that for every divisor d of n, d+n/d is
 * prime.
 *
 * @author Kevin Crosby
 */
public class P357 {
  private static long limit;
  private static NewPrimes primes;

  private static boolean matches(final long number) {
    if(number == 1) {
      return true;
    }
    List<Long> factors = primes.factor(number);
    if(Sets.newHashSet(factors).size() < factors.size()) {
      return false;
    }
    List<Long> divisors = Lists.newArrayList(1L);
    for(final long factor : factors) {
      divisors.addAll(Lists.transform(divisors, divisor -> factor * divisor));
    }
    Collections.sort(divisors);

    int n = divisors.size();
    for(int i = 0; i < n / 2; ++i) {
      long sum = divisors.get(i) + divisors.get(n - i - 1);
      if(!primes.isPrime(sum)) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    limit = args.length > 0 ? Long.parseLong(args[0]) : 100000000;
    primes = NewPrimes.getInstance(limit);

    long sum = StreamSupport.stream(primes.spliterator(), false).mapToLong(Long::valueOf).filter(p -> p <= limit)
        .map(p -> p - 1).filter(P357::matches).sum();

    System.out.format("The sum of all positive integers n not exceeding %d such that for every divisor d of n, d+n/d is prime is %d.\n", limit, sum);
  }
}
