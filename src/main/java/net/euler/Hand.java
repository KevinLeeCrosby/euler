package net.euler;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Abstract card hand class.  Assumes that hands can have any number of cards.
 *
 * @author Kevin Crosby
 */
public abstract class Hand implements Iterable<Card> {
  protected List<Card> cards;
  protected static final String RANKS = Card.getRanks();
  protected static final String SUITS = Card.getSuits();

  protected static Function<String, Card> stringToCard = new Function<String, Card>() {
    @Override
    public Card apply(String string) {
      return new Card(string);
    }
  };

  public Hand(String string) {
    this(string.split("\\s"));
  }

  public Hand(String[] strings) {
    this(Lists.transform(Arrays.asList(strings), stringToCard));
  }

  public Hand(Card... cards) {
    this(Lists.newArrayList(cards));
  }

  public Hand(List<Card> cards) {
    this.cards = cards;
  }

  public Hand(Hand hand) {
    this.cards = Lists.newArrayList(hand.cards);
  }

  public List<Card> getCards() {
    return cards;
  }

  public void add(Card card) {
    cards.add(card);
  }

  public void remove(Card card) {
    cards.remove(card);
  }

  public String toString() {
    Set<Integer> indices = Sets.newTreeSet(Collections.reverseOrder());
    for (Card card : cards) {
      indices.add(card.getIndex());
    }

    List<Card> sortedCards = Lists.newArrayList();
    for (int index : indices) {
      sortedCards.add(Card.getCardFromIndex(index));
    }

    return Joiner.on(" ").join(sortedCards);
  }

  @Override
  public Iterator<Card> iterator() {
    return new HandIterator();
  }

  private class HandIterator implements Iterator<Card> {
    private int index;

    public HandIterator() {
      index = 0;
    }

    @Override
    public boolean hasNext() {
      return index < cards.size();
    }

    @Override
    public Card next() {
      return cards.get(index++);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
