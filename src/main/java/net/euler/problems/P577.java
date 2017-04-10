package net.euler.problems;

/**
 * An equilateral triangle with integer side length n≥3 is divided into n^2 equilateral triangles with side length 1 as
 * shown in the diagram below.
 * The vertices of these triangles constitute a triangular lattice with (n+1)(n+2)/2 lattice points.
 *
 * Let H(n) be the number of all regular hexagons that can be found by connecting 6 of these points.
 *
 * For example, H(3)=1, H(6)=12, and H(20)=966.
 *
 * Find ∑n=3 to 12345 H(n).
 *
 * @author Kevin Crosby
 */
public class P577 {
  private final int limit;

  public P577(final int limit) {
    this.limit = limit;
  }

  private long tetrahedral(final long n) {
    return n * (n + 1) * (n + 2) / 6;
  }

  private long sum(final int n) {
    long sum = 0;
    for (int m = 1, i = n - 2; m <= n / 3; ++m, i -= 3) {
      sum += m * tetrahedral(i);
    }
    return sum;
  }

  public long sum() {
    return sum(limit);
  }

  public static void main(String[] args) {
    int limit = args.length > 0 ? Integer.parseInt(args[0]) : 12345; // 265695031399260211

    P577 problem = new P577(limit);

    long sum = problem.sum();
    System.out.format("The sum from H(3) to H(%d) is %d.\n", limit, sum);
  }
}
