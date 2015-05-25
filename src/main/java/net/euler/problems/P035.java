package net.euler.problems;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import net.euler.utils.MathUtils;
import net.euler.utils.Primes;

import java.util.Set;

/**
 * The number, 197, is called a circular prime because all rotations of the digits: 197, 971, and 719, are themselves
 * prime.
 *
 * There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71, 73, 79, and 97.
 *
 * How many circular primes are there below one million?
 *
 * @author Kevin Crosby
 */
public class P035 {
  private static Primes primes = Primes.getInstance();

  private static Set<Long> circularPrimes = Sets.newTreeSet();
  private static Set<Long> nonCircularPrimes = Sets.newHashSet();

  private static void categorizePrime(long prime) {
    assert primes.isPrime(prime) : "Cannot categorize the composite number " + prime;
    if (circularPrimes.contains(prime) || nonCircularPrimes.contains(prime)) { // already categorized
      return;
    }

    Set<Long> testSet = Sets.newHashSet(prime);

    boolean circular = true;
    long length = MathUtils.log10(prime) + 1; // number of digits
    long power = MathUtils.pow(10, length - 1);
    long digit = prime % 10;
    long number = digit * power + prime / 10; // rotated number
    while (number != prime) {
      if (digit != 0 && primes.isPrime(number)) {
        testSet.add(number);
      } else {
        circular = false;
      }
      digit = number % 10;
      number = digit * power + number / 10; // rotated number
    }

    if (circular) {
      circularPrimes.addAll(testSet);
    } else {
      nonCircularPrimes.addAll(testSet);
    }

  }


  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 1000000;
    }

    for (long prime : primes) {
      if (prime >= limit) break;
      //System.out.println("categorizing: " + prime);
      categorizePrime(prime);
    }

    System.out.println(Joiner.on(",").join(circularPrimes));
    int count = circularPrimes.size();
    System.out.println("There are " + count + " circular primes below " + limit);
  }

}
