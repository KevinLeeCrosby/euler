package net.euler.problems;

import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import static java.lang.Math.hypot;
import static net.euler.utils.MathUtils.sqrt;

/**
 * NOTE: This problem is a significantly more challenging version of Problem 81.
 *
 * In the 5 by 5 matrix below, the minimal path sum from the top left to the bottom right, by moving left, right, up,
 * and down, is indicated in bold red and is equal to 2297.
 *
 * Find the minimal path sum, in matrix.txt (right click and "Save Link/Target As..."), a 31K text file containing a 80
 * by 80 matrix, from the top left to the bottom right by moving left, right, up, and down.
 *
 * @author Kevin Crosby
 */
public class P083 {
  protected static int[][] matrix;
  protected static int m;
  protected static int n;
  protected static int[][] h; // future cost (heuristic)
  protected static boolean[][] closed; // true if closed
  protected static PriorityQueue<Node> open;

  protected static class Node implements Comparator<Node> {
    protected final int g;
    protected final int h;
    protected final int i;
    protected final int j;

    Node() {
      this(0, 0, 0, 0);
    }

    Node(final int g, final int h, final int i, final int j) {
      this.g = g;
      this.h = h;
      this.i = i;
      this.j = j;
    }

    @Override
    public int compare(final Node n1, final Node n2) {
      int n1f = n1.g + n1.h, n2f = n2.g + n2.h;
      int result = n1f < n2f ? -1 : n1f > n2f ? 1 : 0; // ascending order
      if (result == 0) {
        double n1d = hypot(m - n1.i - 1, n - n1.j - 1), n2d = hypot(m - n2.i - 1, n - n2.j - 1);
        result = n1d < n2d ? -1 : n1d > n2d ? 1 : 0; // ascending order
      }
      return result;
    }
  }

  protected static List<Node> children(final Node node) {
    List<Node> children = Lists.newArrayList();
    int r = node.i;
    int c = node.j;

    for (int dr = -1; dr <= +1; dr += 2) {
      int rp = r + dr;
      if (rp >= 0 && rp < m && !closed[rp][c]) {
        int g = node.g + matrix[rp][c]; // current cost (actualized)
        Node child = new Node(g, h[rp][c], rp, c);
        children.add(child);
      }
    }

    for (int dc = -1; dc <= +1; dc += 2) {
      int cp = c + dc;
      if (cp >= 0 && cp < n && !closed[r][cp]) {
        int g = node.g + matrix[r][cp]; // current cost (actualized)
        Node child = new Node(g, h[r][cp], r, cp);
        children.add(child);
      }
    }

    return children;
  }

  protected static void loadMatrix(final InputStream is) {
    Scanner fileScan = new Scanner(is);

    List<List<Integer>> grid = Lists.newArrayList();
    while (fileScan.hasNext()) {
      List<Integer> line = Lists.newArrayList();
      Scanner lineScan = new Scanner(fileScan.nextLine()).useDelimiter(",");
      while (lineScan.hasNextInt()) {
        int element = lineScan.nextInt();
        line.add(element);
      }
      grid.add(line);
    }
    fileScan.close();

    m = grid.size();
    n = grid.get(0).size();
    matrix = new int[m][n];
    h = new int[m][n];
    closed = new boolean[m][n];
    open = new PriorityQueue<>((int) sqrt(m * n), new Node());

    for (int r = 0; r < m; r++) {
      List<Integer> row = grid.get(r);
      for (int c = 0; c < n; c++) {
        matrix[r][c] = row.get(c);
        closed[r][c] = false;
        h[r][c] = 0;
      }
    }

    // TODO:  determine why heuristic doesn't work
    //h[m - 1][n - 1] = matrix[m - 1][n - 1];
    //for (int rc = m + n - 3; rc >= 0; rc--) { // sum of row and column indices
    //  for (int r = max(0, rc - n + 1); r <= min(rc, m - 1); r++) {
    //    int c = rc - r;
    //    int down = r + 1 <= m - 1 ? h[r + 1][c] : Integer.MAX_VALUE;
    //    int right = c + 1 <= n - 1 ? h[r][c + 1] : Integer.MAX_VALUE;
    //    h[r][c] = matrix[r][c] + min(down, right);
    //  }
    //}
    //for (int r = 0; r < m; r++) {
    //  for (int c = 0; c < n; c++) {
    //    h[r][c] -= matrix[r][c];
    //  }
    //}
  }

  protected static int aStar() {
    int r = 0, c = 0;
    int g = matrix[r][c]; // current cost (actualized)
    Node node = new Node(g, h[r][c], r, c);
    open.add(node);
    boolean done = false;
    while (!done && !open.isEmpty()) {
      node = open.remove();
      r = node.i;
      c = node.j;
      closed[r][c] = true;
      done = r == m - 1 && c == n - 1;
      if (!done) {
        open.addAll(children(node));
      }
    }
    if (done) {
      return node.g;
    }
    System.err.println("SOLUTION NOT FOUND!");
    return Integer.MAX_VALUE;
  }

  public static void main(String[] args) {
    String file = "/net/euler/problems/p083.txt";
    InputStream is = P083.class.getResourceAsStream(file);
    loadMatrix(is);

    int minimalPathSum = aStar();
    System.out.println("The minimal path sum in " + m + " by " + n
        + " matrix, from the top left to the bottom right by moving left, right, up, and down is " + minimalPathSum);
  }
}
