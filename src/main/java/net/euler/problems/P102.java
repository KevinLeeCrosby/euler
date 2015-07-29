package net.euler.problems;

import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Three distinct points are plotted at random on a Cartesian plane, for which -1000 ≤ x, y ≤ 1000, such that a
 * triangle is formed.
 *
 * Consider the following two triangles:
 *
 * A(-340,495), B(-153,-910), C(835,-947)
 *
 * X(-175,41), Y(-421,-714), Z(574,-645)
 *
 * It can be verified that triangle ABC contains the origin, whereas triangle XYZ does not.
 *
 * Using triangles.txt (right click and 'Save Link/Target As...'), a 27K text file containing the co-ordinates of one
 * thousand "random" triangles, find the number of triangles for which the interior contains the origin.
 *
 * NOTE: The first two examples in the file represent the triangles in the example given above.
 *
 * @author Kevin Crosby
 */
public class P102 {
  private static class Point {
    private final int x, y;

    public Point(final int x, final int y) {
      this.x = x;
      this.y = y;
    }

    public Point subtract(final Point subtrahend) {
      return new Point(x - subtrahend.x, y - subtrahend.y);
    }
  }

  private static class Triangle {
    private final Point A, B, C;

    public Triangle(final Point a, final Point b, final Point c) {
      A = a;
      B = b;
      C = c;
    }

    private static int det(final Point u, final Point v) {
      return u.x * v.y - u.y * v.x;
    }

    public boolean containsOrigin() {
      Point v0 = A, v1 = B.subtract(A), v2 = C.subtract(A);
      double d = det(v1, v2);
      double a = -det(v0, v2) / d, b = det(v0, v1) / d;
      return a > 0 && b > 0 && a + b < 1;
    }
  }

  private static List<Triangle> loadTriangles(final InputStream is) {
    Scanner fileScan = new Scanner(is);

    List<Triangle> triangles = Lists.newArrayList();
    while(fileScan.hasNext()) {
      List<Point> points = Lists.newArrayList();
      Scanner lineScan = new Scanner(fileScan.nextLine()).useDelimiter(",");
      while(lineScan.hasNextInt()) {
        int abscissa = lineScan.nextInt(), ordinate = lineScan.nextInt();
        points.add(new Point(abscissa, ordinate));
      }
      triangles.add(new Triangle(points.get(0), points.get(1), points.get(2)));
    }
    fileScan.close();

    return triangles;
  }

  public static void main(String[] args) {
    String file = "/net/euler/problems/p102.txt";
    InputStream is = P102.class.getResourceAsStream(file);

    int count = 0;
    for(final Triangle triangle : loadTriangles(is)) {
      if(triangle.containsOrigin()) {
        count++;
      }
    }
    System.out.println("The number of triangles for which the interior contains the origin is " + count);
  }
}
