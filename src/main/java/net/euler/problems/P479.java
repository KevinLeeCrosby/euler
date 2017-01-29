package net.euler.problems;

import static net.euler.utils.MathUtils.modPow;

/**
 * Let ak, bk, and ck represent the three solutions (real or complex numbers) to the expression 1/x = (k/x)^2(k+x^2) -
 * kx.
 *
 * For instance, for k = 5, we see that {a5, b5, c5} is approximately {5.727244, -0.363622+2.057397i,
 * -0.363622-2.057397i}.
 *
 * Let S(n) = Σ (ak+bk)^p(bk+ck)^p(ck+ak)^p for all integers p, k such that 1 ≤ p, k ≤ n.
 *
 * Interestingly, S(n) is always an integer. For example, S(4) = 51160.
 *
 * Find S(10^6) modulo 1 000 000 007.
 *
 * @author Kevin Crosby
 */
public class P479 {
  private static final long MODULUS = 1_000_000_007;
  private final int n;

  private P479(final int n) {
    this.n = n;
  }

  // sum of powers for p = 1 to n, with Modulus applied
  private long sumOfPowersMod(final long x, final int n) {
    long numerator = modPow(x, n + 1, MODULUS) - 1;
    long denominator = modPow(x - 1, MODULUS - 2, MODULUS); // since modulus is prime
    return (numerator * denominator) % MODULUS - 1;
  }

  private long solve() {
    long sum = 0;
    for (long k = 1, o = 1, s = 1; k <= n; ++k, o += 2, s += o) {
      sum = (sum + sumOfPowersMod(1 - s, n)) % MODULUS;
    }
    return sum;
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000; // 191541795

    P479 problem = new P479(limit);
    long solution = problem.solve();

    System.out.format("S(%d) is %d\n", limit, solution);
  }
}
