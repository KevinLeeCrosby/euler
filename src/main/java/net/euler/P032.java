package net.euler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;

/**
 * We shall say that an n-digit number is pandigital if it makes use of all the digits 1 to n exactly once; for
 * example,
 * the 5-digit number, 15234, is 1 through 5 pandigital.
 *
 * The product 7254 is unusual, as the identity, 39 × 186 = 7254, containing multiplicand, multiplier, and product is 1
 * through 9 pandigital.
 *
 * Find the sum of all products whose multiplicand/multiplier/product identity can be written as a 1 through 9
 * pandigital.
 *
 * HINT: Some products can be obtained in more than one way so be sure to only include it once in your sum.
 *
 * @author Kevin Crosby
 */
public class P032 {
  private static SetMultimap<Integer, Pair<Integer, Integer>> table = HashMultimap.create();

  private static void createTable(final Character low, final Character high) {
    createTable(Integer.parseInt(low.toString()), Integer.parseInt(high.toString()));
  }

  private static void createTable(final int low, final int high) { // table of last digits
    int modulus = high - low + 2;
    for (int i = low; i < high; i++) {
      for (int j = i + 1; j <= high; j++) {
        int k = (i * j) % modulus;
        if (low <= k && k <= high && i != k && j != k) {
          //System.out.println("Adding (" + i + " * " + j+ ") mod " + modulus + " = " + k + " triple.");
          table.put(k, Pair.create(i, j));
        }
      }
    }
  }

  private static boolean areValidLengths(String x, String y, String z) {
    int a = x.length(), b = y.length(), c = z.length();
    return c <= a + b && a + b <= c + 1;   // weak triangle inequality constraint
  }

  private static List<Integer> findPandigitalProducts(String permutation) {
    List<Integer> products = Lists.newArrayList();

    int firstIndex = 0;
    int endIndex = permutation.length();

    Character lastChar = permutation.charAt(endIndex - 1);
    int lastDigit = Integer.parseInt(lastChar.toString());

    for (Pair<Integer, Integer> pair : table.get(lastDigit)) {
      int indexA = permutation.indexOf(pair.getFirst().toString());
      int indexB = permutation.indexOf(pair.getSecond().toString());
      int secondIndex = Math.min(indexA, indexB) + 1;
      int thirdIndex = Math.max(indexA, indexB) + 1;
      String firstNumber = permutation.substring(firstIndex, secondIndex);
      String secondNumber = permutation.substring(secondIndex, thirdIndex);
      String thirdNumber = permutation.substring(thirdIndex, endIndex);
      if (!areValidLengths(firstNumber, secondNumber, thirdNumber)) {
        continue;
      }
      int multiplicand = Integer.parseInt(firstNumber);
      int multiplier = Integer.parseInt(secondNumber);
      int product = Integer.parseInt(thirdNumber);
      if (multiplicand < multiplier && multiplicand * multiplier == product) {
        System.out.println("Found pandigital product " + multiplicand + " * " + multiplier + " == " + product);
        products.add(product);
      }
    }
    return products;
  }

  public static void main(String[] args) {
    final String digits = "123456789";

    Character firstDigit = digits.charAt(0);
    Character lastDigit = digits.charAt(digits.length() - 1);

    createTable(firstDigit, lastDigit);

    Set<Integer> products = Sets.newHashSet();

    int sum = 0;
    for (String permutation : P024.permute(digits)) {
      for (int product : findPandigitalProducts(permutation)) {
        if (!products.contains(product)) {
          sum += product;
          products.add(product);
        }
      }
    }
    System.out.println("The sum of all (unique) products whose multiplicand/multiplier/product identity can be written as a " +
        firstDigit + " through " + lastDigit + " pandigital is " + sum);
  }
}
