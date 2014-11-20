package net.euler;

import org.apache.commons.math3.util.CombinatoricsUtils;
/**
 * Created by kevin on 11/20/14.
 */
public class P015 {
  private static final int n = 20; // lattice size  (NOTE:  over 14 gives overflow errors

  private static long factorial(int n) {
    return n < 2 ? 1 : n * factorial(n - 1);
  }

  public static void main(String[] args) {
    long noRoutes = CombinatoricsUtils.binomialCoefficient(2 * n, n);

    System.out.println("There are " + noRoutes + " routes in a " + n + "×" + n + " grid.");
  }
}
