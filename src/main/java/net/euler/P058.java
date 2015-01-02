package net.euler;

/**
 * Starting with 1 and spiralling anticlockwise in the following way, a square spiral with side length 7 is formed.
 * <p/>
 * 37 36 35 34 33 32 31
 * 38 17 16 15 14 13 30
 * 39 18  5  4  3 12 29
 * 40 19  6  1  2 11 28
 * 41 20  7  8  9 10 27
 * 42 21 22 23 24 25 26
 * 43 44 45 46 47 48 49
 * <p/>
 * It is interesting to note that the odd squares lie along the bottom right diagonal, but what is more interesting is
 * that 8 out of the 13 numbers lying along both diagonals are prime; that is, a ratio of 8/13 ≈ 62%.
 * <p/>
 * If one complete new layer is wrapped around the spiral above, a square spiral with side length 9 will be formed. If
 * this process is continued, what is the side length of the square spiral for which the ratio of primes along both
 * diagonals first falls below 10%?
 *
 * @author Kevin Crosby
 */
public class P058 {
  private static final Primes primes = Primes.getInstance();

  public static void main(String[] args) {
    final int percent = args.length > 0 ? Integer.parseInt(args[0]) : 10;
    int m = 1, diagonal = 1;
    Integer noPrimes = 0, noDiagonals = 1;
    double ratio = 1.0;

    for (int increment = 2; 100 * ratio >= percent; increment += 2) {
      m += 2;
      noDiagonals += 4;
      for (int k = 0; k < 4; k++) {
        diagonal += increment;
        if (primes.isPrime(diagonal)) {
          //System.out.println("Found prime " + diagonal);
          noPrimes++;
        }
      }
      ratio = noPrimes.doubleValue() / noDiagonals.doubleValue();
    }
    System.out.print("The side length of the square spiral for which the ratio of primes along both diagonals");
    System.out.println(" first falls below " + percent + "% is " + m);
  }
}
