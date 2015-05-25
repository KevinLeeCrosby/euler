package net.euler.problems;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import net.euler.utils.Primes;

import java.util.List;

/**
 * The cube, 41063625 (345^3), can be permuted to produce two other cubes: 56623104 (384^3) and 66430125 (405^3). In
 * fact, 41063625 is the smallest cube which has exactly three permutations of its digits which are also cube.
 *
 * Find the smallest cube for which exactly five permutations of its digits are cube.
 *
 * @author Kevin Crosby
 */
public class P062 {
  private static final Primes primes = Primes.getInstance();

  private static ListMultimap<Integer, Long> roots = ArrayListMultimap.create();
  private static ListMultimap<Integer, Long> cubes = ArrayListMultimap.create();

  private static int hashCode(long number) {
    int hash = 1;
    while (number > 0) {
      int digit = new Long(number % 10).intValue();
      hash *= primes.get(digit);
      number /= 10;
    }
    return hash;
  }

  public static void main(String[] args) {
    final int size = args.length > 0 ? Integer.parseInt(args[0]) : 5;

    primes.generate(31);  // ensure 10 primes for hashing

    boolean loop = true;
    int hashCode = 0;
    for (long n = 300, cube = n*n*n; loop; ++n, cube += 3 * n * (n - 1) + 1) {
      hashCode = hashCode(cube);
      roots.put(hashCode, n);
      cubes.put(hashCode, cube);
      loop = cubes.get(hashCode).size() != size;
    }

    List<Long> root = roots.get(hashCode);
    List<Long> permutations = cubes.get(hashCode);
    List<String> strings = Lists.newArrayList();
    for (int i = 0; i < size; i++) {
      strings.add(root.get(i) + "^3 = " + permutations.get(i));
    }
    System.out.println("Found:  " + Joiner.on(", ").join(strings));

    System.out.println("The smallest cube for which exactly " + size + " permutations of its digits are cube is "
        + root.get(0) + "^3 = " + permutations.get(0));
  }
}
