package net.euler.problems;

import com.google.common.collect.Maps;
import net.euler.utils.NewPrimes;

import java.util.Map;
import java.util.stream.StreamSupport;

import static net.euler.utils.MathUtils.log10;

/**
 * Sam and Max are asked to transform two digital clocks into two "digital root" clocks.
 * A digital root clock is a digital clock that calculates digital roots step by step.
 *
 * When a clock is fed a number, it will show it and then it will start the calculation, showing all the intermediate
 * values until it gets to the result.
 * For example, if the clock is fed the number 137, it will show: "137" → "11" → "2" and then it will go black, waiting
 * for the next number.
 *
 * Every digital number consists of some light segments: three horizontal (top, middle, bottom) and four vertical
 * (top-left, top-right, bottom-left, bottom-right).
 * Number "1" is made of vertical top-right and bottom-right, number "4" is made by middle horizontal and vertical
 * top-left, top-right and bottom-right. Number "8" lights them all.
 *
 * The clocks consume energy only when segments are turned on/off.
 * To turn on a "2" will cost 5 transitions, while a "7" will cost only 4 transitions.
 *
 * Sam and Max built two different clocks.
 *
 * Sam's clock is fed e.g. number 137: the clock shows "137", then the panel is turned off, then the next number ("11")
 * is turned on, then the panel is turned off again and finally the last number ("2") is turned on and, after some
 * time,
 * off.
 * For the example, with number 137, Sam's clock requires:
 *
 * "137" : (2 + 5 + 4) × 2 = 22 transitions ("137" on/off).
 * "11"  : (2 + 2) × 2 = 8 transitions ("11" on/off).
 * "2"   : (5) × 2 = 10 transitions ("2" on/off).
 * For a grand total of 40 transitions.
 * Max's clock works differently. Instead of turning off the whole panel, it is smart enough to turn off only those
 * segments that won't be needed for the next number.
 * For number 137, Max's clock requires:
 *
 * "137" : 2 + 5 + 4 = 11 transitions ("137" on)
 * 7 transitions (to turn off the segments that are not needed for number "11").
 *
 * "11" : 0 transitions (number "11" is already turned on correctly)
 * 3 transitions (to turn off the first "1" and the bottom part of the second "1";
 * the top part is common with number "2").
 *
 * "2" : 4 transitions (to turn on the remaining segments in order to get a "2")
 * 5 transitions (to turn off number "2").
 * For a grand total of 30 transitions.
 *
 * Of course, Max's clock consumes less power than Sam's one.
 * The two clocks are fed all the prime numbers between A = 10^7 and B = 2×10^7.
 * Find the difference between the total number of transitions needed by Sam's clock and that needed by Max's one.
 *
 * @author Kevin Crosby
 */
public class P315 {
  private static final char[] segments = {0x7E, 0x30, 0x6D, 0x79, 0x33, 0x5B, 0x5F, 0x72, 0x7F, 0x7B};

  private static Map<Character, Integer> bitCounts = Maps.newHashMap();

  /**
   * Count bits for segment.
   * https://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetKernighan
   */
  private static int bitCounts(final char segs) {
    if(!bitCounts.containsKey(segs)) {
      int c;
      char v = segs;
      for(c = 0; v > 0; ++c) {
        v &= v - 1;
      }
      bitCounts.put(segs, c);
    }
    return bitCounts.get(segs);
  }

  /**
   * Count bits for segments in inary coded decimal.
   */
  private static int bcdCounts(final long now) {
    int count = 0;
    long n = now;
    while(n > 0) {
      char segs = segments[(int) (n % 10)];
      count += bitCounts(segs);
      n /= 10;
    }
    return count;
  }

  /**
   * Use Sam's method.
   */
  private static int sam(final long now) {
    return bcdCounts(now) << 1;
  }

  /**
   * Use Max's method.
   */
  private static int max(final long prev, final long next) {
    int transitions = 0, delta;
    if(prev < 0) {
      delta = -1;
    } else if(next < 0) {
      delta = +1;
    } else {
      delta = (int) (log10(prev) - log10(next));
    }
    long p = prev, n = next;
    while(p > 0 && n > 0) {
      char segs = (char) (segments[(int) (n % 10)] ^ segments[(int) (p % 10)]);
      transitions += bitCounts(segs);
      p /= 10;
      n /= 10;
    }
    if(delta > 0) {
      transitions += bcdCounts(p);
    } else if(delta < 0) {
      transitions += bcdCounts(n);
    }
    return transitions;
  }

  private static Map<Long, Long> next = Maps.newHashMap();

  /**
   * Compute intermediate step in digital root.
   */
  private static long next(final long prev) {
    if(!next.containsKey(prev)) {
      long n = 0;
      long p = prev;
      while(p > 0) {
        n += p % 10;
        p /= 10;
      }
      if(log10(prev) + 1 > 3) { // i.e. no keys with more than 3 digits
        return n;
      }
      next.put(prev, prev != n ? n : -1);
    }
    return next.get(prev);
  }

  /**
   * Compute difference between Sam's and Max's method.
   */
  private static long difference(final long now) {
    long p = -1, difference = 0;
    for(long n = now; n > 0; n = next(n)) {
      difference += sam(n) - max(p, n);
      p = n;
    }
    difference -= max(p, -1);
    return difference;
  }

  public static void main(String[] args) {
    final long a = args.length > 0 ? Long.parseLong(args[0]) : 10000000;
    final long b = args.length > 1 ? Long.parseLong(args[1]) : 20000000;

    long difference =
        StreamSupport.stream(NewPrimes.getInstance(b).spliterator(), false)
            .filter(p -> p >= a)          // set lower bound
            .mapToLong(Long::longValue)   // convert to something we can sum
            .map(P315::difference)        // find difference for this number
            .sum();                       // add them together
    System.out.format("The difference between the total number of transitions needed by Sam's clock and that needed by Max's one is %d.\n", difference);
  }
}
