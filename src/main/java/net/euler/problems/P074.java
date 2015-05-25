package net.euler.problems;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The number 145 is well known for the property that the sum of the factorial of its digits is equal to 145:
 *
 * 1! + 4! + 5! = 1 + 24 + 120 = 145
 *
 * Perhaps less well known is 169, in that it produces the longest chain of numbers that link back to 169; it turns out
 * that there are only three such loops that exist:
 *
 * 169 → 363601 → 1454 → 169
 * 871 → 45361 → 871
 * 872 → 45362 → 872
 *
 * It is not difficult to prove that EVERY starting number will eventually get stuck in a loop. For example,
 *
 * 69 → 363600 → 1454 → 169 → 363601 (→ 1454)
 * 78 → 45360 → 871 → 45361 (→ 871)
 * 540 → 145 (→ 145)
 *
 * Starting with 69 produces a chain of five non-repeating terms, but the longest non-repeating chain with a starting
 * number below one million is sixty terms.
 *
 * How many chains, with a starting number below one million, contain exactly sixty non-repeating terms?
 *
 * @author Kevin Crosby
 */
public class P074 {
  private static List<Integer> factorial = Lists.newArrayList(1);
  private static Map<Integer, Integer> chainLengths = new HashMap<Integer, Integer>() {{
    put(169, 3);
    put(363601, 3);
    put(1454, 3);
    put(871, 2);
    put(45361, 2);
    put(872, 2);
    put(45362, 2);
  }};

  private static int factorial(final int n) {
    if (factorial.size() <= n) {
      factorial.add(n * factorial(n - 1));
    }
    return factorial.get(n);
  }

  private static int digitFactorialSum(final int number) {
    if (number == 0) return 1;
    int sum = 0;
    int n = number;
    while (n > 0) {
      int digit = n % 10;
      n /= 10;
      sum += factorial(digit);
    }
    return sum;
  }

  private static int getChainLength(final int n) {
    if (!chainLengths.containsKey(n)) {
      int d = digitFactorialSum(n);
      if (n == d) {
        chainLengths.put(n, 1);  // i.e. is factorion
      } else {
        chainLengths.put(n, getChainLength(d) + 1);
      }
    }
    return chainLengths.get(n);
  }

  public static void main(String[] args) {
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;
    final int length = args.length > 1 ? Integer.parseInt(args[1]) : 60;

    int count = 0;
    for (int n = 0; n < limit; n++) {
      int chainLength = getChainLength(n);
      if (chainLength == length) {
        System.out.println(n + " has a length of " + length);
        count++;
      }
    }

    System.out.println("There are " + count + " chains, with a starting number below " + limit
        + ", that contain exactly " + length + " sixty non-repeating terms.");
  }
}
