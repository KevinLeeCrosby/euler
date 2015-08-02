package net.euler.problems;

import com.google.common.collect.ImmutableMap;
import net.euler.utils.MathUtils;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;

/**
 * The Fibonacci sequence is defined by the recurrence relation:
 *
 * Fn = Fn−1 + Fn−2, where F1 = 1 and F2 = 1.
 * It turns out that F541, which contains 113 digits, is the first Fibonacci number for which the last nine digits are
 * 1-9 pandigital (contain all the digits 1 to 9, but not necessarily in order). And F2749, which contains 575 digits,
 * is the first Fibonacci number for which the first nine digits are 1-9 pandigital.
 *
 * Given that Fk is the first Fibonacci number for which the first nine digits AND the last nine digits are 1-9
 * pandigital, find k.
 *
 * @author Kevin Crosby
 */
public class P104 {
  private static final double PHI = (1 + sqrt(5)) / 2, LOG_PHI = Math.log10(PHI), LOG_SQRT_5 = Math.log10(5) / 2;
  private static final Map<Integer, Integer> DIGIT_MAP = new ImmutableMap.Builder<Integer, Integer>()
      .put(0, 1).put(1, 2).put(2, 3).put(3, 5).put(4, 7).put(5, 11).put(6, 13).put(7, 17).put(8, 19).put(9, 23)
      .build();
  private static int PRIMORIAL, LIMIT, MODULUS;

  private static class LowFibonacci implements Iterable<Integer> {
    @Override
    public Iterator<Integer> iterator() {
      return new LowFibonacciIterator();
    }

    private class LowFibonacciIterator implements Iterator<Integer> {
      private final Queue<Long> queue;

      private LowFibonacciIterator() {
        queue = new ArrayDeque<Long>() {{
          add(0L);
          add(1L);
        }};
      }

      @Override
      public boolean hasNext() {
        return true;
      }

      @Override
      public Integer next() {
        Long f = queue.remove();
        queue.add((f + queue.peek()) % MODULUS);
        return f.intValue();
      }
    }
  }

  private static int primorial(final int n) {
    assert n < 10 : "Cannot compute primorial " + n + "!";
    if (!DIGIT_MAP.containsKey(n)) {
      return 1;
    }
    return DIGIT_MAP.get(n) * primorial(n - 1);
  }

  // approximate formula
  public static int highFibonacci(final int n) {
    double logFibonacci = n * LOG_PHI - LOG_SQRT_5;
    int digits = (int) (logFibonacci) + 1;
    return (int) (digits > LIMIT ? Math.pow(10, logFibonacci - digits + LIMIT) : round(Math.pow(10, logFibonacci)));
  }

  private static boolean isPandigital(final int n) {
    if (MathUtils.log10(n) + 1 != LIMIT) {
      return false;
    }
    int product = 1;
    int number = n;
    while (number > 0) {
      int digit = number % 10;
      number /= 10;
      product *= DIGIT_MAP.get(digit);
    }
    return product == PRIMORIAL;
  }

  public static void main(String[] args) {
    LIMIT = args.length > 0 ? Integer.parseInt(args[0]) : 9;
    PRIMORIAL = primorial(LIMIT);
    MODULUS = (int) MathUtils.pow(10, LIMIT);

    LowFibonacci lowFibonaccis = new LowFibonacci();
    int k = -1;
    for (final int lowFibonacci : lowFibonaccis) {
      ++k;
      if (!isPandigital(lowFibonacci)) {
        continue;
      }
      int highFibonacci = highFibonacci(k);
      if (isPandigital(highFibonacci)) {
        break;
      }
    }
    System.out.printf("F%d is the first Fibonacci number for which the first nine digits AND the last nine digits are 1-9 pandigital.", k);
  }
}
