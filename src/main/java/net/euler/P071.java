package net.euler;

/**
 * Consider the fraction, n/d, where n and d are positive integers. If n<d and HCF(n,d)=1, it is called a reduced
 * proper
 * fraction.
 *
 * If we list the set of reduced proper fractions for d ≤ 8 in ascending order of size, we get:
 *
 * 1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
 *
 * It can be seen that 2/5 is the fraction immediately to the left of 3/7.
 *
 * By listing the set of reduced proper fractions for d ≤ 1,000,000 in ascending order of size, find the numerator of
 * the fraction immediately to the left of 3/7.
 *
 * @author Kevin Crosby
 */
public class P071 {
  public static void main(String[] args) {
    final int n = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;

    // Farey sequence
    int a = 1, b = 2, c = 3, d = 7;  // i.e. 3/7 and 1/2
    int k = (n + b) / d;
    int p = k * c - a, q = k * d - b; // i.e. new fraction p/q immediately to left of 3/7.

    k = (n + p) / d;
    a = k * c - p; // i.e. new fraction a/b immediately to right of 3/7.
    b = k * d - q;

    System.out.println("Found ..., " + p + "/" + q + ", " + c + "/" + d + ", " + a + "/" + b + ", ...");

    System.out.println("For d ≤ " + n + " the numerator of the fraction immediately to the left of 3/7 is " + p);
  }
}
