package net.euler.problems;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.euler.utils.Combinations;

import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static org.apache.commons.math3.util.CombinatoricsUtils.factorial;

/**
 * We call a positive integer double pandigital if it uses all the digits 0 to 9 exactly twice (with no leading zero).
 * For example, 40561817703823564929 is one such number.
 *
 * How many double pandigital numbers are divisible by 11?
 *
 * @author Kevin Crosby
 */
public class P491 {
  public static void main(String[] args) {
    long f9 = factorial(9), f10 = factorial(10);
    long[] pow2 = LongStream.iterate(1, i -> 2 * i).limit(11).toArray();

    List<Integer> digits = Lists.newArrayList(0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9);
    Set<Integer> sums = Sets.newHashSet(23, 34, 45, 56, 67);

    Combinations<Integer> combinations = new Combinations<>(digits, 10);
    long number =
        StreamSupport.stream(combinations.spliterator(), false)
            .distinct() // unique
            .filter(combo -> sums.contains(combo.stream().mapToInt(Integer::intValue).sum())) // ensure correct sum
            .mapToLong(combo -> {
              int pairs = 20 - 2 *(int) combo.stream().distinct().count(); // count pairs
              long zeros = combo.stream().filter(z -> z == 0).count(); // count zeros on left
              return (10 - zeros) * f9 * f10 / pow2[pairs];
            })
            .sum();

    System.out.format("There are %d double pandigital numbers that are divisible by 11.\n", number);
  }
}
