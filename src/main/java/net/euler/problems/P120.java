package net.euler.problems;

import java.util.stream.IntStream;

/**
 * Let r be the remainder when (a−1)^n + (a+1)^n is divided by a^2.
 *
 * For example, if a = 7 and n = 3, then r = 42: 6^3 + 8^3 = 728 ≡ 42 mod 49. And as n varies, so too will r, but for
 * a = 7 it turns out that rmax = 42.
 *
 * For 3 ≤ a ≤ 1000, find ∑ rmax.
 *
 * @author Kevin Crosby
 */
public class P120 {
  public static void main(String[] args) {
    final int N = args.length > 0 ? Integer.parseInt(args[0]) : 1000;

    int sum = IntStream.rangeClosed(3, N).map(a -> 2 * a * ((a - 1) / 2)).sum();

    System.out.printf("For 3 <= a <= %d, sum of rmax is %d.\n", N, sum);
  }
}
