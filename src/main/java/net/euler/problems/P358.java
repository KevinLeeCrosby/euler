package net.euler.problems;

import net.euler.utils.NewPrimes;

import java.util.List;
import java.util.stream.Collectors;

import static net.euler.utils.MathUtils.modPow;

/**
 * A cyclic number with n digits has a very interesting property:
 * When it is multiplied by 1, 2, 3, 4, ... n, all the products have exactly the same digits, in the same order, but
 * rotated in a circular fashion!
 *
 * The smallest cyclic number is the 6-digit number 142857 :
 * 142857 × 1 = 142857
 * 142857 × 2 = 285714
 * 142857 × 3 = 428571
 * 142857 × 4 = 571428
 * 142857 × 5 = 714285
 * 142857 × 6 = 857142
 *
 * The next cyclic number is 0588235294117647 with 16 digits :
 * 0588235294117647 × 1 = 0588235294117647
 * 0588235294117647 × 2 = 1176470588235294
 * 0588235294117647 × 3 = 1764705882352941
 * ...
 * 0588235294117647 × 16 = 9411764705882352
 *
 * Note that for cyclic numbers, leading zeros are important.
 *
 * There is only one cyclic number for which, the eleven leftmost digits are 00000000137 and the five rightmost digits
 * are 56789 (i.e., it has the form 00000000137...56789 with an unknown number of digits in the middle). Find the sum
 * of all its digits.
 *
 * @author Kevin Crosby
 */
public class P358 {
  private static final NewPrimes PRIMES = NewPrimes.getInstance(1000);

  // http://math.stackexchange.com/questions/124408/finding-a-primitive-root-of-a-prime-number
  private static boolean isPrimitiveRoot(final long base, final long prime) {
    assert PRIMES.isPrime(prime);
    if(!NewPrimes.isCoprime(base, prime)) {
      return false;
    }
    final long phi = prime - 1; // Euler's totient
    List<Long> exponents = PRIMES.factor(phi).stream().distinct().map(p -> phi / p).collect(Collectors.toList());
    for(final long exponent : exponents) {
      if(modPow(base, exponent, prime) == 1) {
        return false;
      }
    }
    return true;
  }

  private static boolean isFullRepetendPrime(final long base, final long prime) {
    return PRIMES.isPrime(prime) && isPrimitiveRoot(base, prime);
  }

  private static long sum(final long base, final long prime) {
    return (prime - 1) * (base - 1) / 2;
  }

  public static void main(String[] args) {
    long mask = 100000, nines = 99999999999L;
    long inverseModulus = modPow(56789, PRIMES.totient(mask) - 1, mask); //  works if 56789 coprime to mask per Euler's theorem
    long ending = 99999 * inverseModulus % mask;
    long lowerBound = (nines / 138) / mask * mask + ending;
    long upperBound = (nines / 137) / mask * mask + ending;
    for(long m = lowerBound; m < upperBound; m += mask) { // 729809891 answer
      if(isFullRepetendPrime(10, m)) {
        long sum = sum(10, m);
        System.out.format("The sum of all the digits of 00000000137...56789 (prime %d) is %d.\n", m, sum);
        break;
      }
    }
  }
}
