package net.euler;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.TreeMultimap;

import java.util.List;
import java.util.Set;

/**
 * The arithmetic sequence, 1487, 4817, 8147, in which each of the terms increases by 3330, is unusual in two ways: (i)
 * each of the three terms are prime, and, (ii) each of the 4-digit numbers are permutations of one another.
 *
 * There are no arithmetic sequences made up of three 1-, 2-, or 3-digit primes, exhibiting this property, but there is
 * one other 4-digit increasing sequence.
 *
 * What 12-digit number do you form by concatenating the three terms in this sequence?
 *
 * @author Kevin Crosby
 */
public class P049 {
  private static Primes primes = Primes.getInstance();

  private static int hashCode(int number) {
    int hash = 1;
    while (number > 0) {
      hash *= primes.get(number % 10);
      number /= 10;
    }
    return hash;
  }

  public static void main(String[] args) {
    ListMultimap<Integer, Integer> hashMap = ArrayListMultimap.create();
    for (int number = 1001; number < 10000; number += 2) {
      if (!primes.isPrime(number)) {
        continue;
      }
      hashMap.put(hashCode(number), number);
    }

    for (int hash : hashMap.keySet()) {
      List<Integer> permutations = hashMap.get(hash);
      if (permutations.size() < 3) {
        continue;
      }
      SetMultimap<Integer, Integer> differenceMap = TreeMultimap.create();
      for (int i = 0; i < permutations.size() - 1; i++) {
        int prime1 = permutations.get(i);
        for (int j = i + 1; j < permutations.size(); j++) {
          int prime2 = permutations.get(j);
          int difference = prime2 - prime1;
          differenceMap.put(difference, prime1);
          differenceMap.put(difference, prime2);
        }
      }
      for (int difference : differenceMap.keySet()) {
        List<Integer> numbers = Lists.newArrayList(differenceMap.get(difference));
        List<Integer> sequence = Lists.newArrayList(numbers.get(0));
        for (int k = 1; k < numbers.size() ; k++) {
          int number = numbers.get(k);
          if (number == numbers.get(k - 1) + difference) {
            sequence.add(number);
          } else {
            break;
          }
        }
        if (sequence.size() > 2) {
          System.out.println("Found " + Joiner.on(", ").join(sequence) + ", in which each of the terms increases by " + difference);
        }
      }
    }
  }
}
