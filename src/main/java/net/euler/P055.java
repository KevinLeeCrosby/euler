package net.euler;

import com.google.common.collect.Sets;

import java.math.BigInteger;
import java.util.Set;

/**
 * If we take 47, reverse and add, 47 + 74 = 121, which is palindromic.
 * <p/>
 * Not all numbers produce palindromes so quickly. For example,
 * <p/>
 * 349 + 943 = 1292,
 * 1292 + 2921 = 4213
 * 4213 + 3124 = 7337
 * <p/>
 * That is, 349 took three iterations to arrive at a palindrome.
 * <p/>
 * Although no one has proved it yet, it is thought that some numbers, like 196, never produce a palindrome. A number that never forms a palindrome through the reverse and add process is called a Lychrel number. Due to the theoretical nature of these numbers, and for the purpose of this problem, we shall assume that a number is Lychrel until proven otherwise. In addition you are given that for every number below ten-thousand, it will either (i) become a palindrome in less than fifty iterations, or, (ii) no one, with all the computing power that exists, has managed so far to map it to a palindrome. In fact, 10677 is the first number to be shown to require over fifty iterations before producing a palindrome: 4668731596684224866951378664 (53 iterations, 28-digits).
 * <p/>
 * Surprisingly, there are palindromic numbers that are themselves Lychrel numbers; the first example is 4994.
 * <p/>
 * How many Lychrel numbers are there below ten-thousand?
 * <p/>
 * NOTE: Wording was modified slightly on 24 April 2007 to emphasise the theoretical nature of Lychrel numbers.
 *
 * @author Kevin Crosby
 */
public class P055 {
  private static BigInteger reverse(final BigInteger n) {
    return new BigInteger(new StringBuffer(n.toString()).reverse().toString());
  }

  private static BigInteger reverseAndAdd(final BigInteger n) {
    return n.add(reverse(n));
  }

  private static boolean isPalidrome(final BigInteger n) {
    return n.equals(reverse(n));
  }

  public static void main(String[] args) {
    int limit = args.length > 0 ? Integer.parseInt(args[0]) : 10000;
    int iterations = args.length > 1 ? Integer.parseInt(args[1]) : 50;

    Set<BigInteger> nonLychrelCache = Sets.newHashSet();
    Set<BigInteger> lychrelCache = Sets.newHashSet();
    Set<Integer> lychrels = Sets.newTreeSet();
    for (int number = limit - 1; number >= 100; number--) {
      BigInteger n = BigInteger.valueOf(number);
      if (lychrelCache.contains(n) || nonLychrelCache.contains(n)) continue;
      boolean isNonLychrel = false, isLychrel = false;
      Set<BigInteger> thread = Sets.newHashSet(n);
      for (int iteration = 1; iteration < iterations; iteration++) {
        n = reverseAndAdd(n);
        isNonLychrel = isPalidrome(n) || nonLychrelCache.contains(n);
        isLychrel = lychrelCache.contains(n);
        if (isNonLychrel || isLychrel) break;
        thread.add(n); // do not add palindromes, as they themselves could be Lychrels
      }
      if (isNonLychrel) {
        nonLychrelCache.addAll(thread);
      } else { // assume Lychrel
        lychrels.add(number);
        lychrelCache.addAll(thread);
      }
    }

    int count = lychrels.size();

    System.out.println("Lychrels...");
    int i = 0;
    for (int lychrel : lychrels) {
      System.out.print(" " + lychrel);
      if (++i % 25 == 0) {
        System.out.println();
      }
    }
    System.out.println("\n");

    System.out.println(
        "There are " + count + " Lychrel numbers below " + limit + " (max iterations " + iterations + ")");

  }
}
