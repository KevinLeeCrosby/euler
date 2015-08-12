package net.euler.problems;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.io.InputStream;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

/**
 * Su Doku (Japanese meaning number place) is the name given to a popular puzzle concept. Its origin is unclear, but
 * credit must be attributed to Leonhard Euler who invented a similar, and much more difficult, puzzle idea called
 * Latin Squares. The objective of Su Doku puzzles, however, is to replace the blanks (or zeros) in a 9 by 9 grid in
 * such that each row, column, and 3 by 3 box contains each of the digits 1 to 9.
 *
 * A well constructed Su Doku puzzle has a unique solution and can be solved by logic, although it may be necessary to
 * employ "guess and test" methods in order to eliminate options (there is much contested opinion over this). The
 * complexity of the search determines the difficulty of the puzzle; the example above is considered easy because it
 * can be solved by straight forward direct deduction.
 *
 * The 6K text file, sudoku.txt (right click and 'Save Link/Target As...'), contains fifty different Su Doku puzzles
 * ranging in difficulty, but all with unique solutions (the first puzzle in the file is the example above).
 *
 * By solving all fifty puzzles find the sum of the 3-digit numbers found in the top left corner of each solution grid;
 * for example, 483 is the 3-digit number found in the top left corner of the solution grid above.
 *
 * @author Kevin Crosby
 */
public class P096 {
  private static List<Grid> loadGrids(final InputStream is) {
    List<Grid> grids = Lists.newArrayList();

    Scanner fileScan = new Scanner(is).useDelimiter("\\n");
    while(fileScan.hasNext()) {
      String header = fileScan.nextLine();
      int[][] grid = new int[9][9];
      for(int i = 0; i < 9; ++i) {
        String line = fileScan.nextLine();
        for(int j = 0; j < 9; ++j) {
          grid[i][j] = Integer.parseInt(line.substring(j, j + 1));
        }
      }
      grids.add(new Grid(grid));
    }
    fileScan.close();

    return grids;
  }

  private static class Grid {
    private int[][] matrix;

    private Grid(final int[][] matrix) {
      this.matrix = matrix;
    }

    private Grid(final Grid grid) {
      matrix = new int[9][9];
      for(int i = 0; i < 9; ++i) {
        System.arraycopy(grid.matrix[i], 0, matrix[i], 0, 9);
      }
    }

    private boolean all() {
      for(int i = 0; i < 9; ++i) {
        for(int j = 0; j < 9; ++j) {
          if(matrix[i][j] == 0) {
            return false;
          }
        }
      }
      return true;
    }

    private Pair<Integer, Integer> next() {
      for(int i = 0; i < 9; ++i) {
        for(int j = 0; j < 9; ++j) {
          if(matrix[i][j] == 0) {
            return Pair.of(i, j);
          }
        }
      }
      return null;
    }

    private Grid sudoku() {
      // SUDOKU  Solve Sudoku using recursive backtracking.
      //   sudoku(X), expects a 9-by-9 array X.
      //  Fill in all “singletons”.
      //  C is a cell array of candidate vectors for each cell.
      //  s is the first cell, if any, with one candidate.
      //  e is the first cell, if any, with no candidates.
      Triple<BitSet[][], Boolean, Boolean> candidates = candidates();
      BitSet[][] c = candidates.getLeft();
      boolean singletons = candidates.getMiddle();
      boolean empties = candidates.getRight();
      while(singletons && !empties && !all()) {
        candidates = candidates();
        c = candidates.getLeft();
        singletons = candidates.getMiddle();
        empties = candidates.getRight();
      }
      // Return for impossible puzzles.
      if(empties) {
        return null;
      }
      // Recursive backtracking.
      Pair<Integer, Integer> pair = next();
      if(pair != null) {
        int i = pair.getLeft(), j = pair.getRight();
        for(int r = c[i][j].nextSetBit(0); r >= 0; r = c[i][j].nextSetBit(r + 1)) {
          Grid x = new Grid(this);
          x.matrix[i][j] = r;
          x = x.sudoku();
          if(x != null && x.all()) {
            return x;
          }
        }
      }
      return this;
    }

    private Triple<BitSet[][], Boolean, Boolean> candidates() {
      BitSet[][] c = new BitSet[9][9];
      for(int i = 0; i < 9; ++i) {
        for(int j = 0; j < 9; ++j) {
          if(matrix[i][j] == 0) {
            c[i][j] = BitSet.valueOf(new long[]{0b1111111110L});
            for(int m = 0; m < 9; ++m) {
              if(matrix[m][j] != 0) {
                c[i][j].clear(matrix[m][j]);
              }
            }
            for(int n = 0; n < 9; ++n) {
              if(matrix[i][n] != 0) {
                c[i][j].clear(matrix[i][n]);
              }
            }
            for(int m = 3 * (i / 3); m < 3 * (i / 3 + 1); ++m) {
              for(int n = 3 * (j / 3); n < 3 * (j / 3 + 1); ++n) {
                if(matrix[m][n] != 0) {
                  c[i][j].clear(matrix[m][n]);
                }
              }
            }
          }
        }
      }
      boolean singletons = false, empties = false;
      for(int i = 0; i < 9; ++i) {
        for(int j = 0; j < 9; ++j) {
          if(matrix[i][j] == 0) {
            int length = c[i][j].cardinality();
            switch(length) {
              case 0:
                empties = true;
                break;
              case 1:
                if (!singletons) {
                  matrix[i][j] = c[i][j].nextSetBit(0);
                  singletons = true;
                }
                break;
            }
          }
        }
      }
      return Triple.of(c, singletons, empties);
    }

    public int number() {
      int number = 0;
      for(int j = 0; j < 3; ++j) {
        number = 10 * number + matrix[0][j];
      }
      return number;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      int i, j;
      for(i = 0; i < 9; ++i) {
        if(i % 3 == 0) {
          sb.append("+-------+-------+-------+\n");
        }
        for(j = 0; j < 9; ++j) {
          if(j % 3 == 0) {
            sb.append("| ");
          }
          sb.append(matrix[i][j]).append(" ");
        }
        sb.append("|\n");
      }
      sb.append("+-------+-------+-------+\n");
      return sb.toString();
    }
  }

  public static void main(String[] args) {
    final String file = "/net/euler/problems/p096.txt";
    final InputStream is = P099.class.getResourceAsStream(file);

    int sum = 0;
    for(final Grid grid : loadGrids(is)) {
      Grid solution = grid.sudoku();
      assert solution != null && solution.all();
      sum += solution.number();
    }
    System.out.printf("The sum of the 3-digit numbers found in the top left corner of each solution grid is %d\n", sum);
  }
}
