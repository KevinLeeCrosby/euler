package net.euler.utils;

import static net.euler.utils.MathUtils.binomial;

/**
 * Utility to traverse Pascal's Triangle
 *
 * @author Kevin Crosby
 */
public class Pascal { // TODO add bounds checking
  private int n, k;
  private long c;

  public Pascal() {
    this(0, 0);
    this.c = 1;
  }

  public Pascal(final int n, final int k) {
    goTo(n, k);
  }

  public void goTo(final int n, final int k) {
    this.n = n;
    this.k = k;
    this.c = binomial(n, k);
  }

  public long getCurrent() {
    return c;
  }

  public int getN() {
    return n;
  }

  public int getK() {
    return k;
  }

  public long getRight() {
    return c * (n - k) / (k + 1);
  }

  public long goRight() {
    c = getRight();
    k++;
    return c;
  }

  public long getLeft() {
    return c * k / (n - k + 1);
  }

  public void goLeft() {
    c = getLeft();
    k--;
  }

  public long getUpperRight() {
    return c * (n - k) / n;
  }

  public void goUpperRight() {
    c = getUpperRight();
    n--;
  }

  public long getUpperLeft() {
    return c * k / n;
  }

  public void goUpperLeft() {
    c = getUpperLeft();
    n--;
    k--;
  }

  public long getLowerRight() {
    return c * (n + 1) / (k + 1);
  }

  public void goLowerRight() {
    c = getLowerRight();
    n++;
    k++;
  }

  public long getLowerLeft() {
    return c * (n + 1) / (n - k + 1);
  }

  public void goLowerLeft() {
    c = getLowerLeft();
    n++;
  }
}
