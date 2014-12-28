package net.euler;

/**
 * There are exactly ten ways of selecting three from five, 12345:
 * <p/>
 * 123, 124, 125, 134, 135, 145, 234, 235, 245, and 345
 * <p/>
 * In combinatorics, we use the notation, 5C3 = 10.
 * <p/>
 * In general,
 * <p/>
 * nCr =
 * n!
 * r!(n−r)!
 * ,where r ≤ n, n! = n×(n−1)×...×3×2×1, and 0! = 1.
 * It is not until n = 23, that a value exceeds one-million: 23C10 = 1144066.
 * <p/>
 * How many, not necessarily distinct, values of  nCr, for 1 ≤ n ≤ 100, are greater than one-million?
 *
 * @author Kevin Crosby
 */
public class P053 {
  public static void main(String[] args) {
    int count = 0;
    int r = 0;
    long nCr = 1;
    for (int n = 100; n > 2*r; n--) {
      nCr = nCr * (n - r + 1) / (n + 1); // NOTE:  nCr *= doesn't work for integer division
      while (nCr <= 1000000 && n > 2*r++) {
        nCr = nCr * (n - r + 1) / r; // NOTE:  nCr *= doesn't work for integer division
      }
      if (n > 2*r) count += n + 1 - 2 * r;
      //System.out.println(n + " C " + r + " = " + nCr + ", count = " + count);
    }

    System.out.println(count + " values of nCr, for 1 ≤ n ≤ 100, are greater than one-million.");
  }
}
