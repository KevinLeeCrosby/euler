package net.euler;

/**
 * Created by kevin on 11/12/14.
 */
public class P004 {

  private static boolean isPalindrome(long number) {
    String string = Long.toString(number);
    int length = string.length();
    for (int i = 0; i < length / 2; i++) {
      int j = length - i - 1;
      if (string.charAt(i) != string.charAt(j)) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    long palindrome = 0;
    int a = 0;
    int b = 0;
    for (int i = 999; i > 99; i--) {
      for (int j = i; j > 99; j--) {
        long product = i * j;
        if (isPalindrome(product) && product > palindrome) {
          palindrome = product;
          a = i;
          b = j;
        }
      }
    }
    System.out.println("The largest palindrome made from the product of two 3-digit numbers is " + palindrome + " = " + a + " * " + b);
  }
}
