package net.euler.problems;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import net.euler.utils.Rational;
import org.apache.commons.lang3.tuple.Pair;

import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import static net.euler.utils.MathUtils.pow;
import static net.euler.utils.Rational.ZERO;

/**
 * By using each of the digits from the set, {1, 2, 3, 4}, exactly once, and making use of the four arithmetic
 * operations (+, −, *, /) and brackets/parentheses, it is possible to form different positive integer targets.
 *
 * For example,
 *
 * 8 = (4 * (1 + 3)) / 2
 * 14 = 4 * (3 + 1 / 2)
 * 19 = 4 * (2 + 3) − 1
 * 36 = 3 * 4 * (2 + 1)
 *
 * Note that concatenations of the digits, like 12 + 34, are not allowed.
 *
 * Using the set, {1, 2, 3, 4}, it is possible to obtain thirty-one different target numbers of which 36 is the
 * maximum, and each of the numbers 1 to 28 can be obtained before encountering the first non-expressible number.
 *
 * Find the set of four distinct digits, a < b < c < d, for which the longest set of consecutive positive integers, 1
 * to n, can be obtained, giving your answer as a string: abcd.
 *
 * @author Kevin Crosby
 */
public class P093 {
  private static class Formulas implements Iterable<List<String>> {
    private final int n, k, m;

    public Formulas(final int k) {
      this(9, k);
    }

    public Formulas(final int n, final int k) {
      this.n = n;
      this.k = k;
      this.m = k - 1;
    }

    public Iterator<List<String>> iterator() {
      return new FormulaIterator();
    }

    private class FormulaIterator implements Iterator<List<String>> {
      private List<Integer> permutation;
      private List<Character> operator;
      private int ordering;

      private final Iterator<List<Integer>> permutations;
      private Iterator<List<Character>> operators;
      private Iterator<Integer> orderings;

      private FormulaIterator() {
        permutations = new Permutations(n, k).iterator();
        operators = new Operations(m).iterator();
        orderings = new Orderings(m).iterator();
        permutation = permutations.next();
        operator = operators.next();
      }

      public boolean hasNext() {
        return permutations.hasNext() || operators.hasNext() || orderings.hasNext();
      }

      public List<String> next() {
        if (orderings.hasNext()) {
          ordering = orderings.next();
        } else if (operators.hasNext()) {
          orderings = new Orderings(m).iterator();
          operator = operators.next();
          ordering = orderings.next();
        } else if (permutations.hasNext()) {
          operators = new Operations(m).iterator();
          orderings = new Orderings(m).iterator();
          permutation = permutations.next();
          operator = operators.next();
          ordering = orderings.next();
        }
        return interlace(permutation, operator, ordering);
      }

      private List<String> interlace(List<Integer> permutation, List<Character> operator, int ordering) {
        List<String> formula = Lists.newArrayList();
        for (int mask = 1 << 2 * m, p = 0, q = 0; mask > 0; mask >>>= 1) {
          boolean isClear = (ordering & mask) == 0;
          formula.add(isClear ? permutation.get(p++).toString() : operator.get(q++).toString());
        }
        return Collections.unmodifiableList(formula);
      }
    }
  }

  private static class Orderings implements Iterable<Integer> {
    private final int m;

    private Orderings(final int m) {
      this.m = m;
    }

    public Iterator<Integer> iterator() {
      return new OrderingIterator();
    }

    private class OrderingIterator implements Iterator<Integer> {
      private int bits;
      private final int limit;

      private OrderingIterator() {
        bits = (1 << m) - 1;
        limit = ((int) pow(4, m) - 1) / 3; // see http://oeis.org/A002450
      }

      public boolean hasNext() {
        return bits <= limit;
      }

      // See:  http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation (Lexicographical Permutation)
      // orderings size follows Catalan numbers, see:  http://oeis.org/A000108
      public Integer next() {
        int oldBits = bits;
        do {
          int t = bits | (bits - 1); // t gets bit's least significant 0 bits set to 1
          // Next set to 1 the most significant bit to change,
          // set to 0 the least significant ones, and add the necessary 1 bits.
          bits = (t + 1) | ((~t & -~t) - 1) >>> (Integer.numberOfTrailingZeros(bits) + 1);
        } while (hasNext() && !isValid(bits));
        return oldBits;
      }

      private boolean isValid(final int bits) {
        int count = 2; // assume two literals are in position 2*k + 1 and 2*k
        for (int mask = 1 << (2 * m - 2); mask > 0; mask >>>= 1) {
          boolean isClear = (bits & mask) == 0;
          count += isClear ? +1 : -1;
          if (count == 0) {
            return false;
          }
        }
        return count == 1;
      }
    }
  }

  private static class Operations implements Iterable<List<Character>> {
    private final int m;
    private static List<Character> operators;
    private final List<List<Character>> operations;

    private Operations(final int m) {
      this.m = m;
      operators = new ImmutableList.Builder<Character>().add('+', '-', '*', '/').build();
      operations = Lists.newArrayList();
      generate();
    }

    private void generate() {
      generate(Lists.<Character>newArrayList(), m);
    }

    private void generate(final List<Character> ops, final int j) {
      if (j == 0) {
        operations.add(Lists.newArrayList(ops));
        return;
      }
      for (char operator : operators) {
        ops.add(operator);
        generate(ops, j - 1);
        ops.remove(ops.size() - 1);
      }
    }

    public Iterator<List<Character>> iterator() {
      return new OperationIterator();
    }

    private class OperationIterator implements Iterator<List<Character>> {
      private int i;

      public OperationIterator() {
        i = 0;
      }

      public boolean hasNext() {
        return i < operations.size();
      }

