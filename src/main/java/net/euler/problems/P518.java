package net.euler.problems;

import com.google.common.collect.Sets;
import net.euler.utils.NewPrimes;

import java.util.Collections;
import java.util.List;

import static net.euler.utils.MathUtils.pow;
import static net.euler.utils.MathUtils.sqrt;

/**
 * Let S(n) = a+b+c over all triples (a,b,c) such that:
 *
 * a, b, and c are prime numbers.
 * a < b < c < n.
 * a+1, b+1, and c+1 form a geometric sequence.
 * For example, S(100) = 1035 with the following triples:
 *
 * (2, 5, 11), (2, 11, 47), (5, 11, 23), (5, 17, 53), (7, 11, 17), (7, 23, 71), (11, 23, 47), (17, 23, 31),
 * (17, 41, 97), (31, 47, 71), (71, 83, 97)
 *
 * Find S(10^8).
 *
 * @author Kevin Crosby
 */
public class P518 {
  private final long limit;
  private final NewPrimes primes;

  private P518(long limit) {
    this.limit = limit;
    primes = NewPrimes.getInstance(limit);
  }

  private long rootOfLargestSquareDivisor(final long n) {
    long root = 1;
    List<Long> factors = primes.factor(n);
    for(final long factor : Sets.newHashSet(factors)) {
      int exponent = Collections.frequency(factors, factor);
      if(exponent > 1) {
        root *= pow(factor, exponent / 2);
      }
    }
    return root;
  }

  // http://math.stackexchange.com/questions/1218821/find-all-prime-triples-a-b-c-such-a1-b1-c1-form-a-geometric-sequence
  private long sum() {
    long sum = 0;
    for(final long a : primes) {
      long d = rootOfLargestSquareDivisor(a + 1);
      for(long n = d + 1; n <= sqrt(d * d * (limit + 1) / (a + 1)); ++n) {
        long b = (a + 1) / d * n - 1;
        if(!primes.isPrime(b)) {
          continue;
        }
        long c = (b + 1) / d * n - 1;
        if(primes.isPrime(c)) {
          sum += a + b + c;
        }
      }
    }
    //    System.out.println();
    return sum;
  }

  public static void main(String[] args) {
    long limit = args.length > 0 ? Long.parseLong(args[0]) : 100000000;

    long sum = new P518(limit).sum();

    System.out.format("S(%d) is %d.\n", limit, sum);
  }
}
