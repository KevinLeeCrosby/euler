package net.euler.problems;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.euler.utils.MathUtils.log10;

/**
 * By replacing each of the letters in the word CARE with 1, 2, 9, and 6 respectively, we form a square number: 1296 =
 * 36^2. What is remarkable is that, by using the same digital substitutions, the anagram, RACE, also forms a square
 * number: 9216 = 96^2. We shall call CARE (and RACE) a square anagram word pair and specify further that leading
 * zeroes are not permitted, neither may a different letter have the same digital value as another letter.
 *
 * Using words.txt (right click and 'Save Link/Target As...'), a 16K text file containing nearly two-thousand common
 * English words, find all the square anagram word pairs (a palindromic word is NOT considered to be an anagram of
 * itself).
 *
 * What is the largest square number formed by any member of such a pair?
 *
 * NOTE: All anagrams formed must be contained in the given text file.
 *
 * @author Kevin Crosby
 */
public class P098 {
  private static final Pattern pattern = Pattern.compile("\"(\\w*)\"");

  private static final Map<Character, Integer> CHARACTER_MAP = new ImmutableMap.Builder<Character, Integer>()
      .put('A', 2).put('B', 3).put('C', 5).put('D', 7).put('E', 11)
      .put('F', 13).put('G', 17).put('H', 19).put('I', 23).put('J', 29)
      .put('K', 31).put('L', 37).put('M', 41).put('N', 43).put('O', 47)
      .put('P', 53).put('Q', 59).put('R', 61).put('S', 67).put('T', 71)
      .put('U', 73).put('V', 79).put('W', 83).put('X', 89).put('Y', 97).put('Z', 101)
      .build();

  private static final Map<Integer, Integer> DIGIT_MAP = new ImmutableMap.Builder<Integer, Integer>()
      .put(0, 2).put(1, 3).put(2, 5).put(3, 7).put(4, 11).put(5, 13).put(6, 17).put(7, 19).put(8, 23).put(9, 29)
      .build();

  private static long wordHash(final String word) {
    long product = 1;
    for (final char character : word.toCharArray()) {
      product *= CHARACTER_MAP.get(character);
    }
    return product;
  }

  private static long squareHash(final long square) {
    long product = 1;
    long number = square;
    while (number > 0) {
      int digit = (int) (number % 10);
      number /= 10;
      product *= DIGIT_MAP.get(digit);
    }
    return product;
  }

  private static Multimap<Integer, List<String>> loadAnagrams(final InputStream is) throws IOException {
    Scanner fileScan = new Scanner(is);

    Multimap<Long, String> anagramHash = ArrayListMultimap.create();
    while (fileScan.hasNext()) {
      Scanner lineScan = new Scanner(fileScan.nextLine()).useDelimiter(",");
      while (lineScan.hasNext()) {
        String line = lineScan.next();
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          String word = matcher.group(1);
          anagramHash.put(wordHash(word), word);
        }
      }
    }
    fileScan.close();

    Set<Long> singletons = Sets.newHashSet();
    singletons.addAll(anagramHash.keySet().stream()
        .filter(hash -> anagramHash.get(hash).size() == 1).collect(Collectors.toList()));
    singletons.forEach(anagramHash::removeAll);

    Multimap<Integer, List<String>> anagrams = ArrayListMultimap.create();
    for (final long hash : anagramHash.keySet()) {
      List<String> words = (List<String>) anagramHash.get(hash);
      int length = words.get(0).length();
      anagrams.put(length, words);
    }

    return anagrams;
  }

  private static Multimap<Integer, List<Integer>> findPermutations() {
    Multimap<Long, Integer> permutationHash = TreeMultimap.create();
    for (int odd = 1, square = 1; square < 1e9; odd += 2, square += odd) {
      permutationHash.put(squareHash(square), square);
    }

    Set<Long> singletons = Sets.newHashSet();
    singletons.addAll(permutationHash.keySet().stream()
        .filter(hash -> permutationHash.get(hash).size() == 1).collect(Collectors.toList()));
    singletons.forEach(permutationHash::removeAll);

    Multimap<Integer, List<Integer>> permutations = ArrayListMultimap.create();
    for (final long hash : permutationHash.keySet()) {
      List<Integer> squares = Lists.newArrayList(permutationHash.get(hash));
      int length = (int) log10(squares.get(0)) + 1;
      permutations.put(length, squares);
    }
    return permutations;
  }

  private static Collection<Integer> findMatches(final List<String> anagrams, final List<Integer> permutations) {
    Multimap<String, Integer> matches = ArrayListMultimap.create();
    for (final String anagram : anagrams) {
      for (final Integer permutation : permutations) {
        boolean isValid = true;
        Iterator<Character> letters = Lists.charactersOf(anagram).listIterator();
        Iterator<Character> digits = Lists.charactersOf(permutation.toString()).listIterator();
        Map<Character, Character> map = Maps.newTreeMap();
        while (letters.hasNext() && digits.hasNext()) {
          char letter = letters.next(), digit = digits.next();
          if (map.containsKey(letter)) {
            if (map.get(letter) == digit) {
              continue;
            } else {
              isValid = false;
              break;
            }
          }
          if (map.containsValue(digit)) {
            isValid = false;
            break;
          }
          map.put(letter, digit);
        }
        if (isValid) {
          String hash = Joiner.on("").join(Iterables.transform(map.values(), Object::toString));
          matches.put(hash, permutation);
        }
      }
    }
    Set<String> singletons = Sets.newHashSet();
    singletons.addAll(matches.keySet().stream().filter(hash -> matches.get(hash).size() == 1).collect(Collectors.toList()));
    singletons.forEach(matches::removeAll);

    return matches.values();
  }

  private static int findBestMatch(final Multimap<Integer, List<String>> anagrams, final Multimap<Integer, List<Integer>> permutations) {
    NavigableSet<Integer> matches = Sets.<Integer>newTreeSet().descendingSet();
    for (int size = 2; size < 10; ++size) {
      if (!anagrams.containsKey(size) || !permutations.containsKey(size)) continue;
      for (final List<String> wordSets : anagrams.get(size)) {
        for (final List<Integer> squareSets : permutations.get(size)) {
          matches.addAll(findMatches(wordSets, squareSets));
        }
      }
    }
    return matches.first();
  }

  public static void main(String[] args) throws IOException {
    final String file = "/net/euler/problems/p098.txt";
    final InputStream is = P098.class.getResourceAsStream(file);

    final Multimap<Integer, List<String>> anagrams = loadAnagrams(is);
    final Multimap<Integer, List<Integer>> permutations = findPermutations();
    final int largestSquare = findBestMatch(anagrams, permutations);

    System.out.println("The largest square number formed by any member of such a pair " + largestSquare);
  }
}
