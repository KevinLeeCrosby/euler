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
 * Created by Kevin on 11/27/2014.
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