      public List<Character> next() {
        return operations.get(i++);
      }
    }
  }


  private static class Permutations implements Iterable<List<Integer>> {
    private final int n, k;

    public Permutations(final int k) {
      this(9, k);
    }

    public Permutations(final int n, final int k) {
      this.n = n;
      this.k = k;
    }

    public Iterator<List<Integer>> iterator() {
      return new PermutationIterator();
    }

    private class PermutationIterator implements Iterator<List<Integer>> {
      private final Iterator<List<Integer>> combinations;
      private final Queue<List<Integer>> permutations;

      private PermutationIterator() {
        combinations = new Combinations(n, k).iterator();
        permutations = Queues.newArrayDeque();
      }

      public boolean hasNext() {
        return combinations.hasNext() || !permutations.isEmpty();
      }

      public List<Integer> next() {
        if (permutations.isEmpty()) {
          List<Integer> combination = Lists.newArrayList(combinations.next());
          permute(combination);
        }
        return Collections.unmodifiableList(permutations.remove());
      }

      private void permute(final List<Integer> combination) {
        permute(combination, 0);
      }

      private void permute(final List<Integer> combination, final int j) {
        if (k - j == 1) {
          permutations.add(Lists.newArrayList(combination));
          return;
        }
        for (int i = j; i < k; ++i) {
          swap(combination, i, j);
          permute(combination, j + 1);
          swap(combination, i, j);
        }
      }

      private void swap(final List<Integer> combination, final int i, final int j) {
        int a = combination.get(i), b = combination.get(j);
        combination.set(i, b);
        combination.set(j, a);
      }
    }
  }

  private static class Combinations implements Iterable<List<Integer>> {
    private final int n, k;

    public Combinations(final int k) {
      this(9, k);
    }

    public Combinations(final int n, final int k) {
      this.n = n;
      this.k = k;
    }

    public Iterator<List<Integer>> iterator() {
      return new CombinationIterator();
    }

    private class CombinationIterator implements Iterator<List<Integer>> {
      private int bits; // bits 0 to 8, representing integers 1 to 9
      private final int limit; // bits 0 to 8, representing integers 1 to 9

      private CombinationIterator() {
        assert k <= n : "Can only represent integers 1 to " + n + ".";
        bits = (1 << k) - 1;
        limit = bits << (n - k);
      }

      public boolean hasNext() {
        return bits <= limit;
      }

      // See:  http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation (Lexicographical Permutation)
      public List<Integer> next() {
        int oldBits = bits;
        int t = bits | (bits - 1); // t gets bit's least significant 0 bits set to 1
        // Next set to 1 the most significant bit to change,
        // set to 0 the least significant ones, and add the necessary 1 bits.
        bits = (t + 1) | ((~t & -~t) - 1) >>> (Integer.numberOfTrailingZeros(bits) + 1);
        return bits2Set(oldBits);
      }

      private List<Integer> bits2Set(final int bits) {
        List<Integer> combination = Lists.newArrayList();
        for (int b = bits; b > 0; b &= b - 1) {
          int number = Integer.numberOfTrailingZeros(b) + 1;
          combination.add(number);
        }
        return Collections.unmodifiableList(combination);
      }
    }
  }

  private static Rational evaluate(final Rational a, final Rational b, final char operator) {
    switch (operator) {
      case '+':
        return a.add(b);
      case '-':
        return a.subtract(b);
      case '*':
        return a.multiply(b);
      case '/':
        return b.equals(ZERO) ? null : a.divide(b);
      default:
        throw new UnsupportedOperationException("Unsupported Operator '" + operator + "'");
    }
  }

  private static Rational evaluate(final List<String> formula) {
    Stack<Rational> stack = new Stack<>();
    for (final String token : formula) {
      if (token.matches("-?\\d+")) {
        stack.push(new Rational(Integer.parseInt(token)));
      } else {
        char operator = token.charAt(0);
        Rational b = stack.pop();
        Rational a = stack.pop();
        Rational c = evaluate(a, b, operator);
        if (c == null) {
          return null;
        }
        stack.push(c);
      }
    }
    return stack.pop();
  }

  private static Pair<Integer, Integer> evaluate(final int n, final int k) {
    Map<Integer, BitSet> targets = Maps.newHashMap();
    for (final List<String> formula : new Formulas(n, k)) {
      Rational result = evaluate(formula);
      if (result != null && result.isNatural() && result.compareTo(ZERO) == 1) {
        int digits = extract(formula);
        if (!targets.containsKey(digits)) {
          targets.put(digits, new BitSet());
        }
        targets.get(digits).set(result.intValue());
      }
    }
    int digits = 0, bestBit = 0;
    for (final Entry<Integer, BitSet> entry : targets.entrySet()) {
      int bit = entry.getValue().nextClearBit(1) - 1;
      if (bestBit < bit) {
        bestBit = bit;
        digits = entry.getKey();
      }
    }
    return Pair.of(digits, bestBit);
  }

  private static int extract(final List<String> formula) {
    Set<Integer> set = Sets.newTreeSet();
    for (final String token : formula) {
      if (token.matches("\\d+")) {
        set.add(Integer.parseInt(token));
      }
    }
    return Integer.parseInt(Joiner.on("").join(set));
  }


  public static void main(String[] args) {
    final int k = args.length > 1 ? Integer.parseInt(args[0]) : 4;

    Pair<Integer, Integer> pair = evaluate(9, k);
    int digits = pair.getLeft(), n = pair.getRight();
    System.out.println("The set of four distinct digits, a < b < c < d, for with the longest set of consecutive positive integers, 1 to "
        + n + ", can be obtained, giving your answer as a string: abcd, is " + digits);

  }
}
