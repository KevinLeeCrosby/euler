package net.euler;

import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The nth term of the sequence of triangle numbers is given by, tn = ½n(n+1); so the first ten triangle numbers are:
 * <p/>
 * 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...
 * <p/>
 * By converting each letter in a word to a number corresponding to its alphabetical position and adding these values we form a word value. For example, the word value for SKY is 19 + 11 + 25 = 55 = t10. If the word value is a triangle number then we shall call the word a triangle word.
 * <p/>
 * Using words.txt (right click and 'Save Link/Target As...'), a 16K text file containing nearly two-thousand common English words, how many are triangle words?
 *
 * @author Kevin Crosby
 */
public class P042 {
  private static final Pattern pattern = Pattern.compile("\"(\\w*)\"");

  private static Set<Integer> computeTriangleNumbers(int number) {
    Set<Integer> set = Sets.newHashSet();
    int sum = 0;
    for (int n = 1; n <= number; n++) {
      sum += n;
      set.add(sum);
    }
    return set;
  }

  private static int worth(String name) {
    int worth = 0;
    for (char character : name.toLowerCase().toCharArray()) {
      worth += character - 'a' + 1;
    }
    return worth;
  }

  private static Set<String> loadNames(InputStream is) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    Set<String> set = Sets.newTreeSet();

    String line;
    while ((line = br.readLine()) != null) {
      Matcher matcher = pattern.matcher(line);

      while (matcher.find()) {
        String name = matcher.group(1);
        set.add(name);
      }
    }
    br.close();

    return set;
  }

  public static void main(String[] args) throws IOException {
    String file = "/net/euler/p042.txt";
    InputStream is = P042.class.getResourceAsStream(file);
    Set<Integer> triangles = computeTriangleNumbers(20);

    int count = 0;
    for (String name : loadNames(is)) {
      if (triangles.contains(worth(name))) {
        count++;
      }
    }
    System.out.println("Found " + count + " triangle words.");
  }
}
