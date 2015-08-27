package net.euler.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static net.euler.utils.MathUtils.*;

/**
 * Prime number generator (64 bit) and related methods.
 *
 * @author Kevin Crosby
 */
public class NewPrimes implements Iterable<Long> {
  private static NewPrimes instance = null;
  private static BitSet sieve;

  private static final int BASE = 30;
  private static int BITS;
  private static List<Long> BASE_PRIMES;
  private static BiMap<Integer, Integer> MODULI;

  private static final long PRIMALITY_LIMIT = 341550071728321L;
  private static final long BIT_LIMIT = 100000000L; // up to ~375 million
  private static long SIEVE_LIMIT;

  private NewPrimes() {
    initialize();
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
    for (int i = 1, modBit = modSieve.nextClearBit(lastBasePrime >> 1), modulus = (modBit << 1) + 3; modBit < bitLimit;
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
    sieve = new BitSet((int) BIT_LIMIT); // all bits are initially false, let false = prime, true = composite
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
   * Get prime at index.
   * NOTE:  This is a O(n) method, since there is no random access to the sieve.
   *
   * @param index Index to get prime at.
   * @return Prime number at index.
   */
  public long get(final long index) {
    assert index >= 0 : "Index must be non-negative!";
    assert index < SIEVE_LIMIT : "Index is too large for existing sieve.";
    long counter = 0;
    for (final long prime : this) {
      if (counter++ == index) {
        return prime;
      }
    }
    return 0;
  }

  public long getLargestStoredPrime() {
    int bit = sieve.previousClearBit((int) BIT_LIMIT - 1);
    return unpack(bit);
  }

  /**
   * Test if a number is prime using the Miller-Rabin Primality Test, which is guaranteed to correctly distinguish
   * composites and primes up to 341,550,071,728,321 using the first 9 prime numbers.
   *
   * @param n Number to be tested.
   * @return True only if prime.
   */
  public boolean isPrime(final long n) { // TODO:  add pseudoprime checks above LIMIT???
    if (n > PRIMALITY_LIMIT) {
      System.err.println("WARNING!  Primality check not guaranteed for number " + n);
    }
    if (BASE_PRIMES.contains(n)) {
      return true;
    }
    if (n < 2 || !isCoprime(n, BASE)) {
      return false;
    }
    if (n <= 23) {
      return true;
    }
    if (n < SIEVE_LIMIT) {
      return !sieve.get((int) pack(n));
    }
    long d = n - 1;
    int s = 0;
    while (d % 2 == 0) {
      d >>= 1;
      s++;
    }
    for (final long a : Iterables.limit(this, 9)) {
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

  public static boolean isCoprime(final long a, final long b) {
    return gcd(a, b) == 1;
  }

  public boolean isPerfectCube(final long number) {
    return isPerfectPowerOf(number, 3);
  }

  public boolean isPerfectSquare(final long number) {
    return isPerfectPowerOf(number, 2);
  }

  public boolean isPerfectPowerOf(final long number, final long degree) {
    return degree > 1 && degree(number) % degree == 0;
  }

  public boolean isPerfectPower(final long number) {
    return degree(number) > 1;
  }

  public long degree(final long power) {
    assert power > 0 : "Number must be positive!";
    List<Long> factors = factor(power);
    long degree = 0;
    for (final long factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      degree = gcd(degree, exponent);
    }
    return degree;
  }

  public List<Long> factor(long number) { // TODO:  replace with Quadratic Sieve
    List<Long> factors = Lists.newArrayList();
    if (number < 2L) {
      return factors;
    }

    long root = sqrt(number);
    for (final long prime : this) { // trial division
      if (prime > root) {
        break;
      }
      while (number % prime == 0) {
        number /= prime;
        factors.add(prime);
      }
    }
    if (number > 1L) {
      factors.add(number);
    }

    return factors;
  }

  public List<Long> divisors(final long number) {
    // factor number
    List<Long> factors = factor(number);
    if (factors.isEmpty()) {
      return number == 1 ? Lists.newArrayList(1L) : Lists.<Long>newArrayList();
    }

    // construct lists of powers
    List<List<Long>> lists = Lists.newArrayList();
    for (final long factor : Sets.newHashSet(factors)) {
      List<Long> powers = Lists.newArrayList();
      for (int exponent = 1; exponent <= Collections.frequency(factors, factor); exponent++) {
        powers.add(pow(factor, exponent));
      }
      lists.add(powers);
    }

    // take Kronecker product of lists of powers
    List<Long> divisors = Lists.newArrayList(1L);
    for (List<Long> powers : lists) {
      List<Long> products = Lists.newArrayList();
      for (final long power : powers) {
        for (final long divisor : divisors) {
          products.add(power * divisor);
        }
      }
      divisors.addAll(products);
    }
    Collections.sort(divisors);
    return divisors;
  }

  public long countDivisors(final long number) { // Highly composite number formula
    if (number < 1L) {
      return 0L;
    }
    long count = 1;
    List<Long> factors = factor(number);
    for (final long factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      count *= exponent + 1;
    }
    return count;
  }

  public long sumDivisors(final long number) {
    if (number < 1L) {
      return 0L;
    }
    long sum = 1;
    List<Long> factors = factor(number);
    for (final long factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      long numerator = pow(factor, exponent + 1) - 1;
      long denominator = factor - 1;
      sum *= numerator / denominator;
    }
    return sum;
  }

  public long aliquotSum(final long number) { // a.k.a. sum proper divisors
    return sumDivisors(number) - number; // make it proper
  }

  public boolean isPerfectNumber(final long number) {
    return aliquotSum(number) == number;
  }

  public boolean isAlmostPerfectNumber(final long number) {
    return aliquotSum(number) == number - 1;
  }

  public boolean isAbundantNumber(final long number) {
    return aliquotSum(number) > number;
  }

  public boolean isDeficientNumber(final long number) {
    return aliquotSum(number) < number;
  }

  /**
   * Euler's totient or phi function, φ(n), is an arithmetic function that counts the totatives of n, that is, the
   * positive integers less than or equal to n that are relatively prime to n. Thus, if n is a positive integer, then
   * φ(n) is the number of integers k in the range 1 ≤ k ≤ n for which the greatest common divisor gcd(n, k) = 1.
   *
   * @param number Positive whole number to take totient of.
   * @return Euler's totient.
   */
  public long totient(final long number) {
    assert number > 0 : "Number must be positive!";
    List<Long> factors = factor(number);
    long phi = number;
    for (final long factor : Sets.newHashSet(factors)) {
      phi = phi / factor * (factor - 1);
    }
    return phi;
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

    {
      NewPrimes primes = NewPrimes.getInstance();

      // test O(n) get method
      for (int i = 24; i >= 0; --i) {
        System.out.print(primes.get(i) + " ");
      }
      System.out.println();
    }

    {
      //      NewPrimes primes = NewPrimes.getInstance();
      //    int i = 1;
      //    for (long prime : primes) {
      //      if (!primes.isPrime(prime)) {
      //        System.err.println(prime + " is NOT prime!");
      //      }
      //      System.out.print(prime + " ");
      //      if (i++ % 10 == 0) {
      //        System.out.println();
      //      }
      //    }
    }

    {
      NewPrimes primes = NewPrimes.getInstance();
      long number = 60;
      System.out.println("factors of " + number + " = " + primes.factor(number));
      System.out.println("divisors of " + number + " = " + primes.divisors(number));
      System.out.println("number of divisors of " + number + " = " + primes.countDivisors(number));
      System.out.println("sum of divisors of " + number + " = " + primes.sumDivisors(number));
      System.out.println("totient of " + number + " = " + primes.totient(number));

      System.out.println();
      System.out.println("Largest prime stored is " + primes.getLargestStoredPrime());
    }
  }
}
