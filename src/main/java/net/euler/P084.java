package net.euler;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * In the game, Monopoly, the standard board is set up in the following way:
 *
 * A player starts on the GO square and adds the scores on two 6-sided dice to determine the number of squares they
 * advance in a clockwise direction. Without any further rules we would expect to visit each square with equal
 * probability: 2.5%. However, landing on G2J (Go To Jail), CC (community chest), and CH (chance) changes this
 * distribution.
 *
 * In addition to G2J, and one card from each of CC and CH, that orders the player to go directly to jail, if a player
 * rolls three consecutive doubles, they do not advance the result of their 3rd roll. Instead they proceed directly to
 * jail.
 *
 * At the beginning of the game, the CC and CH cards are shuffled. When a player lands on CC or CH they take a card
 * from
 * the top of the respective pile and, after following the instructions, it is returned to the bottom of the pile.
 * There
 * are sixteen cards in each pile, but for the purpose of this problem we are only concerned with cards that order a
 * movement; any instruction not concerned with movement will be ignored and the player will remain on the CC/CH
 * square.
 *
 * Community Chest (2/16 cards):
 * - Advance to GO
 * - Go to JAIL
 *
 * Chance (10/16 cards):
 * - Advance to GO
 * - Go to JAIL
 * - Go to C1
 * - Go to E3
 * - Go to H2
 * - Go to R1
 * - Go to next R (railway company)
 * - Go to next R
 * - Go to next U (utility company)
 * - Go back 3 squares.
 *
 * The heart of this problem concerns the likelihood of visiting a particular square. That is, the probability of
 * finishing at that square after a roll. For this reason it should be clear that, with the exception of G2J for which
 * the probability of finishing on it is zero, the CH squares will have the lowest probabilities, as 5/8 request a
 * movement to another square, and it is the final square that the player finishes at on each roll that we are
 * interested in. We shall make no distinction between "Just Visiting" and being sent to JAIL, and we shall also ignore
 * the rule about requiring a double to "get out of jail", assuming that they pay to get out on their next turn.
 *
 * By starting at GO and numbering the squares sequentially from 00 to 39 we can concatenate these two-digit numbers to
 * produce strings that correspond with sets of squares.
 *
 * Statistically it can be shown that the three most popular squares, in order, are JAIL (6.24%) = Square 10, E3
 * (3.18%)
 * = Square 24, and GO (3.09%) = Square 00. So these three most popular squares can be listed with the six-digit modal
 * string: 102400.
 *
 * If, instead of using two 6-sided dice, two 4-sided dice are used, find the six-digit modal string.
 *
 * @author Kevin Crosby.
 */
public class P084 {
  private static Square square;

  private static Random random = new Random();
  private static int sides;

  // Community Chest and Chance card indices.
  private List<Integer> ccStack = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
  private List<Integer> chStack = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

  private int ccIndex, chIndex;

  private P084() {
    Collections.shuffle(ccStack);
    Collections.shuffle(chStack);
    ccIndex = 0;
    chIndex = 0;
    square = Square.GO;
  }

  private static Map<Integer, Square> map = new HashMap<Integer, Square>() {{
    put(0, Square.GO);
    put(1, Square.A1);
    put(2, Square.CC1);
    put(3, Square.A2);
    put(4, Square.T1);
    put(5, Square.R1);
    put(6, Square.B1);
    put(7, Square.CH1);
    put(8, Square.B2);
    put(9, Square.B3);
    put(10, Square.JAIL);
    put(11, Square.C1);
    put(12, Square.U1);
    put(13, Square.C2);
    put(14, Square.C3);
    put(15, Square.R2);
    put(16, Square.D1);
    put(17, Square.CC2);
    put(18, Square.D2);
    put(19, Square.D3);
    put(20, Square.FP);
    put(21, Square.E1);
    put(22, Square.CH2);
    put(23, Square.E2);
    put(24, Square.E3);
    put(25, Square.R3);
    put(26, Square.F1);
    put(27, Square.F2);
    put(28, Square.U2);
    put(29, Square.F3);
    put(30, Square.G2J);
    put(31, Square.G1);
    put(32, Square.G2);
    put(33, Square.CC3);
    put(34, Square.G3);
    put(35, Square.R4);
    put(36, Square.CH3);
    put(37, Square.H1);
    put(38, Square.T2);
    put(39, Square.H2);
  }};

