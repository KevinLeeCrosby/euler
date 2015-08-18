package net.euler.problems;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import static net.euler.utils.MathUtils.modPow;

/**
 * Considering 4-digit primes containing repeated digits it is clear that they cannot all be the same: 1111 is
 * divisible by 11, 2222 is divisible by 22, and so on. But there are nine 4-digit primes containing three ones:
 *
 * 1117, 1151, 1171, 1181, 1511, 1811, 2111, 4111, 8111
 *
 * We shall say that M(n, d) represents the maximum number of repeated digits for an n-digit prime where d is the
 * repeated digit, N(n, d) represents the number of such primes, and S(n, d) represents the sum of these primes.
 *
 * So M(4, 1) = 3 is the maximum number of repeated digits for a 4-digit prime where one is the repeated digit, there
 * are N(4, 1) = 9 such primes, and the sum of these primes is S(4, 1) = 22275. It turns out that for d = 0, it is only
 * possible to have M(4, 0) = 2 repeated digits, but there are N(4, 0) = 13 such cases.
 *
 * In the same way we obtain the following results for 4-digit primes.
 *
 * Digit, d 	M(4, d) 	N(4, d) 	S(4, d)
 *    0          2        13       67061
 *    1          3         9       22275
 *    2          3         1        2221
 *    3          3        12       46214
 *    4          3         2        8888
 *    5          3         1        5557
 *    6          3         1        6661
 *    7          3         9       57863
 *    8          3         1        8887
 *    9          3         7       48073
 *
 * For d = 0 to 9, the sum of all S(4, d) is 273700.
 *
 * Find the sum of all S(10, d).
 *
 * @author Kevin Crosby
 */
public class P111 {
  private static final long PRIMALITY_LIMIT = 341550071728321L;
  private static final List<Long> WITNESSES = Lists.newArrayList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L);

  /**
   * Test if a number is prime using the Miller-Rabin Primality Test, which is guaranteed to correctly distinguish
   * composites and primes up to 341,550,071,728,321 using the first 9 prime numbers.
   *
   * @param n Number to be tested.
   * @return True only if prime.
   */
  public static boolean isPrime(final long n) {
    if (n > PRIMALITY_LIMIT) {
      System.err.println("WARNING!  Primality check not guaranteed for number " + n);
    }
    if (n <= 23) {
      return WITNESSES.contains(n);
    }
    long d = n - 1;
    int s = 0;
    while (d % 2 == 0) {
      d >>>= 1;
      s++;
    }
    for (final long a : WITNESSES) {
      if (modPow(a, d, n) != 1) {
        boolean composite = true;
        for (long r = 0, p = 1; r < s; r++, p <<= 1) { // p = 2^r
          if (modPow(a, p * d, n) == n - 1) {
            composite = false;
            break; // inconclusive
          }
        }
        if (composite) {
          return false;
        }
      }
    }
    return true;
  }

  private static class Primes implements Iterable<Long> {
    private final BitSet digits;
    private final int n;
    private int k;

    public Primes(final int n) {
      this.n = n;
      k = n;
      digits = BitSet.valueOf(new long[]{(1 << 10) - 1}); // digits covered
    }

    @Override
    public Iterator<Long> iterator() {
      return new PrimeIterator();
    }

    private class PrimeIterator implements Iterator<Long> {
      private final Queue<Long> primes;

      private PrimeIterator() {
        primes = Queues.newPriorityQueue();
      }

      @Override
      public boolean hasNext() {
        return !primes.isEmpty() || !digits.isEmpty();
      }

      @Override
      public Long next() {
        if (primes.isEmpty()) {
          --k;
          for (int digit = digits.nextSetBit(0); digit >= 0; digit = digits.nextSetBit(digit + 1)) {
            for (final BitSet mask : new Mask(n, k)) {
              if (digit == 0 && mask.get(0)) continue; // skip leading zero numbers
              findPrimes(digit, mask);
            }
          }
        }
        return primes.remove();
      }

      private void findPrimes(final int digit, final BitSet mask) {
        int[] reversed = new int[n];
        for (int bit = mask.nextSetBit(0); bit >= 0; bit = mask.nextSetBit(bit + 1)) {
          reversed[bit] = digit;
        }
        for (int d = 0; d < 10; ++d) {
          if (d == digit) continue;
          if (d == 0 && !mask.get(0)) continue; // skip leading zero numbers
          int bit = mask.nextClearBit(0);
          reversed[bit] = d;
          fill(digit, mask, mask.nextClearBit(bit + 1), reversed);
        }
      }

      private void fill(final int digit, final BitSet mask, final int bit, final int[] reversed) {
        if (bit >= n) {
          long number = constructNumber(reversed);
          if (isPrime(number)) {
            digits.clear(digit);
            primes.add(number);
          }
          return;
        }
        for (int d = 0; d < 10; ++d) {
          if (d == digit) continue;
          reversed[bit] = d;
          fill(digit, mask, mask.nextClearBit(bit + 1), reversed);
        }
      }

      private long constructNumber(final int[] reversed) {
        long number = 0;
        for (final int digit : reversed) {
          number = 10 * number + digit;
        }
        return number;
      }
    }
  }

  private static class Mask implements Iterable<BitSet> {
    private final int n, k;

    public Mask(final int n, final int k) {
      this.n = n;
      this.k = k;
    }

    public Iterator<BitSet> iterator() {
      return new MaskIterator();
    }

    private class MaskIterator implements Iterator<BitSet> {
      private int bits;
      private final int limit;

      private MaskIterator() {
        assert k <= n : "Can only represent integers 1 to " + n + ".";
        bits = (1 << k) - 1;
        limit = bits << (n - k);
      }

      public boolean hasNext() {
        return bits <= limit;
      }

      // See:  http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation (Lexicographical Permutation)
      public BitSet next() {
        int oldBits = bits;
        int t = bits | (bits - 1); // t gets bit's least significant 0 bits set to 1
        // Next set to 1 the most significant bit to change,
        // set to 0 the least significant ones, and add the necessary 1 bits.
        bits = (t + 1) | ((~t & -~t) - 1) >>> (Integer.numberOfTrailingZeros(bits) + 1);
        return BitSet.valueOf(new long[]{oldBits});
      }
    }
  }

  public static void main(String[] args) {
    final int N = args.length > 0 ? Integer.parseInt(args[0]) : 10;

    long sum = 0;
    for (final long prime : new Primes(N)) {
      System.out.println(prime);
      sum += prime;
    }
    System.out.printf("The sum of all S(%d, d) is %d.", N, sum);
  }
}
