package net.euler.problems;

import com.google.common.math.BigIntegerMath;

import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Created by kevin on 11/27/14.
 */
public class P025 {
  public static void main(String[] args) {
    int digits;
    if (args.length > 0) {
      digits = Integer.parseInt(args[0]);
    } else {
      digits = 1000;
    }

    BigInteger fa = BigInteger.ZERO;
    BigInteger fb = BigInteger.ONE;
    BigInteger fc = fa.add(fb);

    // Since Fₙ is asymptotic to ϕⁿ / √5, the number of digits in Fₙ is asymptotic to n log₁₀ϕ ≈ 0.2090 n.
    // As a consequence, for every integer d > 1 there are either 4 or 5 Fibonacci numbers with d decimal digits.
    // http://en.wikipedia.org/wiki/Fibonacci_number#Magnitude

    //  999 digits => n ≈ 4780
    // 1000 digits => n ≈ 4785

    int c = 2;
    System.out.println("1 digits: F(0) = " + fa);
    System.out.println("1 digits: F(1) = " + fb);
    System.out.println("1 digits: F(" + c + ") = " + fc);

    int numDigits = BigIntegerMath.log10(fc, RoundingMode.FLOOR) + 1;
    while (numDigits < digits) {
      c++;
      fa = fb;
      fb = fc;
      fc = fa.add(fb);
      numDigits = BigIntegerMath.log10(fc, RoundingMode.FLOOR) + 1;
      System.out.println(numDigits + " digits: F(" + c + ") = " + fc);
    }

    System.out.println("The first term in the Fibonacci sequence to contain " + digits + " digits is F(" + c + ") = " + fc);
  }
}
