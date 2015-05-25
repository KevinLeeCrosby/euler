package net.euler.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
  private static BitSet sieve;

  private static final int BASE = 30;
  private static int BITS;
  private static List<Long> BASE_PRIMES;
  private static BiMap<Integer, Integer> MODULI;

  private static final long LIMIT = 341550071728321L;
  private static final long BIT_LIMIT = 100000000L;
  private static long SIEVE_LIMIT;

  private NewPrimes() {
    initialize();
    sieve = new BitSet((int) BIT_LIMIT); // all bits are initially false, let false = prime, true = composite
    generate();
  }

  public static NewPrimes getInstance() {
    if (instance == null) {
      synchronized (NewPrimes.class) {
        if (instance == null) {
          instance = new NewPrimes();
        }
      }
    }
    return instance;
  }

  private void initialize() {
    ImmutableList.Builder<Long> basePrimes = new ImmutableList.Builder<>();
    ImmutableBiMap.Builder<Integer, Integer> moduli = new ImmutableBiMap.Builder<>();
    basePrimes.add(2L);

    final int bitLimit = (BASE - 1) >> 1;
    int lastBasePrime = 2;
    BitSet modSieve = new BitSet(bitLimit); // fill with false (inverted logic), for n >= 3;
    for (int primeBit = modSieve.nextClearBit(0), prime = 3, primorial = 6; primorial <= BASE;
         primeBit = modSieve.nextClearBit(primeBit + 1), prime = (primeBit << 1) + 3, primorial *= prime) {
      basePrimes.add((long) prime);
      lastBasePrime = prime;
      for (int compositeBit = primeBit + prime; compositeBit <= bitLimit; compositeBit += prime) {
        modSieve.set(compositeBit); // set to composite
      }
    }

    moduli.put(0, 1); // NOTE: moduli are not necessarily prime in all bases
    for (int i = 1, modBit = modSieve.nextClearBit(lastBasePrime >> 1), modulus = (modBit << 1) + 3;
         modBit < bitLimit;
         ++i, modBit = modSieve.nextClearBit(modBit + 1), modulus = (modBit << 1) + 3) {
      moduli.put(i, modulus);
    }

    BASE_PRIMES = basePrimes.build();
    MODULI = moduli.build();
    BITS = MODULI.size();
    SIEVE_LIMIT = unpack(BIT_LIMIT);
  }

  /**
   * Black-Key Sieve
   *
   * See:  http://www.qsl.net/w2gl/blackkey.html
   */
  private void generate() {
    sieve.set(0); // 1 is not a prime
    long prime = unpack(1);
    for (int primeBit = sieve.nextClearBit(1); prime <= SIEVE_LIMIT / prime;
         primeBit = sieve.nextClearBit(primeBit + 1), prime = unpack(primeBit)) {
      long ratio = Long.MAX_VALUE / prime;
      long multiplier = unpack(primeBit);
      long composite = prime * multiplier;
      for (int i = 0, multiplierBit = primeBit; i < BITS && multiplier <= ratio && composite < SIEVE_LIMIT;
           ++i, multiplier = unpack(++multiplierBit), composite = prime * multiplier) { // prevent overflow
        for (long compositeBit = pack(composite); compositeBit < BIT_LIMIT; compositeBit += BITS * prime) {
          sieve.set((int) compositeBit);
        }
      }
    }
  }

  private long unpack(final long bit) {
    int mod = (int) (bit % BITS);
    long offset = bit / BITS;
    return BASE * offset + MODULI.get(mod); // number
  }

  private long pack(final long number) {
    assert isCoprime(number, BASE) : "Number " + number + " is not coprime with base " + BASE;
    int invMod = (int) (number % BASE);
    long offset = number / BASE;
    return BITS * offset + MODULI.inverse().get(invMod); // bit
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
    if (BASE_PRIMES.contains(n)) return true;
    if (n < 2 || !isCoprime(n, BASE)) return false;
    if (n <= 23) return true;
    if (n < SIEVE_LIMIT) return !sieve.get((int) pack(n));
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

  public long getLargestStoredPrime() {
    int bit = sieve.previousClearBit((int) BIT_LIMIT - 1);
    return unpack(bit);
  }

  @Override
  public Iterator<Long> iterator() {
    return new NewPrimeIterator();
  }

  private class NewPrimeIterator implements Iterator<Long> {
    private long bit;
    private int baseCount;

    public NewPrimeIterator() {
      bit = 1;
      baseCount = 0;
    }

    public boolean hasNext() {
      return bit < BIT_LIMIT; // TODO generalize
    }

    public Long next() {
      long prime;
      if (baseCount < BASE_PRIMES.size()) {
        prime = BASE_PRIMES.get(baseCount++);
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
    {
      NewPrimes primes = NewPrimes.getInstance();
      for (long number : Lists.newArrayList(105L, 10053L, 1005415L, 10054033243L)) {
        System.out.println(number + " is " + (primes.isPrime(number) ? "prime!" : "composite!"));
      }
      for (long number : Lists.newArrayList(997L, 40487L, 53471161L, 1645333507L, 188748146801L)) {
        System.out.println(number + " is " + (primes.isPrime(number) ? "prime!" : "composite!"));
      }
    }

    for (int limit : Lists.newArrayList(10, 20, 30, 15)) {
      //    for(int limit : Lists.newArrayList(1000)) {
      NewPrimes primes = NewPrimes.getInstance();
      int i = 1;
      for (long prime : primes) {
        System.out.print(prime + " ");
        if (i++ == limit) {
          System.out.println();
          break;
        }
      }
    }

    NewPrimes primes = NewPrimes.getInstance();
    int i = 1;
    for (long prime : primes) {
      if (!primes.isPrime(prime)) {
        System.err.println(prime + " is NOT prime!");
      }
      System.out.print(prime + " ");
      if (i++ % 10 == 0) {
        System.out.println();
      }
    }


    System.out.println();
    System.out.println("Largest prime stored is " + primes.getLargestStoredPrime());
  }
}
