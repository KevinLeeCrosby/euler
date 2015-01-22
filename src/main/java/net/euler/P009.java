package net.euler;

/**
 * A Pythagorean triplet is a set of three natural numbers, a < b < c, for which, a² + b² = c²
 *
 * For example, 3² + 4² = 9 + 16 = 25 = 5².
 *
 * There exists exactly one Pythagorean triplet for which a + b + c = 1000.
 * Find the product abc.
 *
 * @author Kevin Crosby
 */
public class P009 {
  public static void main(String[] args) {
    final int sum = args.length > 0 ? Integer.parseInt(args[0]) : 1000;

    for (int a = 1; a < sum * 0.3; a++) { // upper bound of a is sum * (2 - sqrt(2)) / 2
      for (int b = (int)Math.ceil(sum * 0.3f); b < sum * 0.42; b++) { // lower bound of b is sum * (2 - sqrt(2)) / 2 , upper bound of b is sum * (sqrt(2) - 1)
        int c = sum - a - b;
        if (a * a + b * b == c * c) {
          System.out.println("a = " + a + ", b = " + b + ", c = " + c);
          System.out.println("a + b + c = " + sum + ", a² + b² = c² = " + c * c + ", abc = " + a * b * c);
          break;
        }
      }
    }

  }
}
