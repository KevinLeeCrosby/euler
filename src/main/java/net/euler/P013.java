package net.euler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
 * Created by kevin on 11/19/14.
 */
public class P013 {
  public static void main(String[] args) throws IOException {
    String file = "/net/euler/problem13.txt";
    InputStream is = P013.class.getResourceAsStream(file);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));

    BigInteger total = BigInteger.ZERO;
    String line;
    while ((line = br.readLine()) != null) {
      total = total.add(new BigInteger(line));
    }
    br.close();

    String firstTenDigits = total.toString().substring(0, 10);
    System.out.println("The first ten digits of the sum of the given one-hundred 50-digit numbers is " + firstTenDigits);
  }
}
