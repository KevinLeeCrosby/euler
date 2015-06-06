package net.euler.problems;

import com.google.common.collect.Maps;
import net.euler.utils.Counter;

import java.util.HashMap;
import java.util.Map;

/**
 * A number chain is created by continuously adding the square of the digits in a number to form a new number until it
 * has been seen before.
 *
 * For example,
 *
 * 44 → 32 → 13 → 10 → 1 → 1
 * 85 → 89 → 145 → 42 → 20 → 4 → 16 → 37 → 58 → 89
 *
 * Therefore any chain that arrives at 1 or 89 will become stuck in an endless loop. What is most amazing is that EVERY
 * starting number will eventually arrive at 1 or 89.
 *
 * How many starting numbers below ten million will arrive at 89?
 *
 * @author Kevin Crosby
 */
public class P092 {
  private static final Map<Integer, Integer> SQUARES = Maps.newHashMap();
  private static final Map<Integer, Integer> CHAINS = new HashMap<Integer, Integer>() {{
    put(1, 1);
    put(89, 89);
  }};

  private static int square(final int digit) {
    if (!SQUARES.containsKey(digit)) {
      SQUARES.put(digit, digit * digit);
    }
    return SQUARES.get(digit);
  }

  private static int sumSquareOfDigits(int number) {
    int sumSquares = 0;
    while (number > 0) {
      int digit = number % 10;
      number /= 10;
      sumSquares += square(digit);
    }
    return sumSquares;
  }

  private static int chain(int number) {
    if(!CHAINS.containsKey(number)) {
      int next = sumSquareOfDigits(number);
      CHAINS.put(number, chain(next));
    }
    return CHAINS.get(number);
  }

  public static void main(String[] args) {
    final int LIMIT = args.length > 0 ? Integer.parseInt(args[0]) : 10000000;

    Counter<Integer> counter = new Counter<>();
    for (int i = 1; i < LIMIT; ++i) {
      counter.increment(chain(i));
    }

    for (Map.Entry<Integer, Integer> entry : counter.descendingSortByCount()) {
      System.out.println(entry.getValue() + " numbers below ten million will arrive at " + entry.getKey());
    }
  }
}
