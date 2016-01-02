package net.euler.problems;

import com.google.common.collect.Maps;
import net.euler.utils.Counter;
import net.euler.utils.NewPrimes;

import java.util.Map;

/**
 * In the following equation x, y, and n are positive integers.
 *
 * 1   1   1
 * - + - = -
 * x   y   n
 *
 * For a limit L we define F(L) as the number of solutions which satisfy x < y ≤ L.
 *
 * We can verify that F(15) = 4 and F(1000) = 1069.
 * Find F(10^12).
 *
 * @author Kevin Crosby
 */
public class P454 {
  private static NewPrimes primes = NewPrimes.getInstance();

  public static void main(String[] args) {
    final long L = args.length > 0 ? Long.parseLong(args[0]) : 15;

    Counter<Long> counter = new Counter<>();
    for(long odd = 1, square = 1; square <= 100; odd += 2, square += odd) {
      if (primes.isPrime(odd)) {
        continue;
      }

    }


    for(int l = 4; l < 1000; ++l) {
    }


    //    long number = findSmallestNumber(solutionLimit);

    //    System.out.printf("The least value of n for which the number of distinct solutions exceeds %d is %d.", solutionLimit, number);
  }
}
