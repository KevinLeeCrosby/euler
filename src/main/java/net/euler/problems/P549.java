package net.euler.problems;

import java.util.Arrays;
import java.util.BitSet;

import static java.lang.Math.max;
import static net.euler.utils.MathUtils.logBase;
import static net.euler.utils.MathUtils.pow;

/**
 * The smallest number m such that 10 divides m! is m=5.
 * The smallest number m such that 25 divides m! is m=10.
 *
 * Let s(n) be the smallest number m such that n divides m!.
 * So s(10)=5 and s(25)=10.
 * Let S(n) be ∑s(i) for 2 ≤ i ≤ n.
 * S(100)=2012.
 *
 * Find S(10^8).
 *
 * @author Kevin Crosby
 */
public class P549 {
  private BitSet sieve;
  private final int[] a;

  private P549(final int limit) {
    this.a = generate(limit);
  }

  private int[] generate(final int limit) {
    int[] a = new int[limit + 1];
    // a[1] = 1;  // Omitted per problem description
    sieve = new BitSet(limit + 1);
    sieve.set(2, limit + 1);
    for (int prime = sieve.nextSetBit(0); prime >= 0; prime = sieve.nextSetBit(prime + 1)) {
      a[prime] = prime;
      for (int composite = 2 * prime; composite <= limit; composite += prime) {
        a[composite] = max(a[composite], mu(prime, composite));
        sieve.clear(composite);
      }
    }
    return a;
  }

  private int mu(final int p, final int n) {
    assert sieve.get(p) : String.format("%d is not a prime number!", p);
    int alpha = vp(p, n);
    if (alpha <= p) {
      return p * alpha;
    }
    return kempner(p, alpha);
  }

  private int kempner(final int p, final int alpha) {
    int nu = (int)logBase(1 + alpha * (p - 1), p);
    int a = (int)((pow(p, nu) - 1) / (p - 1));
    assert a > 0 : String.format("Overflow error for %d ^ %d !!!", p, nu);
    return (p - 1) * alpha + kempner(p, alpha, a);
  }

  private int kempner(final int p, final int r, final int a) {
    int k = r / a;
    int remainder = r - k * a;
    if (remainder == 0) {
      return k;
    }
    return k + kempner(p, remainder, (a - 1) / p);
  }

  // p-adic order (valuation) of n : vp(n)
  private int vp(final int p, final int n) {
    assert sieve.get(p) : String.format("%d is not a prime number!", p);
    int number = n, m = 0, q = number / p;
    while (q * p == number) {
      ++m;
      number = q;
      q = number / p;
    }
    return m;
  }

  private long sum() {
    return Arrays.stream(a).mapToLong(Integer::toUnsignedLong).sum();
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 100_000_000;
    // final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 100; // 2012

    P549 problem = new P549(limit);

    System.out.println("Let s(n) be the smallest number m such that n divides m!.\nLet S(n) be ∑s(i) for 2 ≤ i ≤ n.");
    System.out.format("S(%d) is %d.", limit, problem.sum());
  }
}
