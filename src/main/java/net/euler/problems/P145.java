package net.euler.problems;

import static net.euler.utils.MathUtils.log10;
import static net.euler.utils.MathUtils.pow;

/**
 * Some positive longegers n have the property that the sum [ n + reverse(n) ] consists entirely of odd (decimal)
 * digits. For instance, 36 + 63 = 99 and 409 + 904 = 1313. We will call such numbers reversible; so 36, 63, 409, and
 * 904 are reversible. Leading zeroes are not allowed in either n or reverse(n).
 *
 * There are 120 reversible numbers below one-thousand.
 *
 * How many reversible numbers are there below one-billion (10^9)?
 *
 * @author Kevin Crosby
 */
public class P145 {
  public static void main(String[] args) {
    final long limit = args.length > 0 ? Long.parseLong(args[0]) : 1000000000L;
    final long maxDigits = log10(limit) + 1;

    long count = 0;
    for(long d = 2; d < maxDigits; ++d) {
      switch((int) (d % 4)) {
        case 1:
          // add nothing
          break;
        case 0:
        case 2:
          count += 20 * pow(30, (d >> 1) - 1);
          break;
        case 3:
          count += 100 * pow(500, d >> 2);
          break;
      }
    }
    System.out.format("There are %d reversible numbers below %d.\n", count, limit);
  }
}
