package net.euler.problems;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by kevin on 11/26/14.
 */
public class P017 {
  private static Map<Integer, String> words = Maps.newHashMap();

  static {
    words.put(1, "one");
    words.put(2, "two");
    words.put(3, "three");
    words.put(4, "four");
    words.put(5, "five");
    words.put(6, "six");
    words.put(7, "seven");
    words.put(8, "eight");
    words.put(9, "nine");
    words.put(10, "ten");
    words.put(11, "eleven");
    words.put(12, "twelve");
    words.put(13, "thirteen");
    words.put(14, "fourteen");
    words.put(15, "fifteen");
    words.put(16, "sixteen");
    words.put(17, "seventeen");
    words.put(18, "eighteen");
    words.put(19, "nineteen");
    words.put(20, "twenty");
    words.put(30, "thirty");
    words.put(40, "forty");
    words.put(50, "fifty");
    words.put(60, "sixty");
    words.put(70, "seventy");
    words.put(80, "eighty");
    words.put(90, "ninety");
    words.put(100, "hundred");
    words.put(1000, "thousand");
  }

  private static String getWords(int i) {
    String string = "";
    if (i <= 20) {
      string = words.get(i);
    } else if (i < 100) {
      string = words.get(i / 10 * 10) + (i % 10 == 0 ? "" : getWords(i % 10));
    } else if (i < 1000) {
      string = getWords(i / 100) + words.get(100) + (i % 100 == 0 ? "" : "and" + getWords(i % 100));
    } else if (i < 1000000) {
      string = getWords(i / 1000) + words.get(1000) + (i % 1000 == 0 ? "" : (i / 100 % 10 != 0 ? "" : "and") + getWords(i % 1000));
    }
    return string;
  }

  public static void main(String[] args) {
    int limit;
    if (args.length > 0) {
      limit = Integer.parseInt(args[0]);
      if (limit < 1 || limit > 999999) {
        System.err.println("Can only process positive integers between 1 and 999999!");
      }
    } else {
      limit = 1000;
    }

    int total = 0;
    String string;
    for (int i = 1; i <= limit; i++) {
      string = getWords(i);
      //System.out.println(i + ": " + string);
      total += string.length();
    }

    System.out.println("The number of letters used of all the numbers from 1 to " + limit + " inclusive, written out in words, is " + total);
  }
}
