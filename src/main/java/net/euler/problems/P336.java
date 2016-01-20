package net.euler.problems;


import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A train is used to transport four carriages in the order: ABCD. However, sometimes when the train arrives to collect
 * the carriages they are not in the correct order.
 * To rearrange the carriages they are all shunted on to arrangements large rotating turntable. After the carriages are
 * uncoupled
 * at arrangements specific point the train moves off the turntable pulling the carriages still attached with it. The
 * remaining
 * carriages are rotated 180 degrees. All of the carriages are then rejoined and this process is repeated as often as
 * necessary in order to obtain the least number of uses of the turntable.
 * Some arrangements, such as ADCB, can be solved easily: the carriages are separated between A and D, and after DCB
 * are rotated the correct order has been achieved.
 *
 * However, Simple Simon, the train driver, is not known for his efficiency, so he always solves the problem by
 * initially getting carriage A in the correct place, then carriage B, and so on.
 *
 * Using four carriages, the worst possible arrangements for Simon, which we shall call maximix arrangements, are DACB
 * and DBAC; each requiring him five rotations (although, using the most efficient approach, they could be solved using
 * just three rotations). The process he uses for DACB is shown below.
 *
 * D|ACB
 * |DBCA
 * AC|BD
 * A|CDB
 * AB|DC
 * ABCD
 *
 * It can be verified that there are 24 maximix arrangements for six carriages, of which the tenth lexicographic
 * maximix arrangement is DFAECB.
 *
 * Find the 2011th lexicographic maximix arrangement for eleven carriages.
 *
 * @author Kevin
 */
public class P336 {
  private static String getString(final List<Character> permutation) {
    return permutation.stream()
        .map(String::valueOf)
        .collect(Collectors.joining());
  }

  private static List<String> arrangements(final int number) {
    List<Character> characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().limit(number).mapToObj(i -> (char) i).collect(Collectors.toList());
    List<List<Character>> arrangements = next(characters, 2);
    for(int digits = 3; digits <= number; ++digits) {
      arrangements = nexts(arrangements, digits);
    }
    return arrangements.stream()
        .map(P336::getString)
        .sorted()
        .collect(Collectors.toList());
  }

  private static List<List<Character>> nexts(final List<List<Character>> permutations, final int digits) {
    List<List<Character>> nexts = Lists.newArrayList();
    for(final List<Character> permutation : permutations) {
      nexts.addAll(next(permutation, digits));
    }
    return nexts;
  }

  private static List<List<Character>> next(final List<Character> permutation, final int digits) {
    int n = permutation.size();
    List<List<Character>> next = Lists.newArrayList();
    List<Character> flipped = Lists.newLinkedList(permutation);
    Collections.reverse(flipped.subList(n - digits, n));
    if(digits == 2) {
      next.add(flipped);
    }
    for(int d = 2; d < digits; ++d) {
      List<Character> inverted = Lists.newLinkedList(flipped);
      Collections.reverse(inverted.subList(n - d, n));
      next.add(inverted);
    }
    return next;
  }

  public static void main(String[] args) {
    final int number = args.length > 0 ? Integer.parseInt(args[0]) : 11;

    List<String> permutations = arrangements(number);

    if(number < 11) {
      int i = 0;
      for(final String permutation : permutations) {
        System.out.format("%d : %s\n", ++i, permutation);
      }
    }
    switch(number) {
      case 6:
        System.out.format("The 10th lexicographic maximix arrangement for %d carriages is %s.\n", number, permutations.get(10 - 1));
        break;
      case 11:
        System.out.format("The 2011th lexicographic maximix arrangement for %d carriages is %s.\n", number, permutations.get(2011 - 1));
        break;
    }
  }
}
