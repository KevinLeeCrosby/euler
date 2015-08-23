package net.euler.problems;

import net.euler.utils.MathUtils;

/**
 * Working from left-to-right if no digit is exceeded by the digit to its left it is called an increasing number; for
 * example, 134468.
 *
 * Similarly if no digit is exceeded by the digit to its right it is called a decreasing number; for example, 66420.
 *
 * We shall call a positive integer that is neither increasing nor decreasing a "bouncy" number; for example, 155349.
 *
 * As n increases, the proportion of bouncy numbers below n increases such that there are only 12951 numbers below
 * one-million that are not bouncy and only 277032 non-bouncy numbers below 10^10.
 *
 * How many numbers below a googol (10^100) are not bouncy?
 *
 * @author Kevin Crosby
 */
public class P113 {
  private static long count(final int n) {
    return MathUtils.binomial(n + 10, 10) + MathUtils.binomial(n + 9, 9) - 10 * n - 2;
  }

  public static void main(String[] args) {
    final int n = args.length > 0 ? Integer.parseInt(args[0]) : 100;

    System.out.printf("There are %s numbers below 10^%d that are not bouncy.\n", count(n), n);
  }
}
