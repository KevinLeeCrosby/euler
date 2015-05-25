package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.Primes;

import java.util.Set;

/**
 * Created by Kevin on 11/27/2014.
 */
public class P023 {
  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 20161;
      // Every integer greater than 20161 can be written as the sum of two abundant numbers.
      // http://en.wikipedia.org/wiki/Abundant_number#Properties
      // http://mathworld.wolfram.com/AbundantNumber.html
    }

    Primes p = Primes.getInstance();
    Set<Integer> abundants = Sets.newTreeSet();
    Set<Integer> abundantSums = Sets.newTreeSet(); // abundant Sums
    for (int number = 1; number <= limit; number++) {
      if (number < p.aliquotSum(number)) {
        abundants.add(number);
        for (int abundant : abundants) {
          abundantSums.add(abundant + number);
        }
      }
    }

    int sum = 0;
    for (int number = 1; number <= limit; number++) {
      if (!abundantSums.contains(number)) {
        sum += number;
      }
    }

    System.out.println("The sum of all the positive integers which cannot be written as the sum of two abundant numbers is " + sum);
  }
}
