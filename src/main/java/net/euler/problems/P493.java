package net.euler.problems;

import net.euler.utils.Rational;

import static net.euler.utils.MathUtils.binomial;

/**
 * 70 colored balls are placed in an urn, 10 for each of the seven rainbow colors.
 *
 * What is the expected number of distinct colors in 20 randomly picked balls?
 *
 * Give your answer with nine digits after the decimal point (a.bcdefghij).
 *
 * @author Kevin Crosby
 */
public class P493 {
  public static void main(String[] args) {
    int noBalls = 70, noColors = 7, noPerColor = 10, noPicked = 20;

    Rational probabilityCertainColorAbsent = new Rational(binomial(noBalls - noPerColor, noPicked), binomial(noBalls, noPicked));

    double expectedNoOfDistinctColors = noColors * (1.0 - probabilityCertainColorAbsent.doubleValue());

    System.out.format("The expected number of distinct colors in %d randomly picked balls is %.9f.\n", noPicked, expectedNoOfDistinctColors);
  }
}
