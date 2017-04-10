package net.euler.problems;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * An equilateral triangle with integer side length n≥3 is divided into n^2 equilateral triangles with side length 1 as
 * shown in the diagram below.
 * The vertices of these triangles constitute a triangular lattice with (n+1)(n+2)/2 lattice points.
 *
 * Let H(n) be the number of all regular hexagons that can be found by connecting 6 of these points.
 *
 * For example, H(3)=1, H(6)=12, and H(20)=966.
 *
 * Find ∑n=3 to 12345 H(n).
 *
 * @author Kevin Crosby
 */
public class P577slow {
  public P577slow() {}

  public static List<Long> trim(List<Long> polynomial) {
    int i = polynomial.size() - 1;
    while (i > 0 && polynomial.get(i) == 0) {
      polynomial.remove(i--);
    }
    return polynomial;
  }

  public static List<Long> subtract(final List<Long> minuend, final List<Long> subtrahend) {
    int m = minuend.size();
    int n = subtrahend.size();
    int p = Math.min(m, n);
    List<Long> difference = LongStream.generate(() -> 0)
        .limit(Math.max(m, n))
        .boxed()
        .collect(Collectors.toList());
    for (int i = 0; i < p; ++i) {
      difference.set(i, minuend.get(i) - subtrahend.get(i));
    }
    if (m > n) {
      for (int i = p; i < m; ++i) {
        difference.set(i, minuend.get(i));
      }
    } else if (m < n) {
      for (int i = p; i < n; ++i) {
        difference.set(i, -subtrahend.get(i));
      }
    }
    return trim(difference);
  }

  public static List<Long> multiply(final List<Long> multiplicand, final List<Long> multiplier) {
    int m = multiplicand.size();
    int n = multiplier.size();
    List<Long> product = LongStream.generate(() -> 0)
        .limit(m + n - 1)
        .boxed()
        .collect(Collectors.toList());

    for (int i = 0; i < m; ++i) {
      long a = multiplicand.get(i);
      for (int j = 0; j < n; ++j) {
        long b = multiplier.get(j);
        int k = i + j;
        product.set(k, product.get(k) + a * b);
      }
    }
    return product;
  }

  public static List<Long> power(List<Long> base, int exponent) {
    if (exponent < 0) { return Lists.newArrayList(); }
    if (exponent == 0) { return Lists.newArrayList(1L); }
    List<Long> product = Lists.newArrayList(1L); // Kronecker delta
    while (exponent > 0) {
      if ((exponent & 1) != 0) { // i.e. if odd
        product = multiply(base, product);
      }
      exponent >>>= 1;
      base = multiply(base, base);
    }
    return product;
  }

  public static List<Long> differentiate(final List<Long> antiderivative) {
    int m = antiderivative.size();
    return IntStream.range(1, m)
        .mapToLong(i -> i * antiderivative.get(i))
        .boxed()
        .collect(Collectors.toList());
  }

  public static Pair<List<Long>, List<Long>> quotient(final Pair<List<Long>, List<Long>> ratio) {
    List<Long> top = ratio.getLeft();
    List<Long> bottom = ratio.getRight();

    List<Long> numerator = subtract(multiply(differentiate(top), bottom), multiply(top, differentiate(bottom)));
    List<Long> denominator = power(bottom, 2);

    return Pair.of(numerator, denominator);
  }

  public Pair<List<Long>, List<Long>> a() { // See: A011779  A(x) = 1 / ((1-x)^3*(1-x^3)^2)
    List<Long> numerator = Lists.newArrayList(1L); // i.e. "1"

    List<Long> a = Lists.newArrayList(1L, -1L);
    List<Long> b = Lists.newArrayList(1L, 0L, 0L, -1L);
    List<Long> denominator = multiply(power(a, 3), power(b, 2)); // i.e. (1-x)^3*(1-x^3)^2

    return Pair.of(numerator, denominator);
  }

  // evaluate at zero
  public long evaluate(Pair<List<Long>, List<Long>> a) {
    long numerator = a.getLeft().get(0);
    long denominator = a.getRight().get(0);
    assert numerator / denominator * denominator == numerator : "something is wrong";

    return numerator / denominator;
  }

  // Maclauren Series is a Taylor Series evaluated at zero
  public List<Long> maclauren(Pair<List<Long>, List<Long>> a, final int n) {
    long factorial = 1;
    List<Long> h = Lists.newArrayList(0L, 0L, 0L, evaluate(a) / factorial);
    for (int i = 1; i < n - 2; ++i) {
      factorial *= i;
      a = quotient(a);
      h.add(evaluate(a) / factorial);
    }

    return h;
  }

  public List<Long> h(final int n) {
    return maclauren(a(), n);
  }

  // works, but highly inefficient
  public static void main(String[] args) {
    P577slow problem = new P577slow();

    System.out.println(problem.h(15));
  }
}
