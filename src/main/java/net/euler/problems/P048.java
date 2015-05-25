package net.euler.problems;

/**
 * The series, 1^1 + 2^2 + 3^3 + ... + 10^10 = 10405071317.
 *
 * Find the last ten digits of the series, 1^1 + 2^2 + 3^3 + ... + 1000^1000.
 *
 * @author Kevin Crosby
 */
public class P048 {
  private static final long MASK = 10000000000L;

  public static Long truncatedPower(long base, long exponent) {
    long product = 1;
    for (long e = 0; e < exponent; e++) {
      product = (product * base) % MASK;
    }
    return product;
  }

  public static void main(String[] args) {
    int n;
    if (args.length > 0) {
      n = Integer.parseInt(args[0]);
    } else {
      n = 1000;
    }

    long sum = 0L;
    for (long i = 1; i <= n; i++) {
      sum = (sum + truncatedPower(i, i)) % MASK;
    }
    System.out.println("The last ten digits of the series, 1^1 + 2^2 + 3^3 + ... + " + n + "^" + n + " is " +
        String.format("%010d", sum));
  }
}
