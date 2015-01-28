package net.euler;

import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import static net.euler.MathUtils.sqrt;

/**
 * NOTE: This problem is a more challenging version of Problem 81.
 *
 * The minimal path sum in the 5 by 5 matrix below, by starting in any cell in the left column and finishing in any
 * cell
 * in the right column, and only moving up, down, and right, is indicated in red and bold; the sum is equal to 994.
 *
 * Find the minimal path sum, in matrix.txt (right click and "Save Link/Target As..."), a 31K text file containing a 80
 * by 80 matrix, from the left column to the right column.
 *
 * @author Kevin Crosby
 */
public class P082 {
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
        double n1d = n - n1.j - 1, n2d = n - n2.j - 1;
        result = n1d < n2d ? -1 : n1d > n2d ? 1 : 0; // ascending order
      }
      return result;
    }
  }

  protected static List<Node> children(final Node node) {
    List<Node> children = Lists.newArrayList();
    int r = node.i;
    int c = node.j, dc = 1, cp = c + dc;

    for (int dr = -1; dr <= +1; dr += 2) {
      int rp = r + dr;
      if (rp >= 0 && rp < m && !closed[rp][c]) {
        int g = node.g + matrix[rp][c]; // current cost (actualized)
        Node child = new Node(g, h[rp][c], rp, c);
        children.add(child);
      }
    }

    if (cp < n && !closed[r][cp]) {
      int g = node.g + matrix[r][cp]; // current cost (actualized)
      Node child = new Node(g, h[r][cp], r, cp);
      children.add(child);
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
        h[0][0] = 0;
      }
    }
  }

  protected static int aStar() {
    int r, c = 0;
    Node node = new Node();
    for (r = 0; r < m; r++) {
      int g = matrix[r][c]; // current cost (actualized)
      node = new Node(g, h[r][c], r, c);
      open.add(node);
    }
    boolean done = false;
    while (!done && !open.isEmpty()) {
      node = open.remove();
      r = node.i;
      c = node.j;
      closed[r][c] = true;
      done = c == n - 1;
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
    String file = "/net/euler/p082.txt";
    InputStream is = P082.class.getResourceAsStream(file);
    loadMatrix(is);

    int minimalPathSum = aStar();
    System.out.println("The minimal path sum in " + m + " by " + n
        + " matrix, from the left column to the right column by only moving up, down, and right is " + minimalPathSum);
  }
}
