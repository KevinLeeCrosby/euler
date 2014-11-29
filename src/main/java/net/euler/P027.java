package net.euler;

/**
 * Created by Kevin on 11/28/2014.
 */
public class P027 {
  private static long quadratic(long n, long a, long b) {
    return n * n + a * n + b;
  }

  public static void main(String[] args) {
    long limit;
    if (args.length > 0) {
      limit = Long.parseLong(args[0]);
    } else {
      limit = 1000;
    }

    Primes primes = Primes.getInstance();
    long maximum = 0;
    long aa = 0;
    long bb = 0;
    for (long b : primes) {
      if (b > limit) break;
      for (long a = (-limit + 1) | 1; a <= 0; a += 2) { // n(n-a)+b>=b ==> n >= a for all n ==> a <= 0
        for (long n = 1; primes.isPrime(quadratic(n, a, b)); n++) { // n = 0 covered by making b prime
          if (n > maximum) {
            maximum = n;
            aa = a;
            bb = b;
          }
        }
      }
    }
    System.out.println("The product of the coefficients is " + (aa * bb) + " for Quadratic expression n^2 - " +
        -aa + " n + " + bb + ", which generates primes for n = 0.." + maximum + " primes.");
  }
}
