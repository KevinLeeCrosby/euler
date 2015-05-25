package net.euler.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import static net.euler.utils.MathUtils.gcd;
import static net.euler.utils.MathUtils.modPow;

/**
 * Prime number generator (64 bit) and related methods.
 */
public class NewPrimes implements Iterable<Long> {
  private static NewPrimes instance = null;
  private static final long LIMIT = 341550071728321L;
  private static final long BIT_LIMIT = Integer.MAX_VALUE >> 1;
  private static final int BASE = 30, BYTE = 8;

  private static BitSet sieve; // TODO change to LongBitSet via http://java-performance.info/bit-sets/

  // TODO:  generalize based on BASE
  private static final BiMap<Integer, Integer> MODULII = new ImmutableBiMap.Builder<Integer, Integer>()
      .put(0, 1).put(1, 7).put(2, 11).put(3, 13).put(4, 17).put(5, 19).put(6, 23).put(7, 29).build();  // base 30

  private NewPrimes() {
    sieve = new BitSet((int)BIT_LIMIT); // all bits are initially false, let false = prime, true = composite
    generate();
  }

  public static NewPrimes getInstance() {
    if(instance == null) {
      synchronized(NewPrimes.class) {
        if(instance == null) {
          instance = new NewPrimes();
        }
      }
    }
    return instance;
  }

  /**
   * Black-Key Sieve (r30)
   *
   * See:  http://www.qsl.net/w2gl/blackkey.html
   */
  private void generate() {
    sieve.set(0); // 1 is not a prime
    for(int primeBit = sieve.nextClearBit(1); primeBit < BIT_LIMIT; primeBit = sieve.nextClearBit(primeBit + 1)) {
      long prime = unpack(primeBit);
      assert isPrime(prime) : prime + " is NOT prime!";
      for(int i = 0, bit = primeBit; i < BYTE; ++i, ++bit) {
        long composite = prime * unpack(bit);
        for(long compositeBit = pack(composite); compositeBit < BIT_LIMIT; compositeBit += BYTE * prime) {
          sieve.set((int) compositeBit);
        }
      }
    }
  }

  private static long unpack(final long bit) {
    int mod = (int) (bit % BYTE);
    long offset = bit / BYTE;
    return BASE * offset + MODULII.get(mod);
  }

  private static long pack(final long number) {
    assert isCoprime(number, BASE) : "Number " + number + " is not coprime with base " + BASE;
    int invMod = (int) number % BASE;
    long offset = number / BASE;
    return BYTE * offset + MODULII.inverse().get(invMod);
  }

  /**
   * Test if a number is prime using the Miller-Rabin Primality Test, which is guaranteed to correctly distinguish
   * composites and primes up to 341,550,071,728,321 using the first 9 prime numbers.
   *
   * @param n Number to be tested.
   * @return True only if prime.
   */
  public boolean isPrime(final long n) { // TODO:  add pseudoprime checks above LIMIT???
    if (n > LIMIT) System.err.println("WARNING!  Primality check not guaranteed for number " + n);
    if (n <= 3) return n > 1;
    if (!isCoprime(n, BASE)) return false;
    if (n < BASE) return true;
    //if (sieve.get((int)pack(n))) return false; // TODO restore after debugging
    long d = n - 1;
    int s = 0;
    while (d % 2 == 0) {
      d >>= 1;
      s++;
    }
    for (final long a : this) {
      if (a > 23) break;
      if (modPow(a, d, n) != 1) {
        boolean composite = true;
        for (long r = 0, p = 1; r < s; r++, p <<= 1) { // p = 2^r
          if (modPow(a, p * d, n) == n - 1) {
            composite = false;
            break; // inconclusive
          }
        }
        if (composite) return false;
      }
    }
    return true;
  }

  public static boolean isCoprime(final long a, final long b) {
    return gcd(a, b) == 1;
  }

  public static long getLargestStoredPrime() {
    int bit = sieve.previousClearBit((int)BIT_LIMIT - 1);
    return unpack(bit);
  }

  @Override
  public Iterator<Long> iterator() {
    return new NewPrimeIterator();
  }

  private class NewPrimeIterator implements Iterator<Long> {
    private long bit;
    private int baseCount;
    private final List<Long> basePrimes = ImmutableList.of(2L, 3L, 5L); // TODO replace with factor of BASE?

    public NewPrimeIterator() {
      bit = 1;
      baseCount = 0;
    }

    public boolean hasNext() {
      return bit < BIT_LIMIT; // TODO generalize
    }

    public Long next() {
      long prime;
      if(baseCount < basePrimes.size()) {
        prime = basePrimes.get(baseCount++);
      } else {
        prime = unpack(bit);
        bit = sieve.nextClearBit((int) bit + 1);
      }
      return prime;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  public static void main(String[] args) {
    //    for(int limit : Lists.newArrayList(10, 20, 30, 15)) {
    //      //    for(int limit : Lists.newArrayList(1000)) {
    //      NewPrimes primes = NewPrimes.getInstance();
    //      int i = 1;
    //      for(long prime : primes) {
    //        System.out.print(prime + " ");
    //        if(i++ == limit) {
    //          System.out.println();
    //          break;
    //        }
    //      }
    //    }
    NewPrimes primes = NewPrimes.getInstance();
    //int i = 1;
    for(long prime : primes) {
      if (!primes.isPrime(prime)) {
        System.err.println(prime + " is NOT prime!");
      }
//      System.out.print(prime + " ");
//      if(i++ % 10 == 0) {
//        System.out.println();
//      }
    }

    System.out.println();
    System.out.println("Largest prime stored is " + getLargestStoredPrime());
  }
}
