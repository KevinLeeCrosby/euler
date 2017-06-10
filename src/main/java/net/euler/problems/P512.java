package net.euler.problems;

/**
 * Let φ(n)φ(n) be Euler's totient function.
 *
 * Let f(n) = (∑^n_i=1 φ(n^i)) mod (n+1).
 *
 * Let g(n) = ∑^n_i=1 f(i).
 *
 * g(100) = 2007.
 *
 * Find g(5×10^8).
 *
 * @author Kevin Crosby
 */
public class P512 {
  private final int limit;
  private final int[] oddPhi;

  private P512(int limit) {
    this.limit = limit;
    this.oddPhi = oddSieve(limit);
  }

  private int[] sieve(int n) {
    int[] phi = new int[n + 1];
    phi[1] = 1;
    for (int i = 2; i <= n; i++) {
      if (phi[i] == 0) {
        phi[i] = i - 1;
        for (int j = 2; i * j <= n; j++) {
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

  private int index(int i) {
    return (i + 1) >> 1;
  }

  private int[] oddSieve(int n) {
    int[] phi = new int[index(n) + 1];
    phi[1] = 1;
    for (int i = 3; i <= n; i += 2) {
      if (phi[index(i)] == 0) {
        phi[index(i)] = i - 1;
        for (int j = 3; i * j <= n; j += 2) {
          if (phi[index(j)] == 0) { continue; }

          int q = j;
          int f = i - 1;
          while (q % i == 0) {
            f *= i;
            q /= i;
          }
          phi[index(i * j)] = f * phi[index(q)];
        }
      }
    }
    return phi;
  }

  private int phi(int n) {
    assert (n & 1) == 1 : "Did not compute even numbered Euler Totients!";
    return oddPhi[index(n)];
  }

  private long g(int n) {
    long sum = 0;
    for (int i = 1; i <= n; i += 2) {
      sum += phi(i);
    }
    return sum;
  }

  public static void main(String[] args) {
    final int n = args.length > 0 ? Integer.parseInt(args[0]) : 500_000_000;

    P512 problem = new P512(n);

    System.out.format("g(%d) = %d\n", n, problem.g(n));
  }
}

