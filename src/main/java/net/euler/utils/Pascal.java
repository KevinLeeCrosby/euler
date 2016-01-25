package net.euler.utils;

import static net.euler.utils.MathUtils.binomial;

/**
 * Utility to traverse Pascal's Triangle
 *
 * @author Kevin Crosby
 */
class Pascal { // TODO add bounds checking
  private int n, k;
  private long c;

  public Pascal() {
    this(0, 0);
    this.c = 1;
  }

  public Pascal(final int n, final int k) {
    this.n = n;
    this.k = k;
    this.c = binomial(n, k);
  }

  public long getC() {
    return c;
  }

  public int getN() {
    return n;
  }

  public int getK() {
    return k;
  }

  public long getRight() {
    return c / (k + 1) * (n - k);
  }

  public long goRight() {
    c = getRight();
    k++;
    return c;
  }

  public long getLeft() {
    return c / (n - k + 1) * k;
  }

  public void goLeft() {
    c = getLeft();
    k--;
  }

  public long getUpperRight() {
    return c / n * (n - k);
  }

  public void goUpperRight() {
    c = getUpperRight();
    n--;
  }

  public long getUpperLeft() {
    return c / n * k;
  }

  public void goUpperLeft() {
    c = getUpperLeft();
    n--;
    k--;
  }

  public long getLowerRight() {
    return c / (k + 1) * (n + 1);
  }

  public void goLowerRight() {
    c = getLowerRight();
    n++;
    k++;
  }

  public long getLowerLeft() {
    return c / (n - k + 1) * (n + 1);
  }

  public void goLowerLeft() {
    c = getLowerLeft();
    n++;
  }
}
