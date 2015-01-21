package net.euler;

/**
 * Consider the fraction, n/d, where n and d are positive integers. If n<d and HCF(n,d)=1, it is called a reduced
 * proper
 * fraction.
 *
 * If we list the set of reduced proper fractions for d ≤ 8 in ascending order of size, we get:
 *
 * 1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
 *
 * It can be seen that there are 21 elements in this set.
 *
 * How many elements would be contained in the set of reduced proper fractions for d ≤ 1,000,000?
 *
 * @author Kevin Crosby
 */
public class P072 {
  private static final Primes primes = Primes.getInstance();

  public static void main(String[] args) {
    final int d = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;
    primes.generate(d);

    long length = 0; // proper fractions count
    for (int n = 2; n <= d; n++) {
      length += primes.totient(n);
    }

    System.out.println("There are " + length + " elements contained in the set of reduced proper fractions for d ≤ " + d);
  }
}
