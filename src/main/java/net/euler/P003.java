package net.euler;

import com.google.common.base.Joiner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by kevin on 11/11/14.
 */
public class P003 {
  public static void main(String[] args) {
    BigInteger number;
    if (args.length > 0) {
      number = new BigInteger(args[0]);
    } else {
      number = BigInteger.valueOf(600851475143L);
    }

    BigPrimeIterator p = new BigPrimeIterator();
    List<BigInteger> factors = new ArrayList<>();

    BigInteger prime;
    StringBuilder sb = new StringBuilder().append(number).append(" = ");
    do {
      prime = p.next();
      while (number.mod(prime).equals(BigInteger.ZERO)) {
        number = number.divide(prime);
        factors.add(prime);
      }
    } while (prime.compareTo(number.divide(prime)) != 1); // i.e. if prime <= sqrt(number), p <= n/p
    if (!number.equals(BigInteger.ONE)) {
      factors.add(number);
    }
    sb.append(Joiner.on(" * ").join(factors));
    System.out.println(sb);
  }
}
