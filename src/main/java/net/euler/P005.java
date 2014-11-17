package net.euler;

import lcc.util.Lists;

import java.util.List;

/**
 * Created by kevin on 11/12/14.
 */
public class P005 {
  private static long gcd(long a, long b) {
    return b == 0 ? a : gcd(b, a % b);
  }

  private static long gcd(List<Integer> integers) {
    long gcd = integers.get(0);
    for (int i = 1; i < integers.size(); i++) {
      gcd = gcd(gcd, integers.get(i));
    }
    return gcd;
  }

  private static long lcm(long a, long b) {
    return a * b / gcd(a, b);
  }

  private static long lcm(List<Integer> integers) {
    long lcm = integers.get(0);
    for (int i = 1; i < integers.size(); i++) {
      lcm = lcm(lcm, integers.get(i));
    }
    return lcm;
  }

  public static void main(String[] args) {
    List<Integer> integers = Lists.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

    System.out.println("The smallest positive number that is evenly divisible by all of the numbers from 1 to 20 is " + lcm(integers));
  }
}
