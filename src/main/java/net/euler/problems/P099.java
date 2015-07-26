package net.euler.problems;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static java.lang.Math.log10;

/**
 * Comparing two numbers written in index form like 2^11 and 3^7 is not difficult, as any calculator would confirm that
 * 2^11 = 2048 < 3^7 = 2187.
 *
 * However, confirming that 632382^518061 > 519432^525806 would be much more difficult, as both numbers contain over
 * three million digits.
 *
 * Using base_exp.txt (right click and 'Save Link/Target As...'), a 22K text file containing one thousand lines with a
 * base/exponent pair on each line, determine which line number has the greatest numerical value.
 *
 * NOTE: The first two lines in the file represent the numbers in the example given above.
 *
 * @author Kevin Crosby
 */
public class P099 {
  private static TreeMap<Pair<Long, Long>, Integer> loadPairs(final InputStream is) throws IOException {
    Scanner fileScan = new Scanner(is);

    TreeMap<Pair<Long, Long>, Integer> map = Maps.newTreeMap(new Comparator<Pair<Long, Long>>() {
      @Override
      public int compare(final Pair<Long, Long> pair1, final Pair<Long, Long> pair2) {
        long b1 = pair1.getLeft(), e1 = pair1.getRight();
        long b2 = pair2.getLeft(), e2 = pair2.getRight();
        double result = log10(b2) * e2 - log10(b1) * e1; // descending order
        return result < 0 ? -1 : result > 0 ? +1 : 0;
      }
    });

    int lineNumber = 1;
    while (fileScan.hasNext()) {
      Scanner lineScan = new Scanner(fileScan.nextLine());
      while (lineScan.hasNext()) {
        String[] split = lineScan.next().split(",");
        Pair<Long, Long> pair = Pair.of(Long.parseLong(split[0]), Long.parseLong(split[1]));
        map.put(pair, lineNumber++);
      }
    }
    fileScan.close();

    return map;
  }

  public static void main(String[] args) throws IOException {
    final String file = "/net/euler/problems/p099.txt";
    final InputStream is = P099.class.getResourceAsStream(file);
    TreeMap<Pair<Long, Long>, Integer> map = loadPairs(is);

    Map.Entry<Pair<Long, Long>, Integer> entry = map.firstEntry();
    Pair<Long, Long> pair = entry.getKey();
    long base = pair.getLeft(), exponent = pair.getRight();
    int lineNumber = entry.getValue();
    System.out.println("The line number with the greatest numerical value is line # " + lineNumber + ": "
        + base + "^" + exponent);
  }
}
