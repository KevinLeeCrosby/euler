package net.euler;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A common security method used for online banking is to ask the user for three random characters from a passcode. For
 * example, if the passcode was 531278, they may ask for the 2nd, 3rd, and 5th characters; the expected reply would be:
 * 317.
 *
 * The text file, keylog.txt, contains fifty successful login attempts.
 *
 * Given that the three characters are always asked for in order, analyse the file so as to determine the shortest
 * possible secret passcode of unknown length.
 *
 * @author Kevin Crosby
 */
public class P079 {
  private static List<Integer> loadAttempts(final InputStream is) throws IOException {
    Scanner fileScan = new Scanner(is);

    List<Integer> cipher = Lists.newArrayList();
    while (fileScan.hasNext()) {
      Scanner lineScan = new Scanner(fileScan.nextLine());
      while (lineScan.hasNextInt()) {
        cipher.add(lineScan.nextInt());
      }
    }
    fileScan.close();

    return cipher;
  }

  private static SetMultimap<Integer, Integer> store(final List<Integer> attempts) {
    SetMultimap<Integer, Integer> next = HashMultimap.create();
    for (int attempt : attempts) {
      next.putAll(store(attempt));
    }
    return next;
  }

  private static SetMultimap<Integer, Integer> store(final int attempt) {
    SetMultimap<Integer, Integer> next = HashMultimap.create();
    int value = attempt % 10;
    int rest = attempt / 10;
    for (int i = 0; i < 2; i++) {
      int key = rest % 10;
      next.put(key, value);
      rest /= 10;
      value = key;
    }

    return next;
  }

  private static int analyze(final List<Integer> attempts) {
    SetMultimap<Integer, Integer> next = store(attempts);
    Map<Integer, Integer> map = Maps.newHashMap();

    int firstKey = -1;
    while (!next.isEmpty()) {
      SetMultimap<Integer, Integer> copy = HashMultimap.create(next);
      for (int key : copy.keySet()) {
        Set<Integer> values = copy.get(key);
        if (values.size() == 1) {
          firstKey = key;
          int value = Iterables.getOnlyElement(values);
          map.put(key, value);
          for (int k : copy.keySet()) {
            if (next.containsEntry(k, value)) {
              next.remove(k, value);
              if (k != key) {
                next.put(k, key);
              }
            }
          }
          break;
        }
      }
    }

    int key = firstKey;
    int passcode = key;
    while (!map.isEmpty()) {
      int value = map.get(key);
      passcode = passcode * 10 + value;
      map.remove(key);
      key = value;
    }

    return passcode;
  }

  public static void main(String[] args) throws IOException {
    final String file = "/net/euler/p079.txt";
    final InputStream is = P079.class.getResourceAsStream(file);
    final List<Integer> attempts = loadAttempts(is);

    int passcode = analyze(attempts);

    System.out.println("The shortest possible secret passcode of unknown length is " + passcode);
  }
}
