package net.euler;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 11/13/14.
 */
public class P012p1 {
  private static int countDivisors(long number) {
    // Highly composite number formula
    Map<Long, Integer> count = new HashMap<>();

    Primes primes = Primes.getInstance();
    List<Long> factors = primes.factor(number);
    for (Long factor : factors) {
      int exponent = count.containsKey(factor) ? count.get(factor) : 0;
      count.put(factor, exponent + 1);
    }

    int product = 1;
    for (Map.Entry<Long, Integer> entry : count.entrySet()) {
      Long factor = entry.getKey();
      int exponent = entry.getValue();
      product *= exponent + 1;
    }
    return product;
  }

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 500;
    }

    int n = 1;
    long t = (n - 1) * n / 2; // count all previous up to this point
    int noDivisors;
    do {
      t += n;
      noDivisors = countDivisors(t);
      System.out.println(n++ + "\t" + noDivisors + "\t" + t);
    } while (noDivisors <= limit);

    System.out.println("The value of the first triangle number to have over " + limit + " divisors is " + t);
  }
}
