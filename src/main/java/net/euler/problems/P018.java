package net.euler.problems;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * By starting at the top of the triangle below and moving to adjacent numbers on the row below, the maximum total from top to bottom is 23.
 * <p/>
 * 3
 * 7 4
 * 2 4 6
 * 8 5 9 3
 * <p/>
 * That is, 3 + 7 + 4 + 9 = 23.
 * <p/>
 * Find the maximum total from top to bottom of the triangle below:
 * <p/>
 * 75
 * 95 64
 * 17 47 82
 * 18 35 87 10
 * 20 04 82 47 65
 * 19 01 23 75 03 34
 * 88 02 77 73 07 63 67
 * 99 65 04 28 06 16 70 92
 * 41 41 26 56 83 40 80 70 33
 * 41 48 72 33 47 32 37 16 94 29
 * 53 71 44 65 25 43 91 52 97 51 14
 * 70 11 33 28 77 73 17 78 39 68 17 57
 * 91 71 52 38 17 14 91 43 58 50 27 29 48
 * 63 66 04 68 89 53 67 30 73 16 69 87 40 31
 * 04 62 98 27 23 09 70 98 73 93 38 53 60 04 23
 * <p/>
 * NOTE: As there are only 16384 routes, it is possible to solve this problem by trying every route. However, Problem 67, is the same challenge with a triangle containing one-hundred rows; it cannot be solved by brute force, and requires a clever method! ;o)
 *
 * @author Kevin Crosby
 */
public class P018 {
  public static List<List<Integer>> loadTriangle(InputStream is) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    List<List<Integer>> triangle = Lists.newArrayList();

    String line;
    while ((line = br.readLine()) != null) {
      List<Integer> row = Lists.newArrayList();
      for (String string : line.split("\\s")) {
        row.add(Integer.parseInt(string));
      }
      triangle.add(row);
    }
    br.close();

    return triangle;
  }

  public static int getMaxTotal(List<List<Integer>> triangle) {
    List<List<Integer>> t = Lists.newArrayList(triangle);

    // dynamic programming
    for (int row = t.size() - 2; row >= 0; row--) {
      for (int col = 0; col < t.get(row).size(); col++) {
        int here = t.get(row).get(col);
        int left = t.get(row + 1).get(col);
        int right = t.get(row + 1).get(col + 1);
        int max = Math.max(left, right);
        t.get(row).set(col, here + max); // update here
      }
    }

    return t.get(0).get(0);
  }


  public static void main(String[] args) throws IOException {
    String file = "/net/euler/problems/p018.txt";
    InputStream is = P018.class.getResourceAsStream(file);
    List<List<Integer>> triangle = loadTriangle(is);

    int total = getMaxTotal(triangle);

    System.out.println("The maximum total from top to bottom of the triangle is " + total);
  }
}
