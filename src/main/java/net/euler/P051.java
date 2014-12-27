package net.euler;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static net.euler.MathUtils.log10;
import static net.euler.MathUtils.pow;
import static org.apache.commons.math3.util.CombinatoricsUtils.binomialCoefficient;

/**
 * By replacing the 1st digit of the 2-digit number *3, it turns out that six of the nine possible values: 13, 23, 43, 53, 73, and 83, are all prime.
 * <p/>
 * By replacing the 3rd and 4th digits of 56**3 with the same digit, this 5-digit number is the first example having seven primes among the ten generated numbers, yielding the family: 56003, 56113, 56333, 56443, 56663, 56773, and 56993. Consequently 56003, being the first member of this family, is the smallest prime with this property.
 * <p/>
 * Find the smallest prime which, by replacing part of the number (not necessarily adjacent digits) with the same digit, is part of an eight prime value family.
 *
 * @author Kevin Crosby
 */
public class P051 {
  private static Primes primes = Primes.getInstance();

  private static Multimap<Integer, Integer> table = ArrayListMultimap.create();

  private static int hashCode(int mod3, int noReplacements) {
    return (noReplacements - 1) * 3 + mod3;
  }

  private static void constructTable(int size) {
    for (int noReplacements = 1; noReplacements < 4; noReplacements++) {
      for (int sumOtherDigitsMod3 = 0; sumOtherDigitsMod3 < 3; sumOtherDigitsMod3++) {
        List<Integer> possibilities = Lists.newArrayList();
        for (int replacement = 0; replacement < 10; replacement++) {
          if ((replacement * noReplacements + sumOtherDigitsMod3) % 3 != 0) {
            possibilities.add(replacement);
          }
        }
        if (possibilities.size() >= size) {
          table.putAll(hashCode(sumOtherDigitsMod3, noReplacements), possibilities);
        }
      }
    }
  }

  /**
   * Returns the next higher integer with the same number of one bits.
   *
   * @param value The value on which to perform the calculation.
   * @return The next higher integer with the same number of one bits.
   */
  private static Integer increment(int value) {
    int lowestBit = value & -value;                  // find the lowest one bit in the input
    int leftBits = value + lowestBit;                // determine the leftmost bits of the output
    int changedBits = value ^ leftBits;              // determine the difference between the input and leftmost output bits
    int rightBits = (changedBits / lowestBit) >>> 2; // determine the rightmost bits of the output
    return (leftBits | rightBits);                   // return the complete output
  }

  /**
   * Converts bit in integer into list of indices.
   *
   * @param value Integer with bits.
   * @return List of indices.
   */
  private static List<Integer> getIndices(Integer value) {
    List<Integer> indices = Lists.newArrayList();
    while (value > 0) {
      int index = Integer.numberOfTrailingZeros(value);
      indices.add(index);
      value -= pow(2, index).intValue();
    }
    return indices;
  }

  /**
   * Gets list of indices of digits to be replaced in a number.
   *
   * @param noDigits       Number of digits in number.
   * @param noReplacements Number of replacements in a number.
   * @return List of list of indices of digits to replace.
   */
  private static List<List<Integer>> getListOfIndices(int noDigits, int noReplacements) {
    List<List<Integer>> listsOfIndices = Lists.newArrayList();
    Integer permutation = pow(2, noReplacements).intValue() - 1; // initialize using inverted logic
    for (int i = 0; i < binomialCoefficient(noDigits - 1, noReplacements); i++) {
      List<Integer> indices = getIndices(permutation << 1); // do not replace last digit
      listsOfIndices.add(indices);
      permutation = increment(permutation);
    }

    return listsOfIndices;
  }

  public static void main(String[] args) {
    int size;
    if (args.length > 0) {
      size = Integer.parseInt(args[0]);
    } else {
      size = 8;
    }

    // construct replacement table for feasibility
    constructTable(size);
    Set<Integer> possibleNoReplacements = Sets.newTreeSet();
    for (int key : table.keySet()) {
      possibleNoReplacements.add(key / 3 + 1);
    }

    boolean loop = true;
    int noDigits = 1;
    List<Long> primeSet = Lists.newArrayList();
    for (Long prime : primes) {
      int newDigits = log10(prime).intValue() + 1;
      if (newDigits > noDigits) {
        for (int noReplacements : possibleNoReplacements) {
          if (noDigits - 1 < noReplacements) continue;
          //for (int noReplacements = 1; loop && noReplacements < noDigits; noReplacements++) {
          for (List<Integer> indices : getListOfIndices(noDigits, noReplacements)) {
            Multimap<String, Long> sequences = ArrayListMultimap.create();
            for (Long p : primeSet) {
              String string = p.toString();
              Set<Character> digits = Sets.newTreeSet();
              StringBuilder hashCode = new StringBuilder();
              for (Integer index = 0; index < noDigits; index++) {
                char character = string.charAt(noDigits - index - 1);
                if (indices.contains(index)) {
                  digits.add(character);
                } else {
                  hashCode.append(character);
                }
              }
              if (digits.size() > 1) {
                continue;
              }
              sequences.put(hashCode.toString(), p);
            }
            for (String hashCode : sequences.keySet()) {
              Collection<Long> sequence = sequences.get(hashCode);
              if (sequence.size() >= size) {
                System.out.println("Sequence of size " + size + ": {" + Joiner.on(", ").join(sequence) + "}");
                loop = false;
                break;
              }
            }
            if (!loop) break;
          }
          if (!loop) break;
        }
        noDigits = newDigits;
        primeSet.clear();
      }
      if (!loop) break;
      primeSet.add(prime);
    }
  }
}
