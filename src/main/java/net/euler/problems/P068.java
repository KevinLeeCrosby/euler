package net.euler.problems;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Consider the following "magic" 3-gon ring, filled with the numbers 1 to 6, and each line adding to nine.
 *
 * Working clockwise, and starting from the group of three with the numerically lowest external node (4,3,2 in this
 * example), each solution can be described uniquely. For example, the above solution can be described by the set:
 * 4,3,2; 6,2,1; 5,1,3.
 *
 * It is possible to complete the ring with four different totals: 9, 10, 11, and 12. There are eight solutions in
 * total.
 *
 * Total     Solution Set
 * 9  4,2,3; 5,3,1; 6,1,2
 * 9  4,3,2; 6,2,1; 5,1,3
 * 10 2,3,5; 4,5,1; 6,1,3
 * 10 2,5,3; 6,3,1; 4,1,5
 * 11 1,4,6; 3,6,2; 5,2,4
 * 11 1,6,4; 5,4,2; 3,2,6
 * 12 1,5,6; 2,6,4; 3,4,5
 * 12 1,6,5; 3,5,4; 2,4,6
 *
 * By concatenating each group it is possible to form 9-digit strings; the maximum string for a 3-gon ring is
 * 432621513.
 *
 * Using the numbers 1 to 10, and depending on arrangements, it is possible to form 16- and 17-digit strings. What is
 * the maximum 16-digit string for a "magic" 5-gon ring?
 *
 * @author Kevin Crosby
 */
public class P068 {
  private static int n;
  private static int sum;

  private static List<List<Integer>> findPairs(final Set<Integer> set1, final Set<Integer> set2, final int sum) {
    List<List<Integer>> pairs = Lists.newArrayList();

    for (int a : set1) {
      int b = sum - a;
      if (a != b && set2.contains(b)) {
        pairs.add(Lists.newArrayList(a, b));
      }
    }
    return pairs;
  }

  private static int[] solve() {
    assert n % 2 == 1 : "Code only works for odd numbered n!";

    // initialize
    sum = (5 * n + 3) / 2;
    int[] gon = new int[2 * n];
    Arrays.fill(gon, 0);
    Set<Integer> lowNumbers = Sets.newTreeSet(Collections.reverseOrder());
    Set<Integer> highNumbers = Sets.newTreeSet(Collections.reverseOrder());
    for (int i = 1; i <= n; i++) {
      int j = i + n;
      lowNumbers.add(i);
      highNumbers.add(j);
    }

    // set up
    int j = 0;
    int highNumber = n + 1;

    gon[j] = highNumber;
    Set<Integer> highs = Sets.newTreeSet(highNumbers).descendingSet();
    highs.remove(highNumber);

    int[] testGon = Arrays.copyOf(gon, 2 * n);
    int difference = sum - highNumber;
    for (List<Integer> pair : findPairs(lowNumbers, lowNumbers, difference)) {
      Set<Integer> lows = Sets.newTreeSet(lowNumbers).descendingSet();
      for (int i = j + 1, count = 0; count < 2; i = (i + 2) % (2 * n), count++) {
        int lowNumber = pair.get(count);
        testGon[i] = lowNumber;
        lows.remove(lowNumber);
      }
      int[] newGon = solve(j + 2, testGon, highs, lows);
      if (newGon != null) {
        return newGon;
      }
    }
    return null;
  }

  private static int[] solve(final int j, final int[] gon, final Set<Integer> highNumbers, final Set<Integer> lowNumbers) {
    int[] testGon = Arrays.copyOf(gon, 2 * n);
    if (lowNumbers.isEmpty() && highNumbers.size() == 1) {
      for (int highNumber : highNumbers) {
        int total = 0;
        testGon[j] = highNumber;
        for (int i = j, count = 0; count < 3; i = ((i + 1) | 1) % (2 * n), count++) {
          total += testGon[i];
        }
        if (total == sum) {
          return testGon;
        }
      }
      return null;
    }
    int difference = sum - gon[j + 1];
    int i = (j + 3) % (2 * n);
    for (List<Integer> pair : findPairs(highNumbers, lowNumbers, difference)) {
      Set<Integer> highs = Sets.newTreeSet(highNumbers).descendingSet();
      Set<Integer> lows = Sets.newTreeSet(lowNumbers).descendingSet();
      int highNumber = pair.get(0);
      int lowNumber = pair.get(1);
      testGon[j] = highNumber;
      testGon[i] = lowNumber;
      highs.remove(highNumber);
      lows.remove(lowNumber);
      int[] newGon = solve(j + 2, testGon, highs, lows);
      if (newGon != null) {
        return newGon;
      }
    }
    return null;
  }

  private static String asString(int[] gon) {
    List<String> groups = Lists.newArrayList();
    for (int j = 0; j < 2 * n; j += 2) {
      List<String> group = Lists.newArrayList();
      for (int i = j, count = 0; count < 3; i = ((i + 1) | 1) % (2 * n), count++) {
        group.add(String.format("%d", gon[i]));
      }
      groups.add(Joiner.on(",").join(group));
    }
    return Joiner.on("; ").join(groups);
  }

  private static String catenate(int[] gon) {
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j < 2 * n; j += 2) {
      for (int i = j, count = 0; count < 3; i = ((i + 1) | 1) % (2 * n), count++) {
        sb.append(gon[i]);
      }
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    n = args.length > 0 ? Integer.parseInt(args[0]) | 1 : 5; // can go up to 9

    int[] gon = solve();
    System.out.println("Found " + asString(gon));

    String catenate = catenate(gon);
    System.out.println("The maximum " + catenate.length() + "-digit string for a \"magic\" " + n + "-gon ring is " + catenate);
  }
}
