package net.euler.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import static net.euler.utils.MathUtils.binomial;

/**
 * Combination generator
 *
 * @author Kevin
 */
public class Combinations<T> implements Iterable<List<T>> {
  private final List<T> items;
  private final int n, k;
  private final long mask;
  private final long size;

  public Combinations(final List<T> items, final int k) {
    this.items = items;
    this.n = items.size();
    this.k = k;
    this.mask = (1 << n) - 1;
    this.size = binomial(n, k);
  }

  public long size() {
    return size;
  }

  public List<T> get(final long index) {
    long dual = size - 1 - index;
    return combination(encode(dual));
  }

  // combinadic
  private int[] encode(final long number) {
    int[] combinadic = new int[k];
    long n = number;
    Pascal pascal = new Pascal(k, 0);
    for(int i = k; i > 0; --i) {
      if(n == 0) {
        for(int j = i; j > 0; --j) {
          combinadic[k - j] = j - 1;
        }
        break;
      }
      while(pascal.getCurrent() > n) {
        pascal.goUpperLeft();
      }
      while(pascal.getLowerRight() <= n) {
        pascal.goLowerRight();
      }
      combinadic[k - i] = pascal.getN();
      n -= pascal.getCurrent();
      pascal.goRight();
    }
    return combinadic;
  }

  private List<T> combination(final int[] combinadic) {
    List<T> combination = Lists.newArrayList();
    for(int index : combinadic) {
      combination.add(items.get(n - index - 1));
    }
    return Collections.unmodifiableList(combination);
  }

  private List<T> combination(final long bits) {
    Deque<T> combination = Queues.newArrayDeque();
    for(long b = ~bits & mask; b > 0; b &= b - 1) {
      int index = Long.numberOfTrailingZeros(b);
      combination.push(items.get(n - index - 1));
    }
    return Collections.unmodifiableList(Lists.newArrayList(combination));
  }

  @Override
  public Iterator<List<T>> iterator() {
    return new CombinationIterator();
  }

  private class CombinationIterator implements Iterator<List<T>> {
    private long bits;

    // uses inverted logic
    private CombinationIterator() {
      bits = (1 << (n - k)) - 1;
    }

    @Override
    public boolean hasNext() {
      return bits <= mask;
    }

    @Override
    public List<T> next() {
      long oldBits = bits;

      // See:  http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation (Lexicographical Permutation)
      long t = bits | (bits - 1); // t gets bit's least significant 0 bits set to 1
      // Next set to 1 the most significant bit to change,
      // set to 0 the least significant ones, and add the necessary 1 bits.
      bits = (t + 1) | ((~t & -~t) - 1) >>> (Long.numberOfTrailingZeros(bits) + 1);

      // See:  https://en.wikipedia.org/wiki/Combinatorial_number_system#Applications (Gosper's hack, aka HAKMEM 175)
      // long x = bits;
      // long u = x & -x; // extract rightmost bit 1; u =  0'00^a10^b
      // long v = u + x; // set last non-trailing bit 0, and clear to the right; v=x'10^a00^b
      // bits = v +(((v^x)/u)>>2); // v^x = 0'11^a10^b, (v^x)/u = 0'0^b1^{a+2}, and x ← x'100^b1^a

      return combination(oldBits);
    }
  }
}
