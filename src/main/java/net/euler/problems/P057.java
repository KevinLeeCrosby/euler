package net.euler.problems;

import java.math.BigInteger;

import static com.google.common.math.BigIntegerMath.log10;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.RoundingMode.FLOOR;

/**
 * It is possible to show that the square root of two can be expressed as an infinite continued fraction.
 * <p/>
 * √ 2 = 1 + 1/(2 + 1/(2 + 1/(2 + ... ))) = 1.414213...
 * <p/>
 * By expanding this for the first four iterations, we get:
 * <p/>
 * 1 + 1/2 = 3/2 = 1.5
 * 1 + 1/(2 + 1/2) = 7/5 = 1.4
 * 1 + 1/(2 + 1/(2 + 1/2)) = 17/12 = 1.41666...
 * 1 + 1/(2 + 1/(2 + 1/(2 + 1/2))) = 41/29 = 1.41379...
 * <p/>
 * The next three expansions are 99/70, 239/169, and 577/408, but the eighth expansion, 1393/985,
 * is the first example where the number of digits in the numerator exceeds the number of digits in the denominator.
 * <p/>
 * In the first one-thousand expansions, how many fractions contain a numerator with more digits than denominator?
 *
 * @author Kevin Crosby
 */
public class P057 {
  private static final BigInteger TWO = BigInteger.valueOf(2L);

  public static void main(String[] args) {
    final int expansions = args.length > 0 ? Integer.parseInt(args[0]) : 1000;

    BigInteger aK = TWO, bK = ONE; // continued fraction expansion parameters for √ 2
    BigInteger nM1 = ONE, nM2 = ZERO, n = ONE; // numerators
    BigInteger dM1 = ZERO, dM2 = ONE, d = ONE; // denominators

    //BigInteger a0 = ONE, b0 = ONE; // continued fraction expansion parameters for √ 2
    //n = a0.multiply(nM1).add(b0.multiply(nM2)); // convergent = n / d
    //d = a0.multiply(dM1).add(b0.multiply(dM2));

    int count = 0;
    for (int expansion = 0; expansion < expansions; expansion++) {
      if (log10(n, FLOOR) > log10(d, FLOOR)) { // NOTE:  # digits of N = log10(N) + 1;
        count++;
        //if (expansion < 200) System.out.println(expansion + ":  " + n + " / " + d);
      }
      nM2 = nM1;
      nM1 = n;
      dM2 = dM1;
      dM1 = d;
      n = aK.multiply(nM1).add(bK.multiply(nM2)); // convergent = n / d
      d = aK.multiply(dM1).add(bK.multiply(dM2)); // NOTE:  each convergent is in its lowest form, so no reduction needed
    }
    System.out.println("In the first " + expansions + " expansions, there are " + count
        + " fractions containing a numerator with more digits than denominator.");
  }
}
