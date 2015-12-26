package net.euler.problems;

import net.euler.utils.MathUtils;

import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static net.euler.utils.MathUtils.pow;
import static net.euler.utils.MathUtils.sqrt;

/**
 * Find the unique positive integer whose square has the form 1_2_3_4_5_6_7_8_9_0, where each "_" is a single digit.
 *
 * @author Kevin Crosby
 */
public class P206 {
  private static final Pattern pattern = Pattern.compile("1\\d2\\d3\\d4\\d5\\d6\\d7\\d8\\d9"); // implicit 00 at end

  public static void main(String[] args) {
    long root = LongStream.rangeClosed(sqrt(10203040506070809L), sqrt(19293949596979899L))
        .filter(n -> n % 10 == 3 || n % 10 == 7)
        .map(n -> pow(n, 2))
        .mapToObj(String::valueOf)
        .filter(s -> pattern.matcher(s).find())
        .mapToLong(Long::valueOf)
        .map(MathUtils::sqrt)
        .map(n -> 10 * n)
        .findFirst()
        .getAsLong();
    System.out.format("The unique positive integer whose square has the form 1_2_3_4_5_6_7_8_9_0, is %d.\n", root);
  }
}
