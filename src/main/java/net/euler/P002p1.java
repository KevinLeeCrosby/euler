package net.euler;

/**
 * Created by kevin on 11/12/14.
 */
public class P002p1 {
  private static final long limit = 4000000;

  private static final double phi = (1 + Math.sqrt(5)) / 2; // golden ratio
  private static final double phi3 = Math.pow(phi, 3);

  public static void main(String[] args) {
    int sum = 0;
    long fibonacci = 2;
    while (fibonacci <= limit) {
      sum += fibonacci;
      fibonacci = Math.round(phi3 * fibonacci);
    }

    System.out.println("The sum of the even-valued Fibonacci terms not exceeding four million is " + sum);
  }
}
