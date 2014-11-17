package net.euler;

/**
 * Created by kevin on 11/11/14.
 */
public class P002 {
  private static final long limit = 4000000;

  private static final double phi = (1 + Math.sqrt(5)) / 2; // golden ratio

  private static long fibonacci(int n) { // Binet's formula
    return Math.round((Math.pow(phi, n) - Math.pow(-phi, -n)) / Math.sqrt(5));
  }

  public static void main(String[] args) {
    int sum = 0;
    for (int i = 0; fibonacci(i) <= limit; i += 3) {
      sum += fibonacci(i);
    }

    System.out.println("The sum of the even-valued Fibonacci terms not exceeding four million is " + sum);
  }
}
