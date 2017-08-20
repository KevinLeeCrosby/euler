package net.euler.problems;

/**
 * A horizontal row comprising of 2n + 1 squares has n red counters placed at one end and n blue counters at the other
 * end, being separated by a single empty square in the centre. For example, when n = 3.
 *
 * A counter can move from one square to the next (slide) or can jump over another counter (hop) as long as the square
 * next to that counter is unoccupied.
 *
 * Let M(n) represent the minimum number of moves/actions to completely reverse the positions of the coloured counters;
 * that is, move all the red counters to the right and all the blue counters to the left.
 *
 * It can be verified M(3) = 15, which also happens to be a triangle number.
 *
 * If we create a sequence based on the values of n for which M(n) is a triangle number then the first five terms would
 * be:
 * 1, 3, 10, 22, and 63, and their sum would be 99.
 *
 * Find the sum of the first forty terms of this sequence.
 *
 * @author Kevin Crosby
 */
public class P321 {
  private final int limit;

  private P321(int limit) {
    this.limit = limit;
  }

  // See solution from https://www.alpertron.com.ar/QUAD.HTM, for (A, B, C, D, E, F) = (2, 0, -1, 4, -1, 0)
  private long sum() {
    long sum = 0;

    long[] xs = new long[]{1, 3};
    long[] ys = new long[]{2, 5};

    for (int count = 0; count < limit; ++count) {
      int parity = (count & 1) == 0 ? 0 : 1;
      long x = xs[parity];
      long y = ys[parity];
      System.out.format("%d: %d, %d\n", count, x, y);
      sum += x;

      xs[parity] = 3 * x + 2 * y + 3;
      ys[parity] = 4 * x + 3 * y + 5;
    }

    return sum;
  }

  public static void main(String[] args) {
    //final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 5; // 99
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 40; // 2470433131948040

    P321 problem = new P321(limit);

    long sum = problem.sum();
    System.out.format("The sum of the first %d terms for which M(%d) is a triangle number is %d.\n", limit, limit, sum);
  }
}
