package net.euler.problems;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSetMultimap;

import java.util.Iterator;
import java.util.Map;

/**
 * Each of the six faces on a cube has a different digit (0 to 9) written on it; the same is done to a second cube. By
 * placing the two cubes side-by-side in different positions we can form a variety of 2-digit numbers.
 *
 * For example, the square number 64 could be formed:
 * ￼
 * In fact, by carefully choosing the digits on both cubes it is possible to display all of the square numbers below
 * one-hundred: 01, 04, 09, 16, 25, 36, 49, 64, and 81.
 *
 * For example, one way this can be achieved is by placing {0, 5, 6, 7, 8, 9} on one cube and {1, 2, 3, 4, 8, 9} on the
 * other cube.
 *
 * However, for this problem we shall allow the 6 or 9 to be turned upside-down so that an arrangement like {0, 5, 6,
 * 7, 8, 9} and {1, 2, 3, 4, 6, 7} allows for all nine square numbers to be displayed; otherwise it would be impossible
 * to obtain 09.
 *
 * In determining a distinct arrangement we are interested in the digits on each cube, not the order.
 *
 * {1, 2, 3, 4, 5, 6} is equivalent to {3, 6, 4, 1, 2, 5}
 * {1, 2, 3, 4, 5, 6} is distinct from {1, 2, 3, 4, 5, 9}
 *
 * But because we are allowing 6 and 9 to be reversed, the two distinct sets in the last example both represent the
 * extended set {1, 2, 3, 4, 5, 6, 9} for the purpose of forming 2-digit numbers.
 *
 * How many distinct arrangements of the two cubes allow for all of the square numbers to be displayed?
 *
 * @author Kevin Crosby
 */
public class P090 {
  private static final ImmutableMultimap<Integer, Integer> pairs = new ImmutableSetMultimap.Builder<Integer, Integer>()
      .put(0, 1).put(0, 4).put(0, 9).put(1, 6).put(2, 5).put(3, 6).put(4, 9).put(6, 4).put(8, 1)
      .build();

  private static class Die {
    private final Integer limit, mask;
    private Integer bits;

    public Die(final int n, final int k) {
      assert n > k : "Number of integers must exceed number of die faces!";
      bits = (1 << k) - 1;
      limit = bits << (n - k);
      mask = (1 << n) - 1;
    }

    public Die(final Die that) {
      this.bits = that.bits;
      this.limit = that.limit;
      this.mask = that.mask;
    }

    @Override
    public String toString() {
      return Integer.toBinaryString((bits & mask) + mask + 1).substring(1);
    }
  }

  private static class DieCombos implements Iterable<Die> {
    private Die die;

    public DieCombos(final Die die) {
      this.die = new Die(die);
    }

    public Iterator<Die> iterator() {
      return new DieIterator(die);
    }

    public class DieIterator implements Iterator<Die> {
      private Die die;

      private DieIterator(final Die die) {
        this.die = new Die(die);
      }

      @Override
      public boolean hasNext() {
        return die.bits <= die.limit;
      }

      // See:  http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation (Lexicographical Permutation)
      @Override
      public Die next() {
        Die oldDie = new Die(die);
        int t = die.bits | (die.bits - 1); // t gets bit's least significant 0 bits set to 1
        // Next set to 1 the most significant bit to change,
        // set to 0 the least significant ones, and add the necessary 1 bits.
        die.bits = (t + 1) | ((~t & -~t) - 1) >>> (Integer.numberOfTrailingZeros(die.bits) + 1);
        return oldDie;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    }
  }

  public static boolean areGoodDice(final Die die1, final Die die2) {
    int d1 = extend(die1), d2 = extend(die2);
    for (final Map.Entry<Integer, Integer> pair : pairs.entries()) {
      int a = pair.getKey(), b = pair.getValue();
      if (!(test(d1, a) && test(d2, b) || test(d1, b) && test(d2, a))) {
        return false;
      }
    }
    return true;
  }

  public static boolean test(final int bits, final int bit) {
    return ((1 << bit) & bits) != 0;
  }

  public static int extend(final Die die) {
    int d = die.bits;
    if (test(d, 6) || test(d, 9)) {
      d |= (1 << 6) | (1 << 9);
    }
    return d;
  }

  public static void main(String[] args) {
    int count = 0;
    DieCombos dieCombos = new DieCombos(new Die(10, 6));
    for (final Die d1 : dieCombos) {
      for (final Die d2 : new DieCombos(d1)) {
        assert d1.bits <= d2.bits;
        if (areGoodDice(d1, d2)) {
          count++;
          System.out.println(count + ":  " + d1 + "    " + d2);
        }
      }
    }

    System.out.println("There are " + count + " distinct arrangements of the two cubes that allow for all of the square numbers to be displayed.");
  }
}
