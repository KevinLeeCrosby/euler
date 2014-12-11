package net.euler;

/**
 * The fraction 49/98 is a curious fraction, as an inexperienced mathematician in attempting to simplify it may
 * incorrectly believe that 49/98 = 4/8, which is correct, is obtained by cancelling the 9s.
 *
 * We shall consider fractions like, 30/50 = 3/5, to be trivial examples.
 *
 * There are exactly four non-trivial examples of this type of fraction, less than one in value, and containing two
 * digits in the numerator and denominator.
 *
 * If the product of these four fractions is given in its lowest common terms, find the value of the denominator.
 *
 * @author Kevin Crosby
 */
public class P033 {
  public static void main(String[] args) {
    int numerator = 1, denominator = 1;

    int n = 0, d = 0;
    for (int a = 1; a < 10; a++) {
      for (int b = a + 1; b < 10; b++) {
        for (int c = 1; c < 10; c++) {
          // check ab / bc = a / c  (Other combinations cannot occur)
          n = 10 * a + b;
          d = 10 * b + c;
          if (n * c == d * a) {
            numerator *= n;
            denominator *= d;
            System.out.println(n + "/" + d + " = " + a + "/" + c);
          }
        }
      }
    }

    long gcd = MathUtils.gcd(numerator, denominator);
    denominator /= gcd;
    System.out.println("The denominator of the product of these four fractions is given in its lowest common terms, is " + denominator);
  }
}
