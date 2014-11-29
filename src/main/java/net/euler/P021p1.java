package net.euler;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 11/26/14.
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
      d.put(i, P.aliquotSum(i).intValue());
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
