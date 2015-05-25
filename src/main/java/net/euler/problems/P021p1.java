package net.euler.problems;

import com.google.common.collect.Maps;
import net.euler.utils.Primes;

import java.util.Map;

/**
 * Let d(n) be defined as the sum of proper divisors of n (numbers less than n which divide evenly into n).
 * If d(a) = b and d(b) = a, where a ≠ b, then a and b are an amicable pair and each of a and b are called amicable numbers.
 *
 * For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22, 44, 55 and 110; therefore d(220) = 284.
 * The proper divisors of 284 are 1, 2, 4, 71 and 142; so d(284) = 220.
 *
 * Evaluate the sum of all the amicable numbers under 10000.
 *
 * @author Kevin Crosby
 */
public class P021p1 {
  private static Map<Integer, Integer> d = Maps.newHashMap();

  private static final Primes P = Primes.getInstance();

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 10000;
    }

    // construct d(n) = sum of proper divisors of n
    for (int i = 2; i <= limit; i++) {
      d.put(i, Long.valueOf(P.aliquotSum(i)).intValue());
    }

    // find amicable pairs d(a) = b, d(b) = a, a != b,  ... and sum
    int sum = 0;
    for (int a = 2; a <= limit; a++) {
      int b = d.get(a);
      if (b > a && b <= limit && a == d.get(b)) {
        sum += a + b;
        System.out.println("Found amicable pair (" + a + ", " + b + ")");
      }
    }

    System.out.println("The sum of all the amicable numbers under " + limit + " is " + sum);
  }
}
