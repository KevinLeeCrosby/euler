package net.euler;

/**
 * It was proposed by Christian Goldbach that every odd composite number can be written as the sum of a prime and twice a square.
 * <p/>
 * 9 = 7 + 2×1^2
 * 15 = 7 + 2×2^2
 * 21 = 3 + 2×3^2
 * 25 = 7 + 2×3^2
 * 27 = 19 + 2×2^2
 * 33 = 31 + 2×1^2
 * <p/>
 * It turns out that the conjecture was false.
 * <p/>
 * What is the smallest odd composite that cannot be written as the sum of a prime and twice a square?
 *
 * @author Kevin Crosby
 */
public class P046 {
  private static Primes primes = Primes.getInstance();

  public static void main(String[] args) {
    boolean loop = true;
    for (long odd = 3; loop; odd += 2) {
      if (primes.isPrime(odd)) {
        continue;
      }
      boolean isSum = false;
      for (int n = 1, ts = 2; ts < odd; n += 2, ts += 2*n) {
        long p = odd - ts;
        if (primes.isPrime(p)) {
          isSum = true;
          break;
        }
      }
      if (!isSum) {
        loop = false;
        System.out.println("The smallest odd composite that cannot be written as the sum of a prime and twice a square is " + odd);
      }
    }
  }
}
