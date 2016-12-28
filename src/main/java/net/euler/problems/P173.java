package net.euler.problems;

/**
 * We shall define a square lamina to be a square outline with a square "hole" so that the shape possesses vertical and
 * horizontal symmetry. For example, using exactly thirty-two square tiles we can form two different square laminae:
 *
 * With one-hundred tiles, and not necessarily using all of the tiles at one time, it is possible to form forty-one
 * different square laminae.
 *
 * Using up to one million tiles how many different square laminae can be formed?
 *
 * @author Kevin Crosby.
 */
public class P173 {
  private P173() {
  }

  private interface Parity {
    int f(int k);

    default int sum(int i, int j) {
      final int k = Math.abs(j - i + 1);
      return k * (f(i) + f(j)) / 2;
    }
  }

  private static class Odd implements Parity {
    public int f(final int k) {
      return 8 * (k + 1);
    }
  }

  private static class Even implements Parity {
    public int f(final int k) {
      return 8 * (k + 1) + 4;
    }
  }

  private int solve(final int n) {
    int k = 0;
    for (final Parity p : new Parity[]{new Even(), new Odd()}) {
      for (int i = 0; p.f(i) <= n; ++i) {
        for (int j = i; p.sum(i, j) <= n; ++j) {
          k++;
        }
      }
    }
    return k;
  }

  public static void main(String[] args) {
    final int tiles = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000; // 1572729

    P173 problem = new P173();
    int laminae = problem.solve(tiles);

    System.out.format("Using up to %d tiles, %d different square laminae can be formed.", tiles, laminae);
  }
}
