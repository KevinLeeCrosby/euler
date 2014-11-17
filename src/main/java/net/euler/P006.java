package net.euler;

/**
 * Created by kevin on 11/12/14.
 */
public class P006 {
  private static int squaredSum(int n) {
    int s = n * (n + 1) / 2;
    return s * s;
  }

  private static int sumSquares(int n) {
    return n * (n + 1) * (2 * n + 1) / 6;
  }

  private static int difference(int n) {
    return squaredSum(n) - sumSquares(n);
  }

  private static int sumSquareDifference(int n) {
    return n * (n - 1) * (n + 1) * (3 * n + 2) / 12;
  }

  public static void main(String[] args) {
    int n;
    if (args.length > 0) {
      n = Integer.parseInt(args[0]);
    } else {
      n = 100;
    }

    System.out.println("The difference between the sum of the squares from 1 to " + n + " and the square of the sum is " + sumSquareDifference(n));
    System.out.println("The difference between the sum of the squares from 1 to " + n + " and the square of the sum is " + difference(n));
  }
}
