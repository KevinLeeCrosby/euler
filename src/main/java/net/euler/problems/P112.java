package net.euler.problems;

/**
 * Working from left-to-right if no digit is exceeded by the digit to its left it is called an increasing number; for
 * example, 134468.
 *
 * Similarly if no digit is exceeded by the digit to its right it is called a decreasing number; for example, 66420.
 *
 * We shall call a positive integer that is neither increasing nor decreasing a "bouncy" number; for example, 155349.
 *
 * Clearly there cannot be any bouncy numbers below one-hundred, but just over half of the numbers below one-thousand
 * (525) are bouncy. In fact, the least number for which the proportion of bouncy numbers first reaches 50% is 538.
 *
 * Surprisingly, bouncy numbers become more and more common and by the time we reach 21780 the proportion of bouncy
 * numbers is equal to 90%.
 *
 * Find the least number for which the proportion of bouncy numbers is exactly 99%.
 *
 * @author Kevin Crosby
 */
public class P112 {
  private static boolean isBouncy(int n) {
    boolean up = false, down = false;
    int left = n % 10;
    n /= 10;
    while (n > 0) {
      int right = left;
      left = n % 10;
      n /= 10;
      if (left > right) {
        down = true;
      } else if (left < right) {
        up = true;
      }
    }
    return up && down;
  }


  public static void main(String[] args) {
    final int percent = args.length > 0 ? Integer.parseInt(args[0]) : 99;

    int n = 0, count = 0;
    while (n == 0 || 100 * count < n * percent) {
      if (isBouncy(++n)) {
        ++count;
      }
    }
    System.out.printf("The least number for which the proportion of bouncy numbers is exactly %d%% is %d.\n", percent, n);
  }
}
