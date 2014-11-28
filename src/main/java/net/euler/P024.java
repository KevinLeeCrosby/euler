package net.euler;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * A permutation is an ordered arrangement of objects.
 * For example, 3124 is one possible permutation of the digits 1, 2, 3 and 4.
 * If all of the permutations are listed numerically or alphabetically, we call it lexicographic order.
 * The lexicographic permutations of 0, 1 and 2 are:
 *         012   021   102   120   201   210
 * What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5, 6, 7, 8 and 9?
 */
public class P024 {
  public static List<String> permute(String string) {
    if (string.length() == 1) {
      return Lists.newArrayList(string);
    }
    List<String> permutations = Lists.newArrayList();
    for (int i = 0; i < string.length(); i++) {
      for (String end : permute(string.substring(0, i) + string.substring(i + 1))) {
        permutations.add(string.charAt(i) + end);
      }
    }
    return permutations;
  }


  public static void main(String[] args) {
    int millionth = 1000000;
    String digits = "0123456789";
    List<String> permutations = permute(digits);

//    for (String permutation : permutations) {
//      System.out.println(permutation);
//    }

    System.out.println("The millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5, 6, 7, 8 and 9 is "
        + permutations.get(millionth - 1));
  }
}
