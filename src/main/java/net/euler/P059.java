package net.euler;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Each character on a computer is assigned a unique code and the preferred standard is ASCII (American Standard Code
 * for Information Interchange). For example, uppercase A = 65, asterisk (*) = 42, and lowercase k = 107.
 * <p/>
 * A modern encryption method is to take a text file, convert the bytes to ASCII, then XOR each byte with a given value,
 * taken from a secret key. The advantage with the XOR function is that using the same encryption key on the cipher
 * text, restores the plain text; for example, 65 XOR 42 = 107, then 107 XOR 42 = 65.
 * <p/>
 * For unbreakable encryption, the key is the same length as the plain text message, and the key is made up of random
 * bytes. The user would keep the encrypted message and the encryption key in different locations, and without both
 * "halves", it is impossible to decrypt the message.
 * <p/>
 * Unfortunately, this method is impractical for most users, so the modified method is to use a password as a key. If
 * the password is shorter than the message, which is likely, the key is repeated cyclically throughout the message.
 * The balance for this method is using a sufficiently long password key for security, but short enough to be memorable.
 * <p/>
 * Your task has been made easy, as the encryption key consists of three lower case characters. Using cipher.txt (right
 * click and 'Save Link/Target As...'), a file containing the encrypted ASCII codes, and the knowledge that the plain
 * text must contain common English words, decrypt the message and find the sum of the ASCII values in the original text.
 *
 * @author Kevin Crosby
 */
public class P059 {
  private static final Map<Character, Double> frequencies = ImmutableMap.<Character, Double>builder()
      .put('a', 0.08167).put('b', 0.01492).put('c', 0.02782).put('d', 0.04253).put('e', 0.12702)
      .put('f', 0.02228).put('g', 0.02015).put('h', 0.06094).put('i', 0.06966).put('j', 0.00153)
      .put('k', 0.00772).put('l', 0.04025).put('m', 0.02406).put('n', 0.06749).put('o', 0.07507)
      .put('p', 0.01929).put('q', 0.00095).put('r', 0.05987).put('s', 0.06327).put('t', 0.09056)
      .put('u', 0.02758).put('v', 0.00978).put('w', 0.02360).put('x', 0.00150).put('y', 0.01974)
      .put('z', 0.00074).build();

  private static List<Character> loadCypher(final InputStream is) throws IOException {
    Scanner fileScan = new Scanner(is);

    List<Character> cipher = Lists.newArrayList();
    while (fileScan.hasNext()) {
      Scanner lineScan = new Scanner(fileScan.nextLine()).useDelimiter(",");
      while (lineScan.hasNextInt()) {
        cipher.add((char) lineScan.nextInt());
      }
    }
    fileScan.close();

    return cipher;
  }

  public static void main(String[] args) throws IOException {
    String file = "/net/euler/p059.txt";
    InputStream is = P059.class.getResourceAsStream(file);
    List<Character> cipher = loadCypher(is);

    List<Character> keys = Lists.newArrayList();
    for (int i = 0; i < 3; i++) { // for each character in key
      double maxDot = 0;
      char bestKey = 'a';
      for (char keyChar = 'a'; keyChar <= 'z'; keyChar++) {
        Map<Character, Double> counts = Maps.newHashMap();
        int totalCharacters = 0;
        for (int j = i; j < cipher.size(); j += 3) { // iterate every 3 through cipher
          char cipherChar = cipher.get(j);
          char plainChar = Character.toLowerCase((char) (keyChar ^ cipherChar)); // extract plain character for this key
          if (plainChar >= 'a' && plainChar <= 'z') {
            totalCharacters++;
            double count = counts.containsKey(plainChar) ? counts.get(plainChar) : 0; // save counts of plain character
            counts.put(plainChar, count + 1);
          }
        }
        double dot = 0;
        for (char character : frequencies.keySet()) { // take dot product of frequencies
          double frequency = counts.containsKey(character) ? counts.get(character) / totalCharacters : 0;
          dot += frequencies.get(character) * frequency;
        }
        if (maxDot < dot) {
          maxDot = dot;
          bestKey = keyChar;
        }
      }
      keys.add(bestKey);
    }

    int sum = 0;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < cipher.size(); i++) { // decode cipher text to plain text
      char cipherChar = cipher.get(i);
      char keyChar = keys.get(i % 3);
      char plainChar = (char) (keyChar ^ cipherChar);
      sum += (int) plainChar;
      sb.append(plainChar);
    }

    System.out.println("Key is \"" + StringUtils.join(keys, "") + "\"");
    System.out.println(sb);
    System.out.println("The sum of the ASCII values in the original text is " + sum);
  }
}