package net.euler.problems;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * A row of five black square tiles is to have a number of its tiles replaced with coloured oblong tiles chosen from red
 * (length two), green (length three), or blue (length four).
 *
 * If red tiles are chosen there are exactly seven ways this can be done.
 *
 * If green tiles are chosen there are three ways.
 *
 * And if blue tiles are chosen there are two ways.
 *
 * Assuming that colours cannot be mixed there are 7 + 3 + 2 = 12 ways of replacing the black tiles in a row measuring
 * five units in length.
 *
 * How many different ways can the black tiles in a row measuring fifty units in length be replaced if colours cannot be
 * mixed and at least one coloured tile must be used?
 *
 * NOTE: This is related to Problem 117.
 *
 * @author Kevin Crosby
 */
public class P116 {
  private final Map<Integer, Map<Integer, Long>> a;
  private final int limit;

  private P116(int limit) {
    this.limit = limit;
    this.a = Maps.newHashMap();
  }

  private long a(int m, int t) {
    a.putIfAbsent(m, Maps.newHashMap());
    return a.get(m).computeIfAbsent(t, n -> n < m ? 1L : a(m, n - 1) + a(m, n - m));
  }

  private long ways(int m, int t) {
    return a(m, t) - 1;
  }

  private long solve() {
    long sum = 0;
    for (int m = 2; m <= 4; ++m) {
      long ways = ways(m, limit);
      sum += ways;
      System.out.format("%d : %d ways\n", m, ways);
    }
    return sum;
  }

  public static void main(String[] args) {
    //final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 5; // 7 + 3 + 2 = 12
    final int limit = args.length > 0 ? Integer.parseInt(args[0]) : 50; // 20492570929

    P116 problem = new P116(limit);
    long sum = problem.solve();
    System.out.format(
        "There are %d different ways that the black tiles in a row measuring %d units in length be replaced if colours cannot be mixed and at least one coloured tile must be used.\n",
        sum, limit);
  }
}
