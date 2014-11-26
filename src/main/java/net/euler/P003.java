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

    BigIntegerPrimes bip = BigIntegerPrimes.getInstance();
    List<BigInteger> factors = bip.factor(number);
    StringBuilder sb = new StringBuilder().append(number).append(" = ");
    sb.append(Joiner.on(" * ").join(factors));
    System.out.println(sb);
  }
}
