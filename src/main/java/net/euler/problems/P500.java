package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.Set;
import java.util.stream.StreamSupport;

import static net.euler.utils.MathUtils.pow;
import static net.euler.utils.MathUtils.sqrt;

/**
 * The number of divisors of 120 is 16.
 * In fact 120 is the smallest number having 16 divisors.
 *
 * Find the smallest number with 2^500500 divisors.
 * Give your answer modulo 500500507.
 *
 * @author Kevin Crosby
 */
public class P500 {
  private static NewPrimes primes;

  public static void main(String[] args) {
    final int exponent = args.length > 0 ? Integer.parseInt(args[0]) : 500500;
    final long modulus = args.length > 1 ? Long.parseLong(args[1]) : 500500507;
    primes = NewPrimes.getInstance(20000000);

    Set<Long> numbers = Sets.newHashSet();
    for(int limit = exponent, n = 1; limit > 1; limit = (int) sqrt(limit), n *= 2) {
      final int power = n;
      StreamSupport.stream(primes.spliterator(), false)
          .limit(limit)
          .map(p -> pow(p, power))
          .forEach(numbers::add);
    }

    long number = numbers.stream().sorted().limit(exponent).reduce((a, b) -> (a * b) % modulus).get();

    System.out.format("The smallest number with 2^%d divisors modulo %d is %d.", exponent, modulus, number);
  }
}
