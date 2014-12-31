package net.euler;

import com.google.common.collect.ImmutableMap;
import jlibs.core.lang.Ansi;
import jlibs.core.lang.Ansi.Color;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

import static jlibs.core.lang.Ansi.Attribute.*;
import static jlibs.core.lang.Ansi.Color.BLACK;
import static jlibs.core.lang.Ansi.Color.RED;
import static jlibs.core.lang.Ansi.Color.WHITE;

/**
 * Playing card class.
 *
 * @author Kevin Crosby
 */
public class Card implements Comparable<Card> {
  private static final String RANKS = "23456789⒑JQKA";
  private static final Map<Character, Character> RANK_MAP = ImmutableMap.of('T', '⒑');

  private static final String SUITS = "♦♣♥♠";
  private static final Map<Character, Character> SUIT_MAP = ImmutableMap.of(
      'S', '♠', 'H', '♥', 'C', '♣', 'D', '♦');

  private final Character rank;
  private final Character suit;
  private final Integer index;

  public Card(final String card) {
    this(card.charAt(0), card.charAt(1));
  }

  public Card(final Character rank, final Character suit) {
    if (RANKS.contains(rank.toString())) {
      this.rank = rank;
    } else if (RANK_MAP.containsKey(rank)) {
      this.rank = RANK_MAP.get(rank);
    } else {
      this.rank = null;
    }
    if (SUITS.contains(suit.toString())) {
      this.suit = suit;
    } else if (SUIT_MAP.containsKey(suit)) {
      this.suit = SUIT_MAP.get(suit);
    } else {
      this.suit = null;
    }
    assert this.rank != null && this.suit != null : "Card cannot have null rank or suit!";
    index = SUITS.indexOf(this.suit) * RANKS.length() + RANKS.indexOf(this.rank);
  }

  public static String getRanks() {
    return RANKS;
  }

  public static String getSuits() {
    return SUITS;
  }

  public static Character getRankFromIndex(final int index) {
    return getRankFromRankIndex(index % RANKS.length());
  }

  public static Character getSuitFromIndex(final int index) {
    return getSuitFromSuitIndex(index / RANKS.length());
  }

  public static Character getRankFromRankIndex(final int rankIndex) {
    return RANKS.charAt(rankIndex);
  }

  public static Character getSuitFromSuitIndex(final int suitIndex) {
    return SUITS.charAt(suitIndex);
  }

  public static Card getCardFromIndex(final int index) {
    return new Card(getRankFromIndex(index), getSuitFromIndex(index));
  }

  public Character getRank() {
    return rank;
  }

  public Character getSuit() {
    return suit;
  }

  public Integer getIndex() {
    return index;
  }

  public Integer getRankIndex() {
    return index % RANKS.length();
  }

  public Integer getSuitIndex() {
    return index / RANKS.length();
  }

  @Override
  public boolean equals(Object object) {
    return (object instanceof Card) && (this.getRank() == ((Card) object).getRank());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31) // two randomly chosen prime numbers
        // if deriving: .appendSuper(super.hashCode())
        .append(rank)
            //.append(suit) // TODO:  Determine if needed.
        .toHashCode();
  }

  @Override
  public int compareTo(Card card) {
    int a = RANKS.indexOf(rank);
    int b = RANKS.indexOf(card.rank);
    return a > b ? 1 : a < b ? -1 : 0;
  }

  public String toString() {
    Color color = (suit == '♥' || suit == '♦') ? color = RED : BLACK;

    Ansi ansi = new Ansi(NORMAL, color, WHITE);

    return ansi.colorize("" + rank + suit);
  }
}
