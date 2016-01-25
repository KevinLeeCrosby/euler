package net.euler.utils;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Kevin
 */
public class Combinations<T> implements Iterable<List<T>> {
  private final List<T> items;
  private final int n, k;

  public Combinations(final List<T> items, final int k) {
    this.items = items;
    this.n = items.size();
    this.k = k;
  }

  @Override
  public Iterator<List<T>> iterator() {
    return new CombinationIterator();
  }

  private class CombinationIterator implements Iterator<List<T>> {
    private int bits;
    private final int limit, mask;

    // uses inverted logic
    private CombinationIterator() {
      bits = (1 << (n - k)) - 1;
      limit = bits << k;
      mask = (1 << n) - 1;
    }

    @Override
    public boolean hasNext() {
      return bits <= limit;
    }

    @Override
    public List<T> next() {
      int oldBits = bits;

      // see http://oeis.org/A014311
      int x = bits, s = x & -x, r = x + s, o = r ^ x;
      o = (o >>> 2) / s;
      bits = r | o;
      return combination(oldBits);
    }

    private List<T> combination(final int bits) {
      List<T> combination = Lists.newArrayList();
      for(int b = ~bits & mask; b > 0; b &= b - 1) {
        int index = Integer.numberOfTrailingZeros(b);
        combination.add(items.get(n - index - 1));
      }
      return Collections.unmodifiableList(Lists.reverse(combination));
    }
  }
}
