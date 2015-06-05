package net.euler.problems;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;

/**
 * A natural number, N, that can be written as the sum and product of a given set of at least two natural numbers, {a1,
 * a2, ... , ak} is called a product-sum number: N = a1 + a2 + ... + ak = a1 × a2 × ... × ak.
 *
 * For example, 6 = 1 + 2 + 3 = 1 × 2 × 3.
 *
 * For a given set of size, k, we shall call the smallest N with this property a minimal product-sum number. The
 * minimal product-sum numbers for sets of size, k = 2, 3, 4, 5, and 6 are as follows.
 *
 * k=2: 4 = 2 × 2 = 2 + 2
 * k=3: 6 = 1 × 2 × 3 = 1 + 2 + 3
 * k=4: 8 = 1 × 1 × 2 × 4 = 1 + 1 + 2 + 4
 * k=5: 8 = 1 × 1 × 2 × 2 × 2 = 1 + 1 + 2 + 2 + 2
 * k=6: 12 = 1 × 1 × 1 × 1 × 2 × 6 = 1 + 1 + 1 + 1 + 2 + 6
 *
 * Hence for 2≤k≤6, the sum of all the minimal product-sum numbers is 4+6+8+12 = 30; note that 8 is only counted once
 * in the sum.
 *
 * In fact, as the complete set of minimal product-sum numbers for 2≤k≤12 is {4, 6, 8, 12, 15, 16}, the sum is 61.
 *
 * What is the sum of all the minimal product-sum numbers for 2≤k≤12000?
 *
 * @author Kevin Crosby
 */
public class P088 {
  private static final Map<Integer, Integer> NUMBERS = Maps.newHashMap();
  private static int LIMIT;

  private static int sum() {
    productSum(1, 1, 1, 2);

    int sum = 0;
    for (final int value : Sets.newHashSet(NUMBERS.values())) {
      sum += value;
    }
    return sum;
  }

  private static void productSum(final int product, final int sum, final int count, final int start) {
    int k = product - sum + count;
    if (k <= LIMIT) {
      if (k != 1 && (!NUMBERS.containsKey(k) || NUMBERS.get(k) > product)) NUMBERS.put(k, product);
      for (int divisor = start; divisor <= 2 * LIMIT / product; ++divisor) {
        productSum(product * divisor, sum + divisor, count + 1, divisor);
      }
    }
  }

  public static void main(String[] args) {
    LIMIT = args.length > 0 ? Integer.parseInt(args[0]) : 12000;

    System.out.println("The sum of all the minimal product-sum numbers for 2 ≤ k ≤ " + LIMIT + " is " + sum());
  }
}
