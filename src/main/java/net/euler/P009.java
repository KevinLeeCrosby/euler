package net.euler;

/**
 * Created by kevin on 11/12/14.
 */
public class P009 {
  public static void main(String[] args) {
    int sum = 1000;

    for (int a = 1; a < sum * 0.3; a++) { // upper bound of a is sum * (2 - sqrt(2)) / 2
      for (int b = (int)Math.ceil(sum * 0.3f); b < sum * 0.42; b++) { // lower bound of b is sum * (2 - sqrt(2)) / 2 , upper bound of b is sum * (sqrt(2) - 1)
        int c = sum - a - b;
        if (a * a + b * b == c * c) {
          System.out.println("a = " + a + ", b = " + b + ", c = " + c);
          System.out.println("a + b + c = " + sum + ", a² + b² = c² = " + c * c + ", abc = " + a * b * c);
          break;
        }
      }
    }

  }
}
