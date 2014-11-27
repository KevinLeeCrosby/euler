package net.euler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kevin on 11/19/14.
 */
public class P013 {
  public static void main(String[] args) throws IOException {
    String file = "/net/euler/p013.txt";
    InputStream is = P013.class.getResourceAsStream(file);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));

    // max 1s digit = 9*100=900
    // max 10s digit = 9*100=900
    // max 100s digit = 9*100+9
    // max 1000s digit = 9*100+9*10+9
    // nth digit summed affects up to (n + 2)nd digit
    long total = 0L;
    String line;
    while ((line = br.readLine()) != null) {
      long digits = Long.parseLong(line.substring(0, 12)); // add only 10 + 2 digits
      total += digits;
    }
    br.close();

    String firstTenDigits = String.valueOf(total).substring(0, 10);
    System.out.println("The first ten digits of the sum of the given one-hundred 50-digit numbers is " + firstTenDigits);
  }
}
