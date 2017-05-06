package net.euler.problems;

import org.apache.commons.lang3.tuple.Pair;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.sqrt;

/**
 * A square is drawn around a circle as shown in the diagram below on the left.
 * We shall call the blue shaded region the L-section.
 * A line is drawn from the bottom left of the square to the top right as shown in the diagram on the right.
 * We shall call the orange shaded region a concave triangle.
 *
 * It should be clear that the concave triangle occupies exactly half of the L-section.
 *
 * Two circles are placed next to each other horizontally, a rectangle is drawn around both circles, and a line is drawn
 * from the bottom left to the top right as shown in the diagram below.
 *
 * This time the concave triangle occupies approximately 36.46% of the L-section.
 *
 * If n circles are placed next to each other horizontally, a rectangle is drawn around the n circles, and a line is
 * drawn from the bottom left to the top right, then it can be shown that the least value of n for which the concave
 * triangle occupies less than 10% of the L-section is n = 15.
 *
 * What is the least value of n for which the concave triangle occupies less than 0.1% of the L-section?
 *
 * @author Kevin Crosby
 */
public class P587 {
  public P587() {}

  public static Pair<Double, Double> quadratic(final double a, final double b, final double c) {
    double discriminant = b * b - 4 * a * c;

    assert discriminant >= 0 : "Cannot handle imaginary numbers at this time!";

    return Pair.of((-b - sqrt(discriminant)) / (2 * a), (-b + sqrt(discriminant)) / (2 * a));
  }

  public static double ratio(final int n) {
    double u = 1.0 / n; // i.e. tan phi
    double t = quadratic(u - 2, -2 * u, u).getLeft(); // i.e. tan theta
    double theta = atan(t);

    return (4 / (4 - PI)) * (t * (t + 1) / (t * t + 1) - theta);
  }

  public static int search(final double threshold) {
    int n = 0;
    while (ratio(++n) > threshold);
    return n;
  }

  public static void main(String[] args) {
    final double threshold = args.length > 0 ? Double.parseDouble(args[0]) : .001; // 2240

    int n = search(threshold);
    System.out.format("The least value of n for which the concave triangle occupies less than %.2f%% of the L-section is %d.\n", 100 * threshold, n);

  }
}
