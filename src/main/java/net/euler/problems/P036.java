package net.euler.problems;

/**
 * The decimal number, 585 = 10010010012 (binary), is palindromic in both bases.
 *
 * Find the sum of all numbers, less than one million, which are palindromic in base 10 and base 2.
 *
 * (Please note that the palindromic number, in either base, may not include leading zeros.)
 *
 * @author Kevin Crosby
 */
public class P036 {
  private static boolean isDigitalPalindrome(long n) { //digital palindrome
    long r = 0;
    for (long d = n; d > 0; d /= 10) {
      r = r * 10 + d % 10;
    }
    return r == n;
  }

  private static boolean isBinaryPalindrome(long n) { // binary palindrome
    long r = 0;
    for (long b = n; b > 0; b >>= 1) {
      r = (r << 1) | (b & 1);
    }
    return r == n;
  }

  private static boolean isDoubleBasePalindrome(long n) { // digital and binary palindrome
    return isDigitalPalindrome(n) && isBinaryPalindrome(n);
  }

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
    } else {
      limit = 1000000;
    }

    long sum = 0;
    for (int n = 1; n < limit; n++) {
      if (isDoubleBasePalindrome(n)) {
        sum += n;
      }
    }

    System.out.println("The sum of all numbers, less than " + limit + " which are palindromic in base 10 and base 2, is " + sum);
  }
}