  private static enum Square {
    GO(0), A1(1), CC1(2), A2(3), T1(4), R1(5), B1(6), CH1(7), B2(8), B3(9),
    JAIL(10), C1(11), U1(12), C2(13), C3(14), R2(15), D1(16), CC2(17), D2(18), D3(19),
    FP(20), E1(21), CH2(22), E2(23), E3(24), R3(25), F1(26), F2(27), U2(28), F3(29),
    G2J(30), G1(31), G2(32), CC3(33), G3(34), R4(35), CH3(36), H1(37), T2(38), H2(39);

    private int number;

    private Square(final int number) {
      this.number = number;
    }

    public int getNumber() {
      return number;
    }

    public final Square getSquare(final int number) {
      return map.get(number % 40);
    }
  }

  private void drawCommunityChestCard() {
    assert square == Square.CC1 || square == Square.CC2 || square == Square.CC3 : "Not on a Community Chest square!";
    switch (ccStack.get(ccIndex)) {
      case 0:
        square = Square.GO;
        break;
      case 1:
        square = Square.JAIL;
        break;
      default:
        // stay put
    }
    ccIndex = (ccIndex + 1) % 16;
  }

  private void drawChanceCard() {
    assert square == Square.CH1 || square == Square.CH2 || square == Square.CH3 : "Not on a Chance square!";
    switch (chStack.get(chIndex)) {
      case 0:
        square = Square.GO;
        break;
      case 1:
        square = Square.JAIL;
        break;
      case 2:
        square = Square.C1;
        break;
      case 3:
        square = Square.E3;
        break;
      case 4:
        square = Square.H2;
        break;
      case 5:
        square = Square.R1;
        break;
      case 6: // Go to next R (railway company)
      case 7:
        switch (square) {
          case CH1:
            square = Square.R2;
            break;
          case CH2:
            square = Square.R3;
            break;
          case CH3:
            square = Square.R1;
            break;
        }
        break;
      case 8: // Go to next U (utility company)
        switch (square) {
          case CH1:
          case CH3:
            square = Square.U1;
            break;
          case CH2:
            square = Square.U2;
            break;
        }
        break;
      case 9: // Go back 3 squares.
        square = square.getSquare(square.number - 3);
        if (square == Square.CC3) { drawCommunityChestCard(); };
        break;
      default:
        // stay put
    }
    chIndex = (chIndex + 1) % 16;
  }

  private void roll() {
    int dice = random.nextInt(sides) + random.nextInt(sides) + 2;
    square = square.getSquare(square.number + dice);
    switch (square) {
      case CC1:
      case CC2:
      case CC3:
        drawCommunityChestCard();
        break;
      case CH1:
      case CH2:
      case CH3:
        drawChanceCard();
        break;
      case G2J:
        square = Square.JAIL;
        break;
    }
  }

  public static void main(String[] args) {
    sides = args.length > 0 ? Integer.parseInt(args[0]) : 4;

    Counter<Square> counter = new Counter<>();
    for (int i = 0; i < 10000; i++) {
      P084 game = new P084();
      counter.increment(square);
      for (int r = 1; r < 10000; r++) {
        game.roll();
        counter.increment(square);
      }
    }

    System.out.println(counter);

    int i = 0;
    int total = counter.getTotal();
    List<String> squares = Lists.newArrayList();
    StringBuilder numbers = new StringBuilder();
    for (Map.Entry<Square, Integer> entry : counter.descendingSortByCount()) {
      if (++i > 3) break;
      Square sq = entry.getKey();
      int count = entry.getValue();
      int number = sq.getNumber();
      double percent = 100.0 * count / total;
      squares.add(String.format("%s (%.2f%%)", sq, percent));
      numbers.append(String.format("%02d", number));
    }
    System.out.println("Top 3 squares:  " + Joiner.on(", ").join(squares) + " -> " + numbers);
  }

}
