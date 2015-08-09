package net.euler.problems;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

import static net.euler.utils.MathUtils.logBase;
import static net.euler.utils.MathUtils.pow;

/**
 * In the following equation x, y, and n are positive integers.
 *
 * 1   1   1
 * - + - = -
 * x   y   n
 *
 * It can be verified that when n = 1260 there are 113 distinct solutions and this is the least value of n for which
 * the total number of distinct solutions exceeds one hundred.
 *
 * What is the least value of n for which the number of distinct solutions exceeds four million?
 *
 * NOTE: This problem is a much more difficult version of Problem 108 and as it is well beyond the limitations of a
 * brute force approach it requires a clever implementation.
 *
 * @author Kevin Crosby
 */
public class P110 {
  private static final List<Integer> PRIMES = new ImmutableList.Builder<Integer>()
      .add(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47).build();
  private static final List<Long> PRIMORIALS = Lists.newArrayList(1L);

  private static long minimize(final List<Integer> exponents, final long result) {
    long number = 1;
    for(int i = 0; i < exponents.size(); ++i) {
      number *= pow(PRIMES.get(i), exponents.get(i));
      if(number <= 0 || number >= result) {
        return result;
      }
    }
    return number;
  }

  private static int countDivisors(final List<Integer> exponents) {
    int count = 1;
    for(final int exponent : exponents) {
      count *= (2 * exponent + 1);
    }
    return count;
  }

  private static long primorial(final int n) {
    assert n >= 0 && n <= PRIMES.size() : "Cannot find primorial " + n + "#";
    if(PRIMORIALS.size() <= n) {
      PRIMORIALS.add(PRIMES.get(n - 1) * primorial(n - 1));
    }
    return PRIMORIALS.get(n);
  }

  private static long findSmallestNumber(final int solutionLimit) {
    final int solutionBound = 2 * solutionLimit - 1;

    int maxNoPrimes = (int) logBase(solutionBound, 3) + 1, minNoPrimes = 0;
    long result = primorial(maxNoPrimes);
    int noFactors = 1;
    int exponent = 0;
    while(noFactors <= solutionBound) {
      exponent = (int) logBase(result, primorial(++minNoPrimes));
      noFactors *= (2 * exponent + 1);
    }
    int maxExponent = exponent;

    // initialize result
    final List<Integer> exponents = Lists.newArrayList();
    for(int i = 0; i < maxNoPrimes; ++i) {
      exponents.add(minNoPrimes > i ? 1 : 0);
    }
    while(exponents.get(0) <= maxExponent) {
      int i = maxNoPrimes - 1;
      while(i > 0 && (exponents.get(i).equals(exponents.get(i - 1)))) {
        exponents.set(i--, 0);
      }
      exponents.set(i, exponents.get(i) + 1);
      noFactors = countDivisors(exponents);
      if(noFactors > solutionBound) {
        result = minimize(exponents, result);
      }
    }
    return result;
  }

  public static void main(String[] args) {
    final int solutionLimit = args.length > 0 ? Integer.parseInt(args[0]) : 4000000; // answer 9350130049860600

    long number = findSmallestNumber(solutionLimit);

    System.out.printf("The least value of n for which the number of distinct solutions exceeds %d is %d.", solutionLimit, number);
  }
}
