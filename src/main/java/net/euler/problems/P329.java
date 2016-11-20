package net.euler.problems;

import net.euler.utils.NewPrimes;
import org.apache.commons.math3.fraction.BigFraction;

import static org.apache.commons.math3.fraction.BigFraction.ZERO;

/**
 * Susan has a prime frog. Her frog is jumping around over 500 squares numbered 1 to 500. He can only jump one square to
 * the left or to the right, with equal probability, and he cannot jump outside the range [1;500]. (if it lands at
 * either end, it automatically jumps to the only available square on the next move.)
 *
 * When he is on a square with a prime number on it, he croaks 'P' (PRIME) with probability 2/3 or 'N' (NOT PRIME) with
 * probability 1/3 just before jumping to the next square. When he is on a square with a number on it that is not a
 * prime he croaks 'P' with probability 1/3 or 'N' with probability 2/3 just before jumping to the next square.
 *
 * Given that the frog's starting position is random with the same probability for every square, and given that she
 * listens to his first 15 croaks, what is the probability that she hears the sequence PPPPNNPPPNPPNPN?
 *
 * Give your answer as a fraction p/q in reduced form.
 *
 * @author Kevin Crosby
 */
public class P329 {
  private static final BigFraction HALF = new BigFraction(1, 2);

  private final int limit;
  private final NewPrimes primes;
  private final char[] sequence;
  private final int length;

  private P329(final int limit, final String sequence) {
    this.limit = limit;
    this.primes = NewPrimes.getInstance(limit);
    this.sequence = new StringBuilder(sequence).reverse().toString().toCharArray();
    this.length = sequence.length();
  }

  private BigFraction probability(final char character, final int number) {
    boolean isPrime = primes.isPrime(number);
    switch (character) {
      case 'P':
        return isPrime ? new BigFraction(2, 3) : new BigFraction(1, 3);
      case 'N':
        return isPrime ? new BigFraction(1, 3) : new BigFraction(2, 3);
      default:
        throw new IllegalArgumentException(String.format("Invalid character '%c' in sequence!", character));
    }
  }

  private BigFraction probability(final BigFraction[] aleph, final int i) {
    BigFraction probability = ZERO;
    if (i >= length) {
      for (int n = 1; n <= limit; ++n) {
        probability = probability.add(aleph[n]);
      }
      return probability;
    }

    BigFraction[] alpha = new BigFraction[limit + 1];
    for (int n = 1; n <= limit; ++n) {
      if (n == 1) {
        probability = aleph[2];
      } else if (n == limit) {
        probability = aleph[limit - 1];
      } else {
        probability = aleph[n - 1].add(aleph[n + 1]).multiply(HALF);
      }
      alpha[n] = probability.multiply(probability(sequence[i], n));
    }
    return probability(alpha, i + 1);
  }

  private BigFraction probability() {
    BigFraction[] alpha = new BigFraction[limit + 1];
    int i = 0;
    for (int n = 1; n <= limit; ++n) {
      alpha[n] = probability(sequence[i], n).divide(limit);
    }
    return probability(alpha, ++i);
  }


  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 500; // 199740353/29386561536000
    final String sequence = args.length > 1 ? args[1] : "PPPPNNPPPNPPNPN";

    P329 problem = new P329(limit, sequence);
    BigFraction probability = problem.probability();

    System.out.format("The probability that Susan hears the sequence %s is %s.\n", sequence, probability);
  }
}
