package net.euler.problems;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static net.euler.utils.MathUtils.log10;
import static net.euler.utils.MathUtils.sqrt;

/**
 * A Harshad or Niven number is a number that is divisible by the sum of its digits.
 * 201 is a Harshad number because it is divisible by 3 (the sum of its digits.)
 * When we truncate the last digit from 201, we get 20, which is a Harshad number.
 * When we truncate the last digit from 20, we get 2, which is also a Harshad number.
 * Let's call a Harshad number that, while recursively truncating the last digit, always results in a Harshad number a
 * right truncatable Harshad number.
 *
 * Also:
 * 201/3=67 which is prime.
 * Let's call a Harshad number that, when divided by the sum of its digits, results in a prime a strong Harshad number.
 *
 * Now take the number 2011 which is prime.
 * When we truncate the last digit from it we get 201, a strong Harshad number that is also right truncatable.
 * Let's call such primes strong, right truncatable Harshad primes.
 *
 * You are given that the sum of the strong, right truncatable Harshad primes less than 10000 is 90619.
 *
 * Find the sum of the strong, right truncatable Harshad primes less than 10^14.
 *
 * @author Kevin Crosby
 */
public class P387 {
  private static NewPrimes primes;

  private static boolean isHarshad(final long number) {
    long n = number, sum = 0;
    while(n > 0) {
      sum += n % 10;
      n /= 10;
    }
    return number % sum == 0;
  }

  private static boolean isStrongHarshad(final long number) {
    long n = number, sum = 0;
    while(n > 0) {
      sum += n % 10;
      n /= 10;
    }
    return number % sum == 0 && primes.isPrime(number / sum);
  }

  public static void main(String[] args) {
    long limit = args.length > 0 ? Long.parseLong(args[0]) : 100000000000000L;
    primes = NewPrimes.getInstance(sqrt(limit));
    Multimap<Integer, Long> rightHarshad = HashMultimap.create();
    Set<Long> strongRightHarshadPrimes = Sets.newHashSet();

    int digits = 1, length = (int) log10(limit) + 1;
    rightHarshad.putAll(digits, LongStream.range(1, 10).boxed().collect(Collectors.toSet()));
    for(++digits; digits < length; ++digits) {
      for(long rh : rightHarshad.get(digits - 1)) {
        boolean isStrongRightHarshad = digits > 2 && isStrongHarshad(rh);
        for(long number = 10 * rh; number < 10 * (rh + 1); ++number) {
          if(digits != length - 1 && isHarshad(number)) {
            rightHarshad.put(digits, number);
          }
          if(isStrongRightHarshad && primes.isPrime(number)) {
            strongRightHarshadPrimes.add(number);
          }
        }
      }
    }
    long sum = strongRightHarshadPrimes.parallelStream().mapToLong(Long::valueOf).sum();

    System.out.format("The sum of the strong, right truncatable Harshad primes less than %d is %d.\n", limit, sum);
  }
}