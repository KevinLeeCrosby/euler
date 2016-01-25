package net.euler.utils;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static net.euler.utils.MathUtils.factorial;

/**
 * Permutation generator
 *
 * @author Kevin Crosby
 */
public class Permutations<T> implements Iterable<List<T>> {
  private final List<T> items;
  private final int n;
  private final long size;

  public Permutations(final List<T> items) {
    this.items = items;
    this.n = items.size();
    this.size = factorial(n);
  }

  public long size() {
    return size;
  }

  public List<T> get(final long index) {
    return permutation(encode(index));
  }

  // factoradic (mixed radix numeral system)
  private int[] encode(final long decimal) {
    int[] lehmer = new int[n];
    long number = decimal;
    for(int v = 2; v <= n; v++) {
      lehmer[n - v] = (int) (number % v); // generate Lehmer code
      number /= v;
    }
    return lehmer;
  }

  //private long decode(final int[] lehmer) {
  //  long decimal = 0;
  //  for(int i = 0; i < n - 1; ++i) {
  //    decimal = decimal * (n - i) + lehmer[i];
  //  }
  //  return decimal;
  //}

  private List<T> permutation(final int[] lehmer) {
    List<T> sequence = Lists.newLinkedList(items);
    for(int i = 0; i < n - 1; ++i) {
      Collections.rotate(sequence.subList(i, i + lehmer[i] + 1), 1);
    }
    return Collections.unmodifiableList(sequence);
  }

  //private int[] lehmer(final List<T> permutation) {
  //  int[] lehmer = new int[n];
  //  for(int i = 0; i < n - 1; ++i) {
  //    for(int j = i + 1; j < n; ++j) {
  //      if(permutation.get(i) > permutation.get(j)) { // assumes original items are sorted and comparable
  //        lehmer[i]++;
  //      }
  //    }
  //  }
  //  return lehmer;
  //}

  @Override
  public Iterator<List<T>> iterator() {
    return new PermutationIterator();
  }

  private class PermutationIterator implements Iterator<List<T>> {
    private long d; // decimal

    private PermutationIterator() {
      d = 0;
    }

    @Override
    public boolean hasNext() {
      return d < size;
    }

    @Override
    public List<T> next() {
      return permutation(encode(d++));
    }
  }
}
