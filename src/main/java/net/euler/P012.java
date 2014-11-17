package net.euler;

/**
 * Created by kevin on 11/13/14.
 */
public class P012 {
  private static int countDivisors(int t) {
    Double r = Math.sqrt(t);
    int s = r.intValue() * r.intValue();
    int count = s == t ? 1 : 0;
    for (int i = 1; i < r; i++) {
      if (t % i == 0) {
        count += 2;
      }
    }
    return count;
  }

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 500;
    }

    int n = 1;
    int t = (n - 1) * n / 2; // count all previous up to this point
    int noDivisors;
    do {
      t += n;
      noDivisors = countDivisors(t);
      System.out.println(n++ + "\t" + noDivisors + "\t" + t);
    } while (noDivisors <= limit);

    System.out.println("The value of the first triangle number to have over " + limit + " divisors is " + t);
  }
}
