package net.euler;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 11/26/14.
 */
public class P021p1 {
  private static Map<Integer, Integer> d = Maps.newHashMap();

  private static void sumProperDivisors(int number) {
    Map<Long, Integer> count = Maps.newHashMap();

    Primes bip = Primes.getInstance();
    List<Long> factors = bip.factor(number);
    for (Long factor : factors) {
      int exponent = count.containsKey(factor) ? count.get(factor) : 0;
      count.put(factor, exponent + 1);
    }

    int sum = 1;
    for (Map.Entry<Long, Integer> entry : count.entrySet()) {
      Long factor = entry.getKey();
      int exponent = entry.getValue();
      long numerator = Double.valueOf(Math.pow(factor, exponent + 1) - 1).intValue();
      long denominator = factor - 1;
      sum *= numerator / denominator;
    }
    sum -= number; // make it proper
    d.put(number, sum);
  }

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 10000;
    }

    // construct d(n) = sum of proper divisors of n
    for (int i = 2; i <= limit; i++) {
      sumProperDivisors(i);
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
