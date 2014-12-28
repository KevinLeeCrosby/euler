package net.euler;

import java.io.IOException;
import java.io.InputStream;
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
 * Find the maximum total from top to bottom in triangle.txt (right click and 'Save Link/Target As...'), a 15K text file containing a triangle with one-hundred rows.
 * <p/>
 * NOTE: This is a much more difficult version of Problem 18. It is not possible to try every route to solve this problem, as there are 299 altogether! If you could check one trillion (1012) routes every second it would take over twenty billion years to check them all. There is an efficient algorithm to solve it. ;o)
 *
 * @author Kevin Crosby
 */
public class P067 {
  public static void main(String[] args) throws IOException {
    String file = "/net/euler/p067.txt";
    InputStream is = P018.class.getResourceAsStream(file);
    List<List<Integer>> triangle = P018.loadTriangle(is);

    int total = P018.getMaxTotal(triangle);

    System.out.println("The maximum total from top to bottom of the triangle is " + total);
  }
}
