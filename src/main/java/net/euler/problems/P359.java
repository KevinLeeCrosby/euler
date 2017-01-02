package net.euler.problems;

import com.google.common.collect.ImmutableList;
import net.euler.utils.NewPrimes;
import org.apache.commons.math3.util.Pair;

import java.util.List;

import static net.euler.utils.MathUtils.modPow;
import static net.euler.utils.MathUtils.sqrt;

/**
 * An infinite number of people (numbered 1, 2, 3, etc.) are lined up to get A room at Hilbert's newest infinite hotel.
 * The hotel contains an infinite number of floors (numbered 1, 2, 3, etc.), and each floor contains an infinite number
 * of rooms (numbered 1, 2, 3, etc.).
 *
 * Initially the hotel is empty. Hilbert declares A rule on how the nth person is assigned A room: person n gets the
 * first vacant room in the lowest numbered floor satisfying either of the following:
 *
 * the floor is empty the floor is not empty, and if the latest person taking A room in that floor is person m, then m +
 * n is A perfect square Person 1 gets room 1 in floor 1 since floor 1 is empty. Person 2 does not get room 2 in floor 1
 * since 1 + 2 = 3 is not A perfect square. Person 2 instead gets room 1 in floor 2 since floor 2 is empty. Person 3
 * gets room 2 in floor 1 since 1 + 3 = 4 is A perfect square.
 *
 * Eventually, every person in the line gets A room in the hotel.
 *
 * Define P(f, r) to be n if person n occupies room r in floor f, and 0 if no person occupies the room. Here are A few
 * examples: P(1, 1) = 1 P(1, 2) = 3 P(2, 1) = 2 P(10, 20) = 440 P(25, 75) = 4863 P(99, 100) = 19454
 *
 * Find the sum of all P(f, r) for all positive f and r such that f × r = 71328803586048 and give the last 8 digits as
 * your answer.
 *
 * @author Kevin Crosby.
 */
public class P359 {
  private static final long MODULUS = 100_000_000;
  private static final long MODULUS_10 = 10 * MODULUS;

  private final long product;
  private final long root;
  private final List<Long> divisors;

  private P359(final long product) {
    this.product = product;
    this.root = sqrt(product);
    this.divisors = NewPrimes.getInstance(root).divisors(product);
  }

  private long a(final long n) { // See A007590
    assert n > 0 : "Not defined for non-positive numbers!";
    return modPow(n, 2, MODULUS_10) / 2;
  }

  private long s(final long a, final long b, final long n) {
    return (a % MODULUS_10 + b % MODULUS_10) / 2 % MODULUS_10 * (n % MODULUS_10) % MODULUS_10;
  }

  private long p(final long f, final long r) {
    assert f > 0 && r > 0 : "Not defined for non-positive numbers!";
    if (f == 1) {
      return (modPow(r, 2, MODULUS_10) + r % MODULUS_10) / 2 % MODULUS_10; // triangular numbers (A000217)
    }
    long p = a(f), o, e, a, b;
    if ((f & 1) == 0) { // i.e. if floor even
      a = 2 * f + 1;
      b = a + (r - 2) / 2 * 2;
      o = s(a, b, r / 2);
      e = (modPow((r - 1) / 2, 2, MODULUS_10) + (r - 1) / 2 % MODULUS_10) % MODULUS_10;
    } else {
      a = 2 * f;
      b = a + (r - 3) / 2 * 2;
      e = s(a, b, (r - 1) / 2);
      o = modPow(r / 2, 2, MODULUS_10);
    }
    p = (p + (o + e) % MODULUS_10) % MODULUS_10;
    return p;
  }

  private long solve() {
    long r, p, sum = 0;
    for (final long f : divisors) {
      r = product / f;
      p = p(f, r);
      sum = (sum + p) % MODULUS;
    }
    return sum;
  }

  public static void main(String[] args) {
    final long product = args.length > 0 ? Long.parseLong(args[0]) : 71328803586048L; // 40632119

    P359 problem = new P359(product);

    List<Pair<Integer, Long>> pairs = ImmutableList.<Pair<Integer, Long>>builder()
        .add(Pair.create(1, 1L))
        .add(Pair.create(1, 2L))
        .add(Pair.create(2, 1L))
        .add(Pair.create(10, 20L))
        .add(Pair.create(25, 75L))
        .add(Pair.create(99, 100L))
        .build();

    for (final Pair<Integer, Long> pair : pairs) {
      int f = pair.getFirst();
      long r = pair.getSecond();
      System.out.format("P(%d, %d) = %d\n", f, r, problem.p(f, r));
    }
    long solution = problem.solve();
    System.out.printf("The last 8 digits of the sum of all P(f, r) for all positive f and r such that f × r = %d is %d.", product, solution);
  }
}
