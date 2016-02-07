package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;
import net.euler.utils.Pascal;

import java.util.List;
import java.util.Set;

import static net.euler.utils.MathUtils.binomial;
import static net.euler.utils.MathUtils.sqrt;

/**
 * The binomial coefficients nCk can be arranged in triangular form, Pascal's triangle, like this:
 * 1
 * 1   1
 * 1   2   1
 * 1   3   3   1
 * 1   4   6   4   1
 * 1   5  10  10   5   1
 * 1   6  15  20  15   6   1
 * 1   7  21  35  35  21   7   1
 * .........
 *
 * It can be seen that the first eight rows of Pascal's triangle contain twelve distinct numbers: 1, 2, 3, 4, 5, 6, 7,
 * 10, 15, 20, 21 and 35.
 *
 * A positive integer n is called squarefree if no square of a prime divides n. Of the twelve distinct numbers in the
 * first eight rows of Pascal's triangle, all except 4 and 20 are squarefree. The sum of the distinct squarefree
 * numbers in the first eight rows is 105.
 *
 * Find the sum of the distinct squarefree numbers in the first 51 rows of Pascal's triangle.
 *
 * @author Kevin Crosby
 */
public class P203 {
  private final int rows;
  private final Pascal pascal;
  private final NewPrimes primes;

  private P203(final int rows) {
    this.rows = rows;
    pascal = new Pascal(0, 0);
    long limit = binomial(rows - 1, (rows - 1) / 2);
    primes = NewPrimes.getInstance(sqrt(limit));
  }

  private boolean isSquareFree(final long n) {
    List<Long> factors = primes.factor(n);
    return factors.size() == factors.stream().distinct().count();
  }

  private long sum() {
    Set<Long> set = Sets.newHashSet();
    pascal.goLowerRight();
    set.add(pascal.getCurrent());
    for(int n = 2; n < rows; ++n) {
      if((n & 1) == 0) {
        pascal.goLowerLeft(); // at left edge
        for(int k = n / 2; k > 0; --k) {
          set.add(pascal.getCurrent());
          pascal.goLeft();
        }
      } else {
        pascal.goLowerRight(); // in middle
        for(int k = 1; k <= n / 2; ++k) {
          set.add(pascal.getCurrent());
          pascal.goRight();
        }
      }
    }
    return set.stream().filter(this::isSquareFree).mapToLong(Long::longValue).sum();
  }

  public static void main(String[] args) {
    final int rows = args.length > 0 ? Integer.parseInt(args[0]) : 51;

    P203 problem = new P203(rows);
    long sum = problem.sum();

    System.out.format("The sum of the distinct squarefree numbers in the first %d rows of Pascal's triangle is %d.\n", rows, sum);
  }
}
