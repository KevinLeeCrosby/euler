package net.euler.problems;

import static java.lang.Math.floorMod;
import static net.euler.utils.MathUtils.gcd;

/**
 * Let g(a,n,b,m) be the smallest non-negative solution x to the system:
 * x = a mod n
 * x = b mod m
 * if such a solution exists, otherwise 0.
 *
 * E.g. g(2,4,4,6)=10, but g(3,4,4,6)=0.
 *
 * Let φ(n) be Euler's totient function.
 *
 * Let f(n,m)=g(φ(n),n,φ(m),m)
 *
 * Find ∑f(n,m) for 1000000 ≤ n < m < 1005000
 *
 * @author Kevin Crosby
 */
public class P531 {
  private final int min;
  private final int max;
  private final long[] phi;

  private P531(final int min, final int max) {
    this.min = min;
    this.max = max;
    this.phi = totients(max);
  }

  // totient sieve
  private long[] totients(int n) {
    long[] phi = new long[n];
    phi[1] = 1;
    for (int i = 2; i < n; i++) {
      if (phi[i] == 0) {
        phi[i] = i - 1;
        for (int j = 2; i * j < n; j++) {
          if (phi[j] == 0) { continue; }

          int q = j;
          int f = i - 1;
          while (q % i == 0) {
            f *= i;
            q /= i;
          }
          phi[i * j] = f * phi[q];
        }
      }
    }
    return phi;
  }

  // based on Extended Euclidean algorithm
  private long g(long a, long n, long b, long m) {
    long gcd = gcd(n, m);
    if ((b - a) % gcd != 0 && gcd != 1) {
      return 0;
    }

    long[] rst = {m, 0, 1};
    long[] orst = {n, 1, 0};
    while (rst[0] != 0) {
      long q = orst[0] / rst[0];
      for (int i = 0; i < 3; ++i) {
        long tmp = rst[i];
        rst[i] = orst[i] - q * tmp;
        orst[i] = tmp;
      }
    }
    long s = orst[1], t = orst[2]; // Bézout coefficients

    // https://math.stackexchange.com/questions/1644677/what-to-do-if-the-modulus-is-not-coprime-in-the-chinese-remainder-theoremg
    gcd = n * s + m * t;
    long lcm = n * m / gcd;
    long x = a + (b - a) * n * s / gcd;
    //long x = b + (a - b) * m * t / gcd;
    return floorMod(x, lcm);
  }

  private long f(final int n, final int m) {
    return g(phi[n], n, phi[m], m);
  }

  private long solution() {
    long sum = 0;
    for (int n = min; n < max - 1; ++n) {
      for (int m = n + 1; m < max; ++m) {
        sum += f(n, m);
      }
    }

    return sum;
  }

  public static void main(String[] args) {
    final int n = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000; // 4515432351156203105
    final int m = args.length > 1 ? Integer.parseInt(args[1]) : 1_005_000;

    P531 problem = new P531(n, m);

    long sum = problem.solution();
    System.out.format("∑f(n,m) for %d ≤ n < m < %d is %d.\n", n, m, sum);
  }
}
