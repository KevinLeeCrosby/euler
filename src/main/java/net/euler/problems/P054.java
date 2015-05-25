package net.euler.problems;

import com.google.common.collect.Lists;
import net.euler.utils.PokerHand;
import net.euler.utils.PokerHand.Category;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In the card game poker, a hand consists of five cards and are ranked, from lowest to highest, in the following way:
 * <p/>
 * High Card: Highest value card.
 * One Pair: Two cards of the same value.
 * Two Pairs: Two different pairs.
 * Three of a Kind: Three cards of the same value.
 * Straight: All cards are consecutive values.
 * Flush: All cards of the same suit.
 * Full House: Three of a kind and a pair.
 * Four of a Kind: Four cards of the same value.
 * Straight Flush: All cards are consecutive values of same suit.
 * Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
 * The cards are valued in the order:
 * 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.
 * <p/>
 * If two players have the same ranked hands then the rank made up of the highest value wins; for example, a pair of eights beats a pair of fives (see example 1 below). But if two ranks tie, for example, both players have a pair of queens, then highest cards in each hand are compared (see example 4 below); if the highest cards tie then the next highest cards are compared, and so on.
 * <p/>
 * The file, poker.txt, contains one-thousand random hands dealt to two players. Each line of the file contains ten cards (separated by a single space): the first five are Player 1's cards and the last five are Player 2's cards. You can assume that all hands are valid (no invalid characters or repeated cards), each player's hand is in no specific order, and in each hand there is a clear winner.
 * <p/>
 * How many hands does Player 1 win?
 *
 * @author Kevin Crosby
 */
public class P054 {
  private static final Pattern pattern = Pattern.compile("((?:\\w{2}\\s*){4}\\w{2})\\s*((?:\\w{2}\\s*){4}\\w{2})");

  private static List<Pair<PokerHand, PokerHand>> loadGames(InputStream is) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    List<Pair<PokerHand, PokerHand>> games = Lists.newArrayList();

    String line;
    while ((line = br.readLine()) != null) {
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        Pair<PokerHand, PokerHand> game = Pair.of(new PokerHand(matcher.group(1)), new PokerHand(matcher.group(2)));
        games.add(game);
      }
    }
    br.close();

    return games;
  }

  public static void main(String[] args) throws IOException {
    String file = "/net/euler/problems/p054.txt";
    InputStream is = P054.class.getResourceAsStream(file);
    List<Pair<PokerHand, PokerHand>> games = loadGames(is);

    int player1Count = 0;
    for (Pair<PokerHand, PokerHand> game : games) {
      PokerHand hand1 = game.getLeft();
      PokerHand hand2 = game.getRight();

      Category category1 = hand1.evaluate();
      Category category2 = hand2.evaluate();

      int c = hand1.compareTo(hand2);
      char operator;
      switch (c) {
        case -1:
          operator = '<';
          break;
        case 0:
          operator = '=';
          break;
        case 1:
          operator = '>';
          player1Count++;
          break;
        default:
          operator = '?';
      }
      System.out.println(hand1 + " (" + category1 + ")" + " " + operator + " " + hand2 + " (" + category2 + ")");
    }

    System.out.println("Player 1 won " + player1Count + " games");
  }
}

