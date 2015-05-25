package net.euler.problems;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;

/**
 * The square root of 2 can be written as an infinite continued fraction.
 *
 * The infinite continued fraction can be written, √2 = [1;(2)], (2) indicates that 2 repeats ad infinitum. In a
 * similar
 * way, √23 = [4;(1,3,1,8)].
 *
 * It turns out that the sequence of partial values of continued fractions for square roots provide the best rational
 * approximations. Let us consider the convergents for √2.
 *
 * Hence the sequence of the first ten convergents for √2 are:
 *
 * 1, 3/2, 7/5, 17/12, 41/29, 99/70, 239/169, 577/408, 1393/985, 3363/2378, ...
 * What is most surprising is that the important mathematical constant,
 * e = [2; 1,2,1, 1,4,1, 1,6,1 , ... , 1,2k,1, ...].
 *
 * The first ten terms in the sequence of convergents for e are:
 *
 * 2, 3, 8/3, 11/4, 19/7, 87/32, 106/39, 193/71, 1264/465, 1457/536, ...
 * The sum of digits in the numerator of the 10th convergent is 1+4+5+7=17.
 *
 * Find the sum of digits in the numerator of the 100th convergent of the continued fraction for e.
 *
 * @author Kevin Crosby
 */
public class P065 {
  private static final BigInteger TWO = BigInteger.valueOf(2L);

  public static void main(String[] args) {
    final int terms = args.length > 0 ? Integer.parseInt(args[0]) : 100;

    BigInteger nM1 = ONE, nM2 = ZERO, n = ZERO; // numerators
    BigInteger dM1 = ZERO, dM2 = ONE, d = ONE; // denominators
    BigInteger aK = TWO; // continued fraction expansion parameters for e

    BigInteger k = ONE;
    for (int term = 1; term <= terms; term++) {
      n = aK.multiply(nM1).add(nM2); // convergent = n / d
      d = aK.multiply(dM1).add(dM2); // NOTE:  each convergent is in its lowest form, so no reduction needed
      System.out.println(term + ":  " + n + (d.compareTo(ONE) == 0 ? "" : " / " + d));
      nM2 = nM1;
      nM1 = n;
      dM2 = dM1;
      dM1 = d;
      aK = ONE; // for next time
      if (term % 3 == 2) {
        aK = TWO.multiply(k);
        k = k.add(ONE);
      }
    }

    int sum = sumDigits(n);
    System.out.println("The sum of digits in the numerator of the " + terms + "th convergent of the continued fraction for e is " + sum);
  }

  private static int sumDigits(BigInteger n) {
    int sum = 0;
    while (n.compareTo(ZERO) == 1) {
      BigInteger[] divMod = n.divideAndRemainder(TEN);
      n = divMod[0];
      int digit = divMod[1].intValue();
      sum += digit;
    }

    return sum;
  }
}
