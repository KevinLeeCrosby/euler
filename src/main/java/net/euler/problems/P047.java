package net.euler.problems;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.euler.utils.Primes;

import java.util.Collections;
import java.util.List;

/**
 * The first two consecutive numbers to have two distinct prime factors are:
 * <p/>
 * 14 = 2 × 7
 * 15 = 3 × 5
 * <p/>
 * The first three consecutive numbers to have three distinct prime factors are:
 * <p/>
 * 644 = 2² × 7 × 23
 * 645 = 3 × 5 × 43
 * 646 = 2 × 17 × 19.
 * <p/>
 * Find the first four consecutive integers to have four distinct prime factors. What is the first of these numbers?
 *
 * @author Kevin Crosby
 */
public class P047 {
  private static Primes primes = Primes.getInstance();

  public static void main(String[] args) {
    int noDistinctFactors;
    if (args.length > 0) {
      noDistinctFactors = Integer.parseInt(args[0]);
    } else {
      noDistinctFactors = 4;
    }

    int count = 0;
    long number;
    for (number = 1; count != noDistinctFactors; ) {
      count = (Sets.newHashSet(primes.factor(++number)).size() == noDistinctFactors ? count + 1 : 0);
    }

    for (long n = number - noDistinctFactors + 1; n <= number; n++) {
      List<Long> factors = primes.factor(n);
      List<String> strings = Lists.newArrayList();
      for (Long factor : Sets.newTreeSet(factors)) {
        int exponent = Collections.frequency(factors, factor);
        strings.add(exponent == 1 ? factor.toString() : factor + "^" + exponent);
      }
      System.out.println(n + " = " + Joiner.on(" × ").join(strings));
    }
  }
}
