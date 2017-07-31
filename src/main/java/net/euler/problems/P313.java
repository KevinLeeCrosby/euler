package net.euler.problems;

import net.euler.utils.NewPrimes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static net.euler.utils.MathUtils.pow;

/**
 * In a sliding game a counter may slide horizontally or vertically into an empty space. The objective of the game is to
 * move the red counter from the top left corner of a grid to the bottom right corner; the space always starts in the
 * bottom right corner. For example, the following sequence of pictures show how the game can be completed in five moves
 * on a 2 by 2 grid.
 *
 * Let S(m,n) represent the minimum number of moves to complete the game on an m by n grid. For example, it can be
 * verified that S(5,4) = 25.
 *
 * There are exactly 5482 grids for which S(m,n) = p^2, where p < 100 is prime.
 *
 * How many grids does S(m,n) = p^2, where p < 10^6 is prime?
 *
 * @author Kevin Crosby
 */
public class P313 {
  private final long limit2;
  private final List<Long> squares;

  private P313(long limit) {
    this.limit2 = pow(limit, 2);
    NewPrimes primes = NewPrimes.getInstance(limit);
    this.squares = StreamSupport.stream(primes.spliterator(), false)
        .skip(2) // skip 4 and 9
        .map(p -> p * p)
        .collect(Collectors.toList());
  }

  private long count() {
    long count = 2; // count 9
    int n = squares.size();
    int m;
    long f;
    int i = 0;
    for (m = 4, f = 21; i < n && m <= limit2; m += 3, f += 24) {
      while (i < n && squares.get(i) <= f && i < n) {
        ++i;
      }
      count += 2 * (n - i);
    }

    return count;
  }

  public static void main(String[] args) {
    //long limit = args.length > 0 ? Long.parseLong(args[0]) : 100L; // 5482
    long limit = args.length > 0 ? Long.parseLong(args[0]) : 1_000_000L; //

    P313 problem = new P313(limit);

    long count = problem.count();
    System.out.format("There are exactly %d grids for which S(m,n) = p^2, where p < %d is prime.\n", count, limit);
  }
}
