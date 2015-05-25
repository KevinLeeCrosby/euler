package net.euler.problems;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.euler.utils.Primes;

import java.util.List;

/**
 * The prime 41, can be written as the sum of six consecutive primes:
 *
 * 41 = 2 + 3 + 5 + 7 + 11 + 13
 * This is the longest sum of consecutive primes that adds to a prime below one-hundred.
 *
 * The longest sum of consecutive primes below one-thousand that adds to a prime, contains 21 terms, and is equal to
 * 953.
 *
 * Which prime, below one-million, can be written as the sum of the most consecutive primes?
 *
 * @author Kevin Crosby
 */
public class P050 {
  private static final Primes primes = Primes.getInstance();

  public static void main(String[] args) {
    long limit;
    if (args.length > 0) {
      limit = Long.parseLong(args[0]);
    } else {
      limit = 1000000;
    }

    long sum = 0;
    int first = 0;
    int end = 0;
    for (long prime : primes) {
      if (sum + prime >= limit) {
        break;
      }
      end++;
      sum += prime;
    }

    boolean loop = true;
    long s = 0;
    for (int length = end - first; loop; length--) {
      s = sum;
      loop = !primes.isPrime(s);
      while (loop && s - primes.get(first) + primes.get(end) < limit) {
        s += primes.get(end++) - primes.get(first++); // shift sequence
        loop = !primes.isPrime(s);
      }
      if (loop) {
        first = 0;
        end = first + length;
        sum -= primes.get(--end); // shorten sequence
      }
    }

    List<Long> sequence = Lists.newArrayList();
    for (int i = first; i < end; i++) {
      sequence.add(primes.get(i));
    }
    System.out.println("The prime " + s + ", below " + limit + ", can be written as " + s + " = " + Joiner.on(" + ").join(sequence));
  }
}
