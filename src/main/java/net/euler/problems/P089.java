package net.euler.problems;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static net.euler.utils.MathUtils.log10;
import static net.euler.utils.MathUtils.pow;

/**
 * For a number written in Roman numerals to be considered valid there are basic rules which must be followed. Even
 * though the rules allow some numbers to be expressed in more than one way there is always a "best" way of writing a
 * particular number.
 *
 * For example, it would appear that there are at least six ways of writing the number sixteen:
 *
 * IIIIIIIIIIIIIIII
 * VIIIIIIIIIII
 * VVIIIIII
 * XIIIIII
 * VVVI
 * XVI
 *
 * However, according to the rules only XIIIIII and XVI are valid, and the last example is considered to be the most
 * efficient, as it uses the least number of numerals.
 *
 * The 11K text file, roman.txt (right click and 'Save Link/Target As...'), contains one thousand numbers written in
 * valid, but not necessarily minimal, Roman numerals; see About... Roman Numerals for the definitive rules for this
 * problem.
 *
 * Find the number of characters saved by writing each of these in their minimal form.
 *
 * Note: You can assume that all the Roman numerals in the file contain no more than four consecutive identical units.
 *
 * @author Kevin Crosby
 */
public class P089 {
  private static final BiMap<Character, Integer> NUMERALS = new ImmutableBiMap.Builder<Character, Integer>()
      //.put('ↂ', 10000).put('ↁ', 5000) // DO NOT USE FOR THIS PROBLEM!
      .put('M', 1000).put('D', 500).put('C', 100).put('L', 50).put('X', 10).put('V', 5).put('I', 1)
      .build();

  private static List<String> loadNumerals(final InputStream is) throws IOException {
    Scanner fileScan = new Scanner(is);

    List<String> numeral = Lists.newArrayList();
    while (fileScan.hasNext()) {
      Scanner lineScan = new Scanner(fileScan.nextLine());
      while (lineScan.hasNext()) {
        numeral.add(lineScan.next());
      }
    }
    fileScan.close();

    return numeral;
  }

  //Numerals must be arranged in descending order of size.
  //M, C, and X cannot be equalled or exceeded by smaller denominations.
  //D, L, and V can each only appear once.

  //Only one I, X, and C can be used as the leading numeral in part of a subtractive pair.
  //I can only be placed before V and X.
  //X can only be placed before L and C.
  //C can only be placed before D and M.

  private static String roman(final int arabic) {
    StringBuilder roman = new StringBuilder();
    int number = arabic;
    for (Map.Entry<Character, Integer> entry : NUMERALS.entrySet()) {
      char character = entry.getKey();
      int value = entry.getValue(), nextValue = (int)pow(10, log10(2 * value) - 1);
      while (number >= value) {
        roman.append(character);
        number -= value;
      }
      if (nextValue > 0 && number >= value - nextValue) {
        char prevCharacter = NUMERALS.inverse().get(nextValue);
        roman.append(prevCharacter).append(character);
        number -= value - nextValue;
      }
    }
    return roman.toString();
  }

  private static int arabic(final String roman) {
    int arabic = 0, lastValue = 0;
    for (char character : roman.toCharArray()) {
      int value = NUMERALS.get(character);
      arabic += value;
      if (value > lastValue) {
        arabic -= 2*lastValue;
      }
      lastValue = value;
    }
    return arabic;
  }

  private static String minimize(final String roman) {
    return roman(arabic(roman));
  }

  public static void main(String[] args) throws IOException {
    final String file = "/net/euler/problems/p089.txt";
    final InputStream is = P089.class.getResourceAsStream(file);
    final List<String> numerals = loadNumerals(is);

    int count = 0;
    for (final String numeral : numerals) {
      String minimal = minimize(numeral);
      System.out.println("Minimizing " + numeral + " to " + minimal);
      count += numeral.length() - minimal.length();
    }

    System.out.println("The number of characters saved by writing each of these in their minimal form is " + count);
 }
}
