package net.euler;

/**
 * Created by kevin on 11/11/14.
 */
public class P001 {
  private static final int limit = 1000;

  private static int addAllMultiplesOf(int i) {
    int n = (limit - 1) / i; // exclude limit
    int sum = i * n * (n + 1) / 2;
    return sum;
  }

  public static void main(String[] args) {
    int sum = addAllMultiplesOf(3) + addAllMultiplesOf(5) - addAllMultiplesOf(15);

    System.out.println("The sum of all the multiples of 3 or 5 below 1000 is " + sum);
  }
}
