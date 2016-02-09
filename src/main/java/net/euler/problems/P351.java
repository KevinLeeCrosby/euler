package net.euler.problems;

import net.euler.utils.MathUtils;

import java.util.stream.IntStream;

import static net.euler.utils.MathUtils.binomial;
import static net.euler.utils.MathUtils.pow;

/**
 * A hexagonal orchard of order n is a triangular lattice made up of points within a regular hexagon with side n. The
 * following is an example of a hexagonal orchard of order 5:
 *
 * Highlighted in green are the points which are hidden from the center by a point closer to it. It can be seen that
 * for a hexagonal orchard of order 5, 30 points are hidden from the center.
 *
 * Let H(n) be the number of points hidden from the center in a hexagonal orchard of order n.
 *
 * H(5) = 30. H(10) = 138. H(1 000) = 1177848.
 *
 * Find H(100 000 000).
 *
 * @author Kevin Crosby
 */
public class P351 {
  private final int limit;
  private final int[] mobius;

  private P351(final int limit) {
    this.limit = limit;
    mobius = sieveMobius();
  }

  // See http://mathoverflow.net/questions/99473/calculating-m%C3%B6bius-function
  private int[] sieveMobius() {
    int sqrt = (int) MathUtils.sqrt(limit);
    int[] mu = IntStream.generate(() -> 1).limit(limit + 1).toArray();
    for(int i = 2; i <= sqrt; i++) {
      if(mu[i] == 1) {
        for(int j = i; j <= limit; j += i) {
          mu[j] *= -i;
        }
        for(int j = i * i; j <= limit; j += i * i) {
          mu[j] = 0;
        }
      }
    }
    for(int i = 2; i <= limit; i++) {
      if(mu[i] == i) {
        mu[i] = 1;
      } else if(mu[i] == -i) {
        mu[i] = -1;
      } else if(mu[i] < 0) {
        mu[i] = 1;
      } else if(mu[i] > 0) {
        mu[i] = -1;
      }
    }
    return mu;
  }

  // See https://en.wikipedia.org/wiki/Euler%27s_totient_function#Other_formulae_involving_.CF.86
  private long sumOfTotients(final int n) {
    return (1 + IntStream.rangeClosed(1, n).mapToLong(k -> mobius[k] * pow(n / k, 2)).sum()) / 2;
  }

  // See https://oeis.org/A216453
  private long h(final int n) {
    return 6 * (binomial(n + 1, 2) - sumOfTotients(n));
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 100000000;

    long h = new P351(limit).h(limit);
    System.out.format("H(%d) = %d\n", limit, h);
  }
}
