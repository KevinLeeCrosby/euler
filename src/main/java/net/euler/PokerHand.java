package net.euler;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

import static net.euler.PokerHand.Category.*;

/**
 * Poker Hand class.
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
 *
 * @author Kevin Crosby
 */
public class PokerHand extends Hand implements Comparable<PokerHand> {
  public static enum Category {
    HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH,
    FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH //, FIVE_OF_A_KIND
  }

  public PokerHand(String string) {
    super(string);
  }

  public PokerHand(String[] strings) {
    super(strings);
  }

  public PokerHand(Card... cards) {
    super(cards);
  }

  public PokerHand(List<Card> cards) {
    super(cards);
  }

  private static void increment(Map<Integer, Integer> histogram, final Integer key) {
    int count = histogram.containsKey(key) ? histogram.get(key) : 0;
    histogram.put(key, ++count);
  }

  public Category evaluate() { // TODO:  recursively extend for more than 5 cards
    assert cards.size() == 5 : "Poker hand must have exactly 5 cards to evaluate (for now at least)!";
    Map<Integer, Integer> rankHistogram = Maps.newHashMap();
    Map<Integer, Integer> suitHistogram = Maps.newHashMap();
    for (Card card : cards) {
      increment(rankHistogram, card.getRankIndex());
      increment(suitHistogram, card.getSuitIndex());
    }
    List<Integer> rankIndices = Lists.newArrayList(rankHistogram.keySet());
    Collection<Integer> rankCounts = rankHistogram.values();
    switch (rankIndices.size()) {
      case 2: // 4,1 or 3,2
        return rankCounts.contains(4) ? FOUR_OF_A_KIND : FULL_HOUSE;
      case 3: // 3,1,1 or 2,2,1
        return rankCounts.contains(3) ? THREE_OF_A_KIND : TWO_PAIR;
      case 4: // 2,1,1,1
        return ONE_PAIR;
    }

    Collections.sort(rankIndices);
    Set<Integer> suitIndices = suitHistogram.keySet();
    int topRankIndex = rankIndices.get(4);
    int lowRankIndex = rankIndices.get(0);
    char topRank = Card.getRankFromRankIndex(topRankIndex);
    char secRank = Card.getRankFromRankIndex(rankIndices.get(3));
    char lowRank = Card.getRankFromRankIndex(lowRankIndex);
    char ace = RANKS.charAt(RANKS.length() - 1);
    char ten = RANKS.charAt(RANKS.length() - 5);
    char five = RANKS.charAt(RANKS.length() - 10);

    boolean isStraight = topRankIndex - lowRankIndex == 4 || topRank == ace && secRank == five; // "wheel" 5 4 3 2 A
    boolean isFlush = suitIndices.size() == 1;
    boolean isStraightFlush = isStraight && isFlush;
    boolean isRoyalFlush = isStraightFlush && topRank == ace && lowRank == ten; // "broadway" A Q K J 10

    if (isRoyalFlush) return ROYAL_FLUSH;
    if (isStraightFlush) return STRAIGHT_FLUSH;
    if (isFlush) return FLUSH;
    if (isStraight) return STRAIGHT;
    return HIGH_CARD;
  }

  protected static Function<Card, Integer> cardToRankIndex = new Function<Card, Integer>() {
    @Override
    public Integer apply(Card card) {
      return card.getRankIndex();
    }
  };

  private static List<Integer> getMostFrequentElements(final List<Integer> elements) {
    List<Integer> mostFrequentElements = Lists.newArrayList();
    int maxCount = 0;
    for (Integer element : Sets.newHashSet(elements)) {
      int frequency = Collections.frequency(elements, element);
      if (maxCount < frequency) {
        maxCount = frequency;
        mostFrequentElements.clear();
        mostFrequentElements.add(element);
      } else if (maxCount == frequency) {
        mostFrequentElements.add(element);
      }
    }
    Collections.sort(mostFrequentElements, Collections.reverseOrder());

    return mostFrequentElements;
  }

  @Override
  public int compareTo(PokerHand pokerHand) { // TODO:  recursively extend for more than 5 cards
    assert cards.size() == 5 : "Poker hand must have exactly 5 cards to evaluate (for now at least)!";
    Category category = evaluate();
    Category category2 = pokerHand.evaluate();
    if (!category.equals(category2)) { // if different categories
      int c = category.compareTo(category2);
      return c > 0 ? 1 : -1;
    }

    List<Card> hand1 = Lists.newArrayList(cards);
    List<Card> hand2 = Lists.newArrayList(pokerHand.cards);
    Collections.sort(hand1, Collections.reverseOrder());
    Collections.sort(hand2, Collections.reverseOrder());
    switch (category) {
      case ROYAL_FLUSH:
        return 0; // tie
      case STRAIGHT_FLUSH:
      case STRAIGHT:
        return hand1.get(1).compareTo(hand2.get(1)); // compare second highest to allow for "wheel"
      case FLUSH:
      case HIGH_CARD:
        for (int i = 0; i < 5; i++) {
          Card card1 = hand1.get(i);
          Card card2 = hand2.get(i);
          if (!card1.equals(card2)) return card1.compareTo(card2);
        }
        return 0; // tie
    }

    // histogram kinds
    List<Integer> rankIndices1 = Lists.transform(hand1, cardToRankIndex); // already sorted from above
    List<Integer> rankIndices2 = Lists.transform(hand2, cardToRankIndex);
    List<Integer> mostFrequentRank1 = getMostFrequentElements(rankIndices1);
    List<Integer> mostFrequentRank2 = getMostFrequentElements(rankIndices2);
    switch (category) {
      case FOUR_OF_A_KIND:
      case FULL_HOUSE:
      case THREE_OF_A_KIND: {
        int a = Iterables.getOnlyElement(mostFrequentRank1);
        int b = Iterables.getOnlyElement(mostFrequentRank2);
        return a > b ? 1 : a < b ? -1 : 0;
      }
      case TWO_PAIR: {
        for (int i = 0; i < 2; i++) {
          int a = mostFrequentRank1.get(i);
          int b = mostFrequentRank2.get(i);
          if (a != b) return a > b ? 1 : -1;
        }
        Set<Integer> lastRanks1 = Sets.newHashSet(rankIndices1);
        Set<Integer> lastRanks2 = Sets.newHashSet(rankIndices2);
        lastRanks1.removeAll(mostFrequentRank1);
        lastRanks2.removeAll(mostFrequentRank2);
        int a = Iterables.getOnlyElement(lastRanks1);
        int b = Iterables.getOnlyElement(lastRanks2);
        return a > b ? 1 : a < b ? -1 : 0;
      }
      case ONE_PAIR: {
        int a = Iterables.getOnlyElement(mostFrequentRank1);
        int b = Iterables.getOnlyElement(mostFrequentRank2);
        if (a != b) return a > b ? 1 : -1;
        rankIndices1.removeAll(mostFrequentRank1);
        rankIndices2.removeAll(mostFrequentRank2);
        for (int i = 0; i < 3; i++) {
          a = rankIndices1.get(i);
          b = rankIndices2.get(i);
          if (a != b) return a > b ? 1 : -1;
        }
        return 0;
      }
    }
    return 0;
  }

//  @Override
//  public String toString() { // TODO:  find slick way to group cards based on category
//    return super.toString();
//  }
}
