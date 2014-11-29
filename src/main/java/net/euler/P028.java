package net.euler;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Starting with the number 1 and moving to the right in a clockwise direction a 5 by 5 spiral is formed as follows:
 * <p/>
 * 21 22 23 24 25
 * 20  7  8  9 10
 * 19  6  1  2 11
 * 18  5  4  3 12
 * 17 16 15 14 13
 * <p/>
 * It can be verified that the sum of the numbers on the diagonals is 101.
 * <p/>
 * What is the sum of the numbers on the diagonals in a 1001 by 1001 spiral formed in the same way?
 */
public class P028 {
  private static long sumOfDiagonalsInSquareSpiral(int m) { // (4*m^3 + 3*m^2 + 8*m - 9) / 6, also see sequence A114254
    assert m % 2 == 1 : "Square matrix must be of odd numbered size!";
    long sum = 0;
    final List<Integer> coefficients = Lists.newArrayList(4, 3, 8, -9);
    final int denominator = 6;
    for (int coefficient : coefficients) {
      sum = sum * m + coefficient;
    }
    return sum / denominator;
  }

  public static void main(String[] args) {
    int m; // m x m matrix
    if (args.length > 0) {
      m = Integer.parseInt(args[0]) | 1; // force next higher odd number
    } else {
      m = 1001;
    }

    long sum = sumOfDiagonalsInSquareSpiral(m);
    System.out.println("The sum of the numbers on the diagonals in a " + m + " by " + m + " discrete spiral is " + sum);
  }
}
