package net.euler.problems;

import net.euler.utils.MathUtils;
import net.euler.utils.NewPrimes;

import java.util.List;

/**
 * A riffle shuffle is executed as follows: a deck of cards is split into two equal halves, with the top half taken in
 * the left hand and the bottom half taken in the right hand. Next, the cards are interleaved exactly, with the top card
 * in the right half inserted just after the top card in the left half, the 2nd card in the right half just after the
 * 2nd card in the left half, etc. (Note that this process preserves the location of the top and bottom card of the
 * deck)
 *
 * Let s(n) be the minimum number of consecutive riffle shuffles needed to restore a deck of size n to its original
 * configuration, where n is a positive even number.
 *
 * Amazingly, a standard deck of 52 cards will first return to its original configuration after only 8 perfect shuffles,
 * so s(52)=8. It can be verified that a deck of 86 cards will also return to its original configuration after exactly 8
 * shuffles, and the sum of all values of n that satisfy s(n)=8 is 412.
 *
 * Find the sum of all values of n that satisfy s(n)=60.
 *
 * @author Kevin Crosby.
 */
public class P622 {
  private final long s;
  private final List<Long> divisors;

  private P622(long s) {
    this.s = s;
    long mersenne = (1L << s) - 1;
    long limit = MathUtils.sqrt(mersenne);
    NewPrimes primes = NewPrimes.getInstance(limit);
    divisors = primes.divisors(mersenne);
  }

  private long solve() {
    long sum = 0;
    for (long divisor : divisors) {
      long n = divisor + 1;
      if (s(n) == s) {
        sum += n;
      }
    }

    return sum;
  }

  // c.f. sequence A002326
  private long s(long n) {
    n -= 1;
    long order = 1;
    long m = 1; // Mersenne number
    while (m > 0 && Math.floorMod(m, n) > 0) {
      order++;
      m = m << 1 | 1;
    }

    return m > 0 ? order : -1;
  }

  public static void main(String[] args) {
    //final long s = args.length > 0 ? Long.parseLong(args[0]) : 8; // 412
    final long s = args.length > 0 ? Long.parseLong(args[0]) : 60;

    P622 problem = new P622(s);

    long sum = problem.solve();

    System.out.printf("The sum of all values of n that satisfy s(n)=%d is %d.\n", s, sum);
  }
}
