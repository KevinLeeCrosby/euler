package net.euler;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 11/13/14.
 */
public class P012p1 {
  private static final Primes P = Primes.getInstance();

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
      noDivisors = P.countDivisors(t).intValue();
      System.out.println(n++ + "\t" + noDivisors + "\t" + t);
    } while (noDivisors <= limit);

    System.out.println("The value of the first triangle number to have over " + limit + " divisors is " + t);
  }
}
