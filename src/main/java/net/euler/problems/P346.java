package net.euler.problems;

import com.google.common.collect.Sets;

import java.util.Set;

import static net.euler.utils.MathUtils.log2;
import static net.euler.utils.MathUtils.logBase;

/**
 * The number 7 is special, because 7 is 111 written in base 2, and 11 written in base 6
 * (i.e. 7_10 = 11_6 = 111_2). In other words, 7 is a repunit in at least two bases b > 1.
 *
 * We shall call a positive integer with this property a strong repunit. It can be verified that there are 8 strong
 * repunits below 50: {1,7,13,15,21,31,40,43}.
 * Furthermore, the sum of all strong repunits below 1000 equals 15864.
 * Find the sum of all strong repunits below 10^12.
 *
 * @author Kevin Crosby
 */
public class P346 {
  // see Goormaghtigh conjecture
  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 1000000000000L;

    Set<Long> strongRepunits = Sets.newHashSet(1L);

    long sum = 1;
    for(long base = 2, maxExponent = log2(limit + 1);
        maxExponent > 2;
        ++base, maxExponent = logBase((base - 1) * limit + 1, base)) {
      for(long exponent = 3, repunit = base * (base + 1) + 1; exponent <= maxExponent; ++exponent, repunit = base * repunit + 1) {
        if(!strongRepunits.contains(repunit)) {
          strongRepunits.add(repunit);
          sum += repunit;
        }
      }
    }

    System.out.format("The sum of all strong repunits below %d is %d.\n", limit, sum);
  }
}
