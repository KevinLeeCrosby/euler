package net.euler.problems;

/**
 * If a box contains twenty-one coloured discs, composed of fifteen blue discs and six red discs, and two discs were
 * taken at random, it can be seen that the probability of taking two blue discs, P(BB) = (15/21)×(14/20) = 1/2.
 *
 * The next such arrangement, for which there is exactly 50% chance of taking two blue discs at random, is a box
 * containing eighty-five blue discs and thirty-five red discs.
 *
 * By finding the first arrangement to contain over 10^12 = 1,000,000,000,000 discs in total, determine the number of
 * blue discs that the box would contain.
 *
 * @author Kevin Crosby
 */
public class P100 {
  public static void main(String[] args) {
    final long LIMIT = args.length > 0 ? Long.parseLong(args[0]) : 1000000000000L;
    long blue = 1, total = 1;

    while (total < LIMIT) {
      long b = 3 * blue + 2 * total - 2, t = 4 * blue + 3 * total - 3;
      blue = b;
      total = t;
      System.out.println("blue = " + blue + ", total = " + total + ", P(BB) = "
          + ((blue + 0.0) / (total + 0.0) * (blue - 1.0) / (total - 1.0)));
    }
    System.out.println("The first arrangement to contain over " + LIMIT +
        " discs in total, the number of blue discs that the box would contain is " + blue);
  }
}
