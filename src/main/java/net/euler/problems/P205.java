package net.euler.problems;

import java.util.Arrays;

import static java.lang.Math.min;

/**
 * Peter has nine four-sided (pyramidal) dice, each with faces numbered 1, 2, 3, 4.
 * Colin has six six-sided (cubic) dice, each with faces numbered 1, 2, 3, 4, 5, 6.
 *
 * Peter and Colin roll their dice and compare totals: the highest total wins. The result is a draw if the totals are
 * equal.
 *
 * What is the probability that Pyramidal Pete beats Cubic Colin? Give your answer rounded to seven decimal places in
 * the form 0.abcdefg
 *
 * @author Kevin Crosby
 */
public class P205 {
  private static int[] convolve(final int[] x, final int[] y) {
    int m = x.length, n = y.length;
    int[] z = new int[m + n - 1];
    for(int i = 0; i < m; ++i) {
      for(int j = 0; j < n; ++j) {
        int k = i + j;
        z[k] += x[i] * y[j];
      }
    }
    return z;
  }

  private static int[] convolve(final int[] x, int n) {
    int m = x.length;
    if(n < 0) {
      return new int[]{};
    }
    int[] y = Arrays.copyOf(x, m);
    int[] z = new int[]{1}; // Kronecker delta
    while(n > 0) {
      if((n & 1) != 0) { // i.e. if odd
        z = convolve(y, z);
      }
      n >>>= 1;
      y = convolve(y, y);
    }
    return z;
  }

  private static int[] distribution(final int noDice, final int noFaces) {
    int[] die = new int[noFaces + 1];
    Arrays.fill(die, 1, noFaces + 1, 1);
    return convolve(die, noDice);
  }

  private static double probabilityXbeatsY(final int[] x, final int[] y) {
    int m = x.length, n = y.length, p = min(m, n);
    int sumX = Arrays.stream(x).sum(), sumY = Arrays.stream(y).sum();
    double product = 0;
    for(int i = 1; i < p; ++i) {
      for(int j = 0; j < i; ++j) {
        product += x[i] * y[j];
      }
    }
    return product / sumX / sumY;
  }

  private static double probabilityPeteBeatsColin() {
    int noPyramids = 9, noPyramidFaces = 4, noCubes = 6, noCubeFaces = 6;
    int[] pyramids = distribution(noPyramids, noPyramidFaces), cubes = distribution(noCubes, noCubeFaces);
    return probabilityXbeatsY(pyramids, cubes);
  }


  public static void main(String[] args) {
    double probability = probabilityPeteBeatsColin();

    System.out.format("The probability that Pyramidal Pete beats Cubic Colin, rounded to seven decimal places in the form 0.abcdefg is %.7f", probability);
  }
}
