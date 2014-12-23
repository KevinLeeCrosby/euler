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
 * Using names.txt (right click and 'Save Link/Target As...'), a 46K text file containing over five-thousand first names, begin by sorting it into alphabetical order. Then working out the alphabetical value for each name, multiply this value by its alphabetical position in the list to obtain a name score.
 * <p/>
 * For example, when the list is sorted into alphabetical order, COLIN, which is worth 3 + 15 + 12 + 9 + 14 = 53, is the 938th name in the list. So, COLIN would obtain a score of 938 × 53 = 49714.
 * <p/>
 * What is the total of all the name scores in the file?
 *
 * @author Kevin Crosby
 */
public class P022 {
  private static final Pattern pattern = Pattern.compile("\"(\\w*)\"");

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
    String file = "/net/euler/p022.txt";
    InputStream is = P022.class.getResourceAsStream(file);
    Set<String> set = loadNames(is);

    int total = 0;
    int index = 1;
    for (String name : set) {
      int worth = worth(name);
      int score = worth * index++;
      total += score;
    }

    System.out.println("The total of all the name scores in the file is " + total);
  }

}
