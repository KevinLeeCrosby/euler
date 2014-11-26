package net.euler;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by kevin on 11/26/14.
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
    String file = "/net/euler/problem18.txt";
    InputStream is = P018.class.getResourceAsStream(file);
    List<List<Integer>> triangle = loadTriangle(is);

    int total = getMaxTotal(triangle);

    System.out.println("The maximum total from top to bottom of the triangle is " + total);
  }
}
