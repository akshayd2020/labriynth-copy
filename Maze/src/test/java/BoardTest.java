import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.Random;

import org.jgrapht.alg.util.UnorderedPair;
import javafx.util.Pair;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


// TODO change tests to have only odd x odd rows and columns

public class BoardTest {
     UnorderedPair<Gem, Gem> berylZircon;
     UnorderedPair<Gem, Gem> zirconZoisite;
     UnorderedPair<Gem, Gem> zirconRuby;
     UnorderedPair<Gem, Gem> rubyJasper;
     UnorderedPair<Gem, Gem> jasperZoisite;
     Tile bZTile;
     Board testBoardOneTile;
     Board testBoard7x7Expected;
     Board testBoard7x7;
     Board testBoardVertBar;
     Board testBoardCorner0;
     Board testBoardCorner90;
     Board testBoardCorner180;
     Board testBoardCorner270;
     Board testBoardTShape0;
     Board testBoardTShape90;
     Board testBoardTShape180;
     Board testBoardTShape270;
     Board testBoardBarOnly;
     Board testBoardHorizBarOnly;
     Board testBoardDiffDirections;
     Board boardAllMinusBar;

     Tile[][] testGrid7x7Expected;
     Tile[][] testGrid7x7;
     Tile[][] testGridVertBar;
     List<Tile> vertBarTiles15x1;
     List<Tile> corner180Tiles;
     List<Tile> corner270Tiles;
     List<Tile> tShape0Tiles;
     List<Tile> tShape180Tiles;
     List<Tile> barOnlyTiles;
     List<Tile> horizBarOnlyTiles;

     List<Tile> horizBarTiles;
     Board testBoardHorizBar;

     @BeforeEach
     public void setUp() {
          berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
          zirconZoisite = new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE);
          zirconRuby = new UnorderedPair(Gem.ZIRCON, Gem.RUBY);
          rubyJasper = new UnorderedPair(Gem.RUBY, Gem.JASPER);
          jasperZoisite = new UnorderedPair(Gem.JASPER, Gem.ZOISITE);
          bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));

          Tile[][] singleTileBoard = new Tile[1][1];
          singleTileBoard[0][0] = bZTile;

          boardAllMinusBar = BoardTest.createBoard(7, 7, BoardTest.generateTiles(BoardTest.generateGemPairs(49),
                  BoardTest.get49Bar90Connectors(), 49));
          testBoardOneTile = new Board(singleTileBoard);
          testBoard7x7Expected= BoardTest.generateStaticBoard();
          testBoard7x7 = BoardTest.generateStaticBoard();
          testGrid7x7Expected = testBoard7x7Expected.getGrid();
          testGrid7x7 = testBoard7x7.getGrid();
          vertBarTiles15x1 = generateTiles(generateGemPairs(15), getVertBarConnectors(), 15);
          testBoardVertBar = createBoard(1, 15,
              vertBarTiles15x1);

          horizBarTiles = generateTiles(generateGemPairs(16), getHorizBarConnectors(), 16);
          testBoardHorizBar = createBoard(16, 1,
              horizBarTiles);
          corner180Tiles = generateTiles(generateGemPairs(16), getCorner180Connectors(), 16);
          testBoardCorner180 = createBoard(4, 4, corner180Tiles);
          corner270Tiles = generateTiles(generateGemPairs(16), getCorner270Connectors(), 16);
          testBoardCorner270 = createBoard(4, 4, corner270Tiles);
          tShape0Tiles = generateTiles(generateGemPairs(8), getTShape0Connectors(), 8);
          testBoardTShape0 = createBoard(4, 2, tShape0Tiles);
          tShape180Tiles = generateTiles(generateGemPairs(4), getTShape180Connectors(), 4);
          testBoardTShape180 = createBoard(2, 2, tShape180Tiles);
          barOnlyTiles = generateTiles(generateGemPairs(4), getBarOnlyConnectors(), 4);
          testBoardBarOnly = createBoard(2, 2, barOnlyTiles);
          horizBarOnlyTiles = generateTiles(generateGemPairs(3), getHorizBarOnlyConnectors(), 3);
          testBoardHorizBarOnly = createBoard(1, 3, horizBarOnlyTiles);
          testBoardCorner0 = createBoard(4, 4, generateTiles(generateGemPairs(16),
                  getCorner0Connectors(), 16));
          testBoardCorner90 = createBoard(3, 4, generateTiles(generateGemPairs(12),
                  getCorner90Connectors(), 12));
          testBoardTShape90 = createBoard(2, 3, generateTiles(generateGemPairs(6),
                  getTShape90Connectors(), 6));
          testBoardTShape270 = createBoard(2, 1, generateTiles(generateGemPairs(2),
                  getTShape270Connectors(), 2));
          testBoardDiffDirections = createBoard(5, 2, generateTiles(generateGemPairs(10),
                  getDiffConnectors(), 10));
     }

     //region Board equals
     @Test

     public void boardEquals() {
          assertEquals(testBoard7x7Expected, testBoard7x7);
     }

     public void passBoardEquals() {
          Tile[][] grid = boardAllMinusBar.getGridDeepCopy();

          Tile[][] expectedGrid = boardAllMinusBar.getGridDeepCopy();
          assertEquals(new Board(expectedGrid), new Board(grid));
     }
     //endregion

     // TODO write tests for trying construct board test that invalid even dimensions
     // TODO write tests to catch the illegal exception for insert

     //region generateSpecificConnectors()
     /**
      * Gets the connectors for the vertical bar connector test
      * @return     list of connectors for vert bar test
      */
     private List<Connector> getHorizBarConnectors() {
          List<Connector> horizBarConnectors = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.CORNER, Orientation.ZERO),
                  new Connector(Unicode.BAR, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.CORNER, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.ZERO),
                  new Connector(Unicode.T_SHAPE, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.PLUS, Orientation.ZERO)
          }));
          return horizBarConnectors;
     }

     /**
      * Gets the connectors for the vertical bar connector test
      * @return     list of connectors for vert bar test
      */
     private List<Connector> getVertBarConnectors() {
          List<Connector> vertBarConnectors = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.CORNER, Orientation.ZERO),
                  new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.BAR, Orientation.ZERO),
                  new Connector(Unicode.CORNER, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.ZERO),
                  new Connector(Unicode.BAR, Orientation.ZERO),
                  new Connector(Unicode.T_SHAPE, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.ZERO),
                  new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.BAR, Orientation.ZERO),
                  new Connector(Unicode.PLUS, Orientation.ZERO),
                  new Connector(Unicode.BAR, Orientation.ZERO),
                  new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY)
          }));
          return vertBarConnectors;
     }

     /**
      * Gets the connectors for the corner with 180 degree orientation connector test
      * @return     list of connectors for corner with 180 degree test
      */
     private List<Connector> getCorner180Connectors() {
          List<Connector> corner180Connectors = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.T_SHAPE, Orientation.ZERO),
                  new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.CORNER, Orientation.ZERO),
                  new Connector(Unicode.T_SHAPE, Orientation.NINETY),
                  new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.CORNER, Orientation.NINETY),
                  new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.PLUS, Orientation.ONE_EIGHTY)
          }));
          return corner180Connectors;
     }

     /**
      * Gets the connectors for the corner with 270 degree orientation connector test
      * @return     list of connectors for corner with 270 degree test
      */
     private List<Connector> getCorner270Connectors() {
          List<Connector> corner270connectors = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.PLUS, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.CORNER, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.NINETY),
                  new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.T_SHAPE, Orientation.ZERO),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.NINETY)
          }));
          return corner270connectors;
     }

     /**
      * Gets the connectors for the t-shape with 0 degree orientation connector test
      * @return     list of connectors for t-shape with 0 degree test
      */
     private List<Connector> getTShape0Connectors() {
          List<Connector> tShape0Tiles = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.PLUS, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.T_SHAPE, Orientation.ZERO),
                  new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.ZERO),
                  new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY)
          }));
          return tShape0Tiles;
     }

     /**
      * Gets the connectors for the t-shape with 180 degree orientation connector test
      * @return     list of connectors for t-shape with 180 degree test
      */
     private List<Connector> getTShape180Connectors() {
          List<Connector> tShape180Tiles = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
                  new Connector(Unicode.PLUS, Orientation.NINETY),
                  new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY)
          }));
          return tShape180Tiles;
     }

     /**
      * Gets the connectors for the bar only connector test
      * --
      * ||
      * @return     list of connectors for bar only test
      */
     private List<Connector> getBarOnlyConnectors() {
          List<Connector> barOnly = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.ONE_EIGHTY),
                  new Connector(Unicode.BAR, Orientation.ONE_EIGHTY)
          }));
          return barOnly;
     }

     /**
      * Gets the connectors for the horizontal bar only connector test
      * -
      * -
      * -
      * @return     list of connectors for horizontal bar only test
      */
     private List<Connector> getHorizBarOnlyConnectors() {
          List<Connector> barOnly = new ArrayList<>(Arrays.asList(new Connector[] {
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.NINETY),
                  new Connector(Unicode.BAR, Orientation.NINETY)
          }));
          return barOnly;

     }

     /**
      * Gets the connectors for a board with different directions test
      * @return     list of connectors for vert bar test
      */
     private List<Connector> getDiffConnectors() {
          List<Connector> diffConnectors = new ArrayList<>(Arrays.asList(new Connector[] {
              new Connector(Unicode.BAR, Orientation.TWO_SEVENTY),
              new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.PLUS, Orientation.TWO_SEVENTY),
              new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY),
              new Connector(Unicode.BAR, Orientation.TWO_SEVENTY),
              new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
              new Connector(Unicode.BAR, Orientation.TWO_SEVENTY),
              new Connector(Unicode.CORNER, Orientation.NINETY),
              new Connector(Unicode.BAR, Orientation.ONE_EIGHTY)
          }));
          return diffConnectors;
     }

     /**
      * Gets the connectors for the T Shape at orientation 270 connector test
      * @return     list of connectors for vert bar test
      */
     private List<Connector> getTShape270Connectors() {
          List<Connector> tShape270Connectors = new ArrayList<>(Arrays.asList(new Connector[] {
              new Connector(Unicode.PLUS, Orientation.TWO_SEVENTY),
              new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY)
          }));
          return tShape270Connectors;
     }

     /**
      * Gets the connectors for the T Shape at orientation 90 connector test
      * @return     list of connectors T Shape at orientation 90 connector test
      */
     private List<Connector> getTShape90Connectors() {
          List<Connector> tShape90Connectors = new ArrayList<>(Arrays.asList(new Connector[] {
              new Connector(Unicode.PLUS, Orientation.TWO_SEVENTY),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
              new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
              new Connector(Unicode.BAR, Orientation.NINETY)
          }));
          return tShape90Connectors;
     }

     /**
      * Gets the connectors for the Corner at orientation 90 connector test
      * @return     list of connectors for vert bar test
      */
     private List<Connector> getCorner90Connectors() {
          List<Connector> corner90Connectors = new ArrayList<>(Arrays.asList(new Connector[] {
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.ZERO),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.NINETY),
              new Connector(Unicode.CORNER, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
              new Connector(Unicode.CORNER, Orientation.NINETY),
              new Connector(Unicode.PLUS, Orientation.NINETY),
              new Connector(Unicode.CORNER, Orientation.NINETY),
              new Connector(Unicode.BAR, Orientation.NINETY)
          }));
          return corner90Connectors;
     }

     /**
      * Gets the connectors for the Corner at orientation 0 connector test
      * @return     list of connectors for vert bar test
      */
     private List<Connector> getCorner0Connectors() {
          List<Connector> horizBarConnectors = new ArrayList<>(Arrays.asList(new Connector[] {
              new Connector(Unicode.T_SHAPE, Orientation.ZERO),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.CORNER, Orientation.ZERO),
              new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
              new Connector(Unicode.CORNER, Orientation.ZERO),
              new Connector(Unicode.CORNER, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.NINETY),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY),
              new Connector(Unicode.BAR, Orientation.NINETY),
              new Connector(Unicode.CORNER, Orientation.ZERO),
              new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY),
              new Connector(Unicode.CORNER, Orientation.ZERO),
              new Connector(Unicode.BAR, Orientation.NINETY)
          }));
          return horizBarConnectors;

     }

     public static List<Connector> get49Bar90Connectors() {
          List<Connector> ans = new ArrayList<>();
          for (int i = 0; i < 49; i += 1) {
               ans.add(new Connector(Unicode.BAR, Orientation.NINETY));
          }
          return ans;
     }
     //endregion

     //region generate board functions
     /**
      * Generates a board, with both grid
      * @param randomSeed     random
      * @param width          width of the grid
      * @param height         height of the grid
      * @return               a board, with both grid and spare tile
      */
     public static Board generateBoard(int randomSeed, int width, int height) {
          int totalTiles = width * height;
          Random random = new Random(randomSeed);
          List<Tile> tiles = generateRandomTiles(random, totalTiles);
          return createBoard(width, height, tiles);
     }

     /**
      * Generates a specific 7x7 board used for testing.
      */
     public static Board generateStaticBoard() {
          Character[] connectors = new Character[] {
                  '┬', '┘', '│', '┐', '┼', '│', '┼',
                  '┼', '┼', '┼', '┌', '└', '┘', '┤',
                  '│', '┼', '┬', '─', '┐', '─', '├',
                  '┼', '─', '┬', '┴', '─', '└', '┴',
                  '┤', '─', '┬', '│', '│', '─', '─',
                  '┬', '┼', '│', '┌', '─', '┌', '│',
                  '┼', '─', '┬', '┬', '─', '┴', '┼'
          };
          Gem[] gems = new Gem[] {
                  Gem.PINK_SC, Gem.PURPLE_ST, Gem.UNAKITE, Gem.AMMOLITE, Gem.AQUAMARINE, Gem.LEMON_QB, Gem.RUBY, Gem.GREEN_PC, Gem.SPINEL, Gem.YELLOW_JASPER, Gem.BLACK_SPINAL_C, Gem.YELLOW_HEART, Gem.ZIRCON, Gem.PINK_OPAL,
                  Gem.SPINEL, Gem.CHRYSOLITE, Gem.BLUE_CS, Gem.AMETRINE, Gem.ZOISITE, Gem.PINK_EC, Gem.BERYL, Gem.RED_SPINAL_SEC, Gem.STILBITE, Gem.PRASIOLITE, Gem.RED_SPINAL_SEC, Gem.SPINEL, Gem.AQUAMARINE, Gem.ZIRCON,
                  Gem.YELLOW_BAGUETTE, Gem.SPHALERITE, Gem.BLUE_PS, Gem.TANZANITE_TRILLION, Gem.GARNET, Gem.EMERALD, Gem.APATITE, Gem.WHITE_SQUARE, Gem.TANZANITE_TRILLION, Gem.PURPLE_OVAL, Gem.BLUE_SH, Gem.RED_SPINAL_SEC, Gem.ALEXANDRITE_PS, Gem.STILBITE,
                  Gem.APATITE, Gem.PADPARADSCHA_OVAL, Gem.LEMON_QB, Gem.CHRYSOLITE, Gem.GRAY_A, Gem.HELIOTROPE, Gem.PINK_SC, Gem.PINK_OPAL, Gem.LAPIS_L, Gem.BLACK_ONYX, Gem.BLUE_SH, Gem.PINK_ROUND, Gem.LEMON_QB, Gem.PINK_EC,
                  Gem.KUNZITE, Gem.MOSS_A, Gem.LAPIS_L, Gem.YELLOW_BO, Gem.DIAMOND, Gem.CLINOHUMITE, Gem.WHITE_SQUARE, Gem.TOURMALINE_LC, Gem.PINK_ROUND, Gem.BLUE_C, Gem.PREHNITE, Gem.PRASIOLITE, Gem.KUNZITE_O, Gem.BLUE_C,
                  Gem.PINK_OPAL, Gem.ROCK_QUARTZ, Gem.HELIOTROPE, Gem.PURPLE_CABOCHON, Gem.AMETRINE, Gem.CARNELIAN, Gem.AZURITE, Gem.AUSTRALIAN_M, Gem.CITRINE, Gem.CHRYSOBERYL_C, Gem.GROSSULAR_G, Gem.ORANGE_R, Gem.RAW_CITRINE, Gem.GRANDIDIERITE,
                  Gem.APRICOT_SR, Gem.IOLITE_EC, Gem.PURPLE_SC, Gem.CITRINE, Gem.GARNET, Gem.MOSS_A, Gem.TANZANITE_TRILLION, Gem.PURPLE_CABOCHON, Gem.GARNET, Gem.HELIOTROPE, Gem.GOLDSTONE, Gem.BLACK_ONYX, Gem.UNAKITE, Gem.HACKMANITE
          };
          List<Tile> tiles = createTilesWithArrays(connectors, gems);
          return createBoard(7, 7, tiles);
     }

     /**
      * Generates a specific 7x7 board used for testing, where all tiles
      * are reachable from anywhere on the board.
      */
     public static Board generateStaticPlusBoard() {
          Character[] connectors = new Character[] {
                  '┼', '┼', '┼', '┼', '┼', '┼', '┼',
                  '┼', '┼', '┼', '┼', '┼', '┼', '┼',
                  '┼', '┼', '┼', '┼', '┼', '┼', '┼',
                  '┼', '┼', '┼', '┼', '┼', '┼', '┼',
                  '┼', '┼', '┼', '┼', '┼', '┼', '┼',
                  '┼', '┼', '┼', '┼', '┼', '┼', '┼',
                  '┼', '┼', '┼', '┼', '┼', '┼', '┼'
          };
          Gem[] gems = new Gem[] {
                  Gem.PINK_SC, Gem.PURPLE_ST, Gem.UNAKITE, Gem.AMMOLITE, Gem.AQUAMARINE, Gem.LEMON_QB, Gem.RUBY, Gem.GREEN_PC, Gem.SPINEL, Gem.YELLOW_JASPER, Gem.BLACK_SPINAL_C, Gem.YELLOW_HEART, Gem.ZIRCON, Gem.PINK_OPAL,
                  Gem.SPINEL, Gem.CHRYSOLITE, Gem.BLUE_CS, Gem.AMETRINE, Gem.ZOISITE, Gem.PINK_EC, Gem.BERYL, Gem.RED_SPINAL_SEC, Gem.STILBITE, Gem.PRASIOLITE, Gem.RED_SPINAL_SEC, Gem.SPINEL, Gem.AQUAMARINE, Gem.ZIRCON,
                  Gem.YELLOW_BAGUETTE, Gem.SPHALERITE, Gem.BLUE_PS, Gem.TANZANITE_TRILLION, Gem.GARNET, Gem.EMERALD, Gem.APATITE, Gem.WHITE_SQUARE, Gem.TANZANITE_TRILLION, Gem.PURPLE_OVAL, Gem.BLUE_SH, Gem.RED_SPINAL_SEC, Gem.ALEXANDRITE_PS, Gem.STILBITE,
                  Gem.APATITE, Gem.PADPARADSCHA_OVAL, Gem.LEMON_QB, Gem.CHRYSOLITE, Gem.GRAY_A, Gem.HELIOTROPE, Gem.PINK_SC, Gem.PINK_OPAL, Gem.LAPIS_L, Gem.BLACK_ONYX, Gem.BLUE_SH, Gem.PINK_ROUND, Gem.LEMON_QB, Gem.PINK_EC,
                  Gem.KUNZITE, Gem.MOSS_A, Gem.LAPIS_L, Gem.YELLOW_BO, Gem.DIAMOND, Gem.CLINOHUMITE, Gem.WHITE_SQUARE, Gem.TOURMALINE_LC, Gem.PINK_ROUND, Gem.BLUE_C, Gem.PREHNITE, Gem.PRASIOLITE, Gem.KUNZITE_O, Gem.BLUE_C,
                  Gem.PINK_OPAL, Gem.ROCK_QUARTZ, Gem.HELIOTROPE, Gem.PURPLE_CABOCHON, Gem.AMETRINE, Gem.CARNELIAN, Gem.AZURITE, Gem.AUSTRALIAN_M, Gem.CITRINE, Gem.CHRYSOBERYL_C, Gem.GROSSULAR_G, Gem.ORANGE_R, Gem.RAW_CITRINE, Gem.GRANDIDIERITE,
                  Gem.APRICOT_SR, Gem.IOLITE_EC, Gem.PURPLE_SC, Gem.CITRINE, Gem.GARNET, Gem.MOSS_A, Gem.TANZANITE_TRILLION, Gem.PURPLE_CABOCHON, Gem.GARNET, Gem.HELIOTROPE, Gem.GOLDSTONE, Gem.BLACK_ONYX, Gem.UNAKITE, Gem.HACKMANITE
          };
          List<Tile> tiles = createTilesWithArrays(connectors, gems);
          return createBoard(7, 7, tiles);
     }

     private static List<Tile> createTilesWithArrays(Character[] chars, Gem[] gems) {
          List<Tile> tiles = new ArrayList<>();
          for (int i = 0; i < chars.length; i++) {
               tiles.add(new Tile(new UnorderedPair<>(gems[i*2], gems[i*2 + 1]), Connector.convertCharToConnector(chars[i])));
          }
          return tiles;
     }

     /**
      * Creates a board with the given dimensions and tiles
      * @param width     width of board
      * @param height    height of board
      * @param tiles     tiles to be placed on board including spare
      * @return          board
      */
     public static Board createBoard(int width, int height, List<Tile> tiles) {
          Tile[][] grid = new Tile[height][width];
          int count = 0;
          int tilesSize = tiles.size();
          for (int row = 0; row < height; row += 1) {
               Tile[] rowTiles = new Tile[width];
               for (int col = 0; col < width; col += 1) {
                    int index = count;
                    rowTiles[col] = tiles.get(index);
                    count += 1;
               }
               grid[row] = rowTiles;
          }
          return new Board(grid);
     }

     /**
      * Generates a tiles with random pathways
      * @param random         random object
      * @param totalTiles     number of tiles to generate
      * @return               list of tiles
      */
     private static List<Tile> generateRandomTiles(Random random, int totalTiles) {
          List<UnorderedPair<Gem, Gem>> gemPairs = generateGemPairs(totalTiles);
          List<Connector> connectors = generateConnectors(random, totalTiles);
          return generateTiles(gemPairs, connectors, totalTiles);
     }

     /**
      * Generates a list of tiles
      * @param gemPairs       gem pairs for the corresponding tile
      * @param connectors     connector for the corresponding tile
      * @param totalTiles     total number of tiles to generate
      * @return               list of tiles from the given gem pairs and connectors
      */
     public static List<Tile> generateTiles(List<UnorderedPair<Gem, Gem>> gemPairs,
         List<Connector> connectors, int totalTiles) {
          List<Tile> tiles = new ArrayList<>();

          for (int i = 0; i < totalTiles; i += 1) {
               tiles.add(new Tile(gemPairs.get(i), connectors.get(i)));
          }
          return tiles;
     }

     /**
      * Generate gem sets of the given size, where each pair of gems is unique and no pair contains
      * two of the same gems.
      * @param totalGemPairs     number of gem pairs.
      * @return                  list of gem pairs.
      */
     public static List<UnorderedPair<Gem, Gem>> generateGemPairs(int totalGemPairs) {
          List<UnorderedPair<Gem, Gem>> gemPairs = new ArrayList<>();
          Random random = new Random(0);
          int count = 0;
          while (count != totalGemPairs) {
               Gem gem1 = Gem.values()[random.nextInt(Gem.values().length)];
               Gem gem2 = Gem.values()[random.nextInt(Gem.values().length)];
               UnorderedPair<Gem, Gem> randomGemPair = new UnorderedPair<>(gem1, gem2);
               if (!gem1.equals(gem2) && !gemPairs.contains(randomGemPair)) {
                    gemPairs.add(randomGemPair);
                    count++;
               }
          }
          return gemPairs;
     }

     private static List<List<String>> generateGemPairStrings(int totalGemPairs) {
          List<List<String>> gemPairs = new ArrayList<>();
          // TODO can this be cleaner?
          int count = 0;
          for (Gem gem1 :  Gem.values()) {
               for (Gem gem2 :  Gem.values()) {
                    List<String> curr = new ArrayList<>();
                    curr.add("\"" + gem1.toString() + "\"");
                    curr.add("\"" + gem2.toString() + "\"");
                    gemPairs.add(curr);
                    count += 1;
                    if (count == totalGemPairs) {
                         return gemPairs;
                    }
               }
          }
          return gemPairs;
     }

     /**
      * Generate connectors of the given size
      * @param random              random object
      * @param totalConnectors     number of connectors
      * @return                    list of connectors
      */
     private static List<Connector> generateConnectors(Random random, int totalConnectors) {
          List<Connector> connectors = new ArrayList<>();

          for (int index = 0; index < totalConnectors; index += 1) {
               connectors.add(Connector.getRandom(random));
          }
          return connectors;
     }
     //endregion

     //region getGrid()
     @Test
     public void passGetGrid() {
          assertEquals(testGrid7x7, testBoard7x7.getGrid(), "The getGrid() function failed");
     }
     //endregion

     //region Tile
     @Test
     public void failGetTile() {
          IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                  () -> testBoard7x7.getTile(-1, 6));
          assertEquals("Cannot get a tile that is out of bounds", e.getMessage());

     }

     @Test
     public void passGetTile() {
          assertEquals(new Tile(new UnorderedPair(Gem.PINK_SC, Gem.PURPLE_ST),
                  new Connector(Unicode.T_SHAPE, Orientation.ZERO)), testBoard7x7.getTile(0, 0));
     }

     @Test
     public void passGetDeepCopy() {
          assertEquals(new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO)), bZTile.getDeepCopy());
     }

     @Test
     public void passGetConnector() {
          assertEquals(new Connector(Unicode.CORNER, Orientation.ZERO),
                  bZTile.getConnector(),
                  "Get connector failed.");
     }

     @Test
     public void passConnectorEquals() {
          assertEquals(new Connector(Unicode.CORNER, Orientation.ZERO), new Connector(Unicode.CORNER, Orientation.ZERO));
     }

     @Test
     public void passConnectorHashCode() {
          assertEquals(new Connector(Unicode.CORNER, Orientation.ZERO).hashCode(),
                  new Connector(Unicode.CORNER, Orientation.ZERO).hashCode());

     }

     @Test
     public void passRotateTile() {
          assertEquals(new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY)),
                  bZTile.rotateTile(270));
     }

     @Test
     public void passTileEquals() {
          assertEquals(new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO)), bZTile);
     }

     @Test
     public void passTileHashCode() {
          assertEquals(new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO)).hashCode(),
                  bZTile.hashCode());
     }
     //endregion

     //region slideAndInsert()
     @Test
     public void passSlideAndInsertOnePositionLeft() {
          int row = 1;

          Tile expectedSpare = testGrid7x7Expected[row][0];
          testGrid7x7Expected[row][0] = testGrid7x7Expected[row][1];
          testGrid7x7Expected[row][1] = testGrid7x7Expected[row][2];
          testGrid7x7Expected[row][2] = testGrid7x7Expected[row][3];
          testGrid7x7Expected[row][3] = testGrid7x7Expected[row][4];
          testGrid7x7Expected[row][4] = testGrid7x7Expected[row][5];
          testGrid7x7Expected[row][5] = testGrid7x7Expected[row][6];
          testGrid7x7Expected[row][6] = bZTile;

          Board expectedBoard = new Board(testGrid7x7Expected);
          BoardAndSpare actualBs = testBoard7x7.slideAndInsert(bZTile, 1,
              Direction.LEFT);
          assertEquals(new BoardAndSpare(expectedBoard, expectedSpare), actualBs);
     }

     @Test
     public void passSlideOnePositionRight() {
          int row = 3;

          Tile expectedSpare = testGrid7x7Expected[row][6];
          testGrid7x7Expected[row][6] = testGrid7x7Expected[row][5];
          testGrid7x7Expected[row][5] = testGrid7x7Expected[row][4];
          testGrid7x7Expected[row][4] = testGrid7x7Expected[row][3];
          testGrid7x7Expected[row][3] = testGrid7x7Expected[row][2];
          testGrid7x7Expected[row][2] = testGrid7x7Expected[row][1];
          testGrid7x7Expected[row][1] = testGrid7x7Expected[row][0];
          testGrid7x7Expected[row][0] = bZTile;

          Board expectedBoard = new Board(testGrid7x7Expected);
          BoardAndSpare actualBs = testBoard7x7.slideAndInsert(bZTile, row,
              Direction.RIGHT);
          assertEquals(new BoardAndSpare(expectedBoard, expectedSpare), actualBs);
     }

     @Test
     public void passSlideOnePositionsDown() {
          int col = 5;
          Tile expectedSpare = testGrid7x7Expected[6][col];

          testGrid7x7Expected[6][col] = testGrid7x7Expected[5][col];
          testGrid7x7Expected[5][col] = testGrid7x7Expected[4][col];
          testGrid7x7Expected[4][col] = testGrid7x7Expected[3][col];
          testGrid7x7Expected[3][col] = testGrid7x7Expected[2][col];
          testGrid7x7Expected[2][col] = testGrid7x7Expected[1][col];
          testGrid7x7Expected[1][col] = testGrid7x7Expected[0][col];
          testGrid7x7Expected[0][col] = bZTile;

          Board expectedBoard = new Board(testGrid7x7Expected);
          BoardAndSpare actualBs = testBoard7x7.slideAndInsert(bZTile, col, Direction.DOWN);
          assertEquals(new BoardAndSpare(expectedBoard, expectedSpare), actualBs);
     }

     @Test
     public void passSlideOnePositionsUp() {
          int col = 1;
          Tile expectedSpare = testGrid7x7Expected[0][col];

          testGrid7x7Expected[0][col] = testGrid7x7Expected[1][col];
          testGrid7x7Expected[1][col] = testGrid7x7Expected[2][col];
          testGrid7x7Expected[2][col] = testGrid7x7Expected[3][col];
          testGrid7x7Expected[3][col] = testGrid7x7Expected[4][col];
          testGrid7x7Expected[4][col] = testGrid7x7Expected[5][col];
          testGrid7x7Expected[5][col] = testGrid7x7Expected[6][col];
          testGrid7x7Expected[6][col] = bZTile;

          Board expectedBoard = new Board(testGrid7x7Expected);
          BoardAndSpare actualBs = testBoard7x7.slideAndInsert(bZTile, col, Direction.UP);
          assertEquals(new BoardAndSpare(expectedBoard, expectedSpare), actualBs);
     }

     @Test
     public void passSlideAndInsertUpAtIndex4() {
          Tile[][] grid = boardAllMinusBar.getGridDeepCopy();
          grid[2][4] = new Tile(rubyJasper, new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY));
          grid[3][4] = new Tile(jasperZoisite, new Connector(Unicode.BAR, Orientation.ZERO));
          grid[3][5] = new Tile(zirconRuby, new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY));

          BoardAndSpare actualBs = new Board(grid).slideAndInsert(new Tile(zirconZoisite, new Connector(Unicode.BAR,
                  Orientation.ZERO)), 4, Direction.UP);
          assertEquals(new Tile(rubyJasper, new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY)),
                  actualBs.getBoard().getGridDeepCopy()[1][4]);
          assertEquals(new Tile(jasperZoisite, new Connector(Unicode.BAR, Orientation.ZERO)),
                  actualBs.getBoard().getGridDeepCopy()[2][4]);
          assertEquals(new Tile(zirconRuby, new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY)),
                  actualBs.getBoard().getGridDeepCopy()[3][5]);
          assertEquals(new Tile(zirconZoisite, new Connector(Unicode.BAR, Orientation.ZERO)),
                  actualBs.getBoard().getGridDeepCopy()[6][4]);
     }
     //endregion

     //region getReachable()

     @Test
     public void passGetReachableTilesFromVertBarToFirstCornerPair() {
          Set<Position> expectedReachableTiles = new HashSet<>();
          expectedReachableTiles.add(new Position(0, 0));
          expectedReachableTiles.add(new Position(2,0));
          expectedReachableTiles.add(new Position(1,0));

          Set<Position> actualReachableTiles =
                  testBoardVertBar.getReachableTiles(1, 0);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the vertical test first corner coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromVertBarToSecondCornerPair() {
          Set<Position> expectedReachableTiles = new HashSet<>();
          expectedReachableTiles.add(new Position(3, 0));
          expectedReachableTiles.add(new Position(5,0));
          expectedReachableTiles.add(new Position(4,0));

          Set<Position> actualReachableTiles =
                  testBoardVertBar.getReachableTiles(4, 0);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the vertical test second corner coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromVertBarToTandPlus() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(6, 0), new Position(8, 0), new Position(9, 0),
                          new Position(10, 0), new Position(11, 0), new Position(12, 0),
                          new Position(13, 0), new Position(14, 0), new Position(7, 0)));
          Set<Position> actualReachableTiles =
                  testBoardVertBar.getReachableTiles(7, 0);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the vertical test with the t-shapes and plus failed.");
     }

     @Test
     public void passGetReachableTilesCorner180FirstCoverage() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(1, 0), new Position(2, 0), new Position(2, 1),
                          new Position(3, 0), new Position(3, 1), new Position(3, 2), new Position(3, 3),
                          new Position(2, 3), new Position(0, 0)));
          Set<Position> actualReachableTiles =
                  testBoardCorner180.getReachableTiles(0, 0);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the corner 180 test first connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesCorner180SecondCoverage() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(0, 2), new Position(1, 3), new Position(0, 3)));
          Set<Position> actualReachableTiles =
                  testBoardCorner180.getReachableTiles(0, 3);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the corner 180 test second connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesCorner180ThirdCoverage() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(1, 1), new Position(2, 2), new Position(1, 2)));
          Set<Position> actualReachableTiles =
                  testBoardCorner180.getReachableTiles(1, 2);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the corner 180 test third connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesCorner270FirstCoverage() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(0, 1), new Position(1, 0), new Position(0, 0)));
          Set<Position> actualReachableTiles =
                  testBoardCorner270.getReachableTiles(0, 0);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the corner 270 test first connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesCorner270SecondCoverage() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(2, 1), new Position(1, 2), new Position (1, 1)));
          Set<Position> actualReachableTiles =
                  testBoardCorner270.getReachableTiles(1, 1);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the corner 270 test second connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesCorner270ThirdCoverage() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(2, 3), new Position(3, 2), new Position(3, 0),
                          new Position(3, 1), new Position(3, 3), new Position(2, 2)));
          Set<Position> actualReachableTiles =
                  testBoardCorner270.getReachableTiles(2, 2);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the corner 270 test third connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesTShape0FirstCoverage() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(0, 0), new Position(0, 2), new Position(0, 3),
                          new Position(1, 1), new Position(1, 2), new Position(1, 3), new Position(0,1)));
          Set<Position> actualReachableTiles =
                  testBoardTShape0.getReachableTiles(0, 1);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the t-shape 0 test first connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesTShape180() {
          Set<Position> expectedReachableTiles =
                  new HashSet<>(Arrays.asList(new Position(0, 0), new Position(0, 1), new Position(1, 0), new Position(1, 1)));
          Set<Position> actualReachableTiles =
                  testBoardTShape180.getReachableTiles(1, 1);
          assertEquals(expectedReachableTiles, actualReachableTiles,
                  "The getReachableTiles for the t-shape 180 test first connector coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromHorizBarFirstCoverage() {
          Set<Position> expected = new HashSet<>();
          expected.add(new Position(0, 1));
          expected.add(new Position(0, 0));
          Set<Position> actual = testBoardHorizBar.getReachableTiles(0, 0);
          assertEquals(expected, actual, "The getReachableTiles for the horizontal test first coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromHorizBarSecondCoverage() {
          Set<Position> expected = new HashSet<>();
          expected.add(new Position(0, 2));
          expected.add(new Position(0, 3));
          Set<Position> actual = testBoardHorizBar.getReachableTiles(0, 3);
          assertEquals(expected, actual, "The getReachableTiles for the horizontal test second coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromHorizBarThirdCoverage() {
          Set<Position> expected = new HashSet<>();
          expected.add(new Position(0, 4));
          expected.add(new Position(0, 6));
          expected.add(new Position(0, 5));

          Set<Position> actual = testBoardHorizBar.getReachableTiles(0, 5);
          assertEquals(expected, actual, "The getReachableTiles for the horizontal test third coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromHorizBarFifthCoverage() {
          Set<Position> expected = new HashSet<>();
          expected.add(new Position(0, 8));
          expected.add(new Position(0, 7));
          Set<Position> actual = testBoardHorizBar.getReachableTiles(0, 7);
          assertEquals(expected, actual, "The getReachableTiles for the horizontal test fifth coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromHorizBarSixthCoverage() {
          Set<Position> expected = new HashSet<>();
          expected.add(new Position(0, 9));
          expected.add(new Position(0, 11));
          expected.add(new Position(0, 12));
          expected.add(new Position(0, 13));
          expected.add(new Position(0, 10));

          Set<Position> actual = testBoardHorizBar.getReachableTiles(0, 10);
          assertEquals(expected, actual, "The getReachableTiles for the horizontal test sixth coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromHorizBarSeventhCoverage() {
          Set<Position> expected = new HashSet<>();
          expected.add(new Position(0, 15));
          expected.add(new Position(0, 14));

          Set<Position> actual = testBoardHorizBar.getReachableTiles(0, 14);
          assertEquals(expected, actual, "The getReachableTiles for the horizontal test seventh coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromCorner0SecondCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(2, 0), new Position(2, 1),
                  new Position(2, 2), new Position(3,1), new Position(3, 2), new Position(3, 3),
                  new Position(3, 0)));

          Set<Position> actual = testBoardCorner0.getReachableTiles(3, 0);
          assertEquals(expected, actual, "The getReachableTiles for the corner 0 test second coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromCorner90FirstCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(0, 0), new Position(0, 1),
                  new Position(0, 2), new Position(1, 0), new Position(1, 1)));

          Set<Position> actual = testBoardCorner90.getReachableTiles(1, 1);
          assertEquals(expected, actual, "The getReachableTiles for the corner 90 test first coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromCorner90SecondCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(1, 2), new Position(2, 1),
                  new Position(2, 0), new Position(2, 2)));

          Set<Position> actual = testBoardCorner90.getReachableTiles(2, 2);
          assertEquals(expected, actual, "The getReachableTiles for the corner 90 test first coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromCorner90ThirdCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(3, 0), new Position(3, 1),
                  new Position(3, 1)));

          Set<Position> actual = testBoardCorner90.getReachableTiles(3, 1);
          assertEquals(expected, actual, "The getReachableTiles for the corner 90 test third coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromTShape90FirstCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(0, 0), new Position(1, 1),
                  new Position(2, 0), new Position(0, 1), new Position(2, 1), new Position(1, 0)));

          Set<Position> actual = testBoardTShape90.getReachableTiles(1, 0);
          assertEquals(expected, actual, "The getReachableTiles for the t-shape 90 test first coverage failed.");
     }

     @Test
     public void passGetReachableTilesFromTShape270FirstCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(0, 0), new Position(0, 1)));

          Set<Position> actual = testBoardTShape270.getReachableTiles(0, 1);
          assertEquals(expected, actual, "The getReachableTiles for the t-shape 270 test first coverage failed.");
     }

     @Test
     public void passGetReachableTilesDiffDirectionsFirstCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(0, 1),
              new Position(0, 2), new Position(1, 0), new Position(1, 1), new Position(1, 2),
              new Position(1, 3), new Position(0, 3), new Position(0, 4), new Position(1, 4),
                  new Position(0, 0)));

          Set<Position> actual = testBoardDiffDirections.getReachableTiles(0, 0);
          assertEquals(expected, actual, "The getReachableTiles for the different directions test first coverage failed.");
     }

     @Test
     public void passGetReachableTilesDiffDirectionsSecondCoverage() {
          Set<Position> expected = new HashSet<>(Arrays.asList(new Position(0, 1),
              new Position(0, 2), new Position(1, 0), new Position(1, 1), new Position(1, 2),
              new Position(1, 3), new Position(0, 3),
              new Position(0, 4), new Position(0, 0), new Position(1, 4)));

          Set<Position> actual = testBoardDiffDirections.getReachableTiles(1, 4);
          assertEquals(expected, actual, "The getReachableTiles for the different directions test second coverage failed.");
     }

     //endregion

     //region Board equals()

     @Test
     public void passBoardEqualsUsingGridDeepCopy() {
          Tile[][] grid = boardAllMinusBar.getGridDeepCopy();
          Tile[][] gridExpected = boardAllMinusBar.getGridDeepCopy();
          assertEquals(new Board(gridExpected), new Board(grid));
     }

     //endregion

     //region isReachable()
     @Test
     public void falseIsReachable() {
          assertFalse(testBoardDiffDirections.isReachable(new Position(1,4), new Position(3,3)));
     }

     @Test
     public void trueIsReachable() {
          assertTrue(testBoardDiffDirections.isReachable(new Position(1,4), new Position(0,1)));
     }

     @Test
     public void trueItselfIsReachable() {
          assertTrue(testBoardDiffDirections.isReachable(new Position(1,4), new Position(1,4)));
     }

     @Test
     public void false2IsReachable() {
          Tile[][] boardAllMinusBarGrid = BoardTest.createBoard(7, 7, BoardTest.generateTiles(BoardTest.generateGemPairs(49),
                  BoardTest.get49Bar90Connectors(), 49)).getGridDeepCopy();
          boardAllMinusBarGrid[1][4] = new Tile(rubyJasper, new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY));
          boardAllMinusBarGrid[2][4] = new Tile(jasperZoisite, new Connector(Unicode.BAR, Orientation.ZERO));
          boardAllMinusBarGrid[3][5] = new Tile(zirconRuby, new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY));
          assertFalse(new Board(boardAllMinusBarGrid).isReachable(new Position(2, 4), new Position(3, 5)));
     }

     @Test
     public void passIsReachableTest() {
          Tile[][] grid = boardAllMinusBar.getGridDeepCopy();
          grid[2][4] = new Tile(rubyJasper, new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY));
          grid[3][4] = new Tile(jasperZoisite, new Connector(Unicode.BAR, Orientation.ZERO));
          grid[3][5] = new Tile(zirconRuby, new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY));
          assertFalse(new Board(grid).isReachable(new Position(3, 4), new Position(3, 5)));
     }

     @Test
     public void isReachableTest() {
          Tile[][] grid = testBoard7x7.getGridDeepCopy();
          grid[0][0] = grid[0][1];
          grid[0][1] = grid[0][2];
          grid[0][2] = grid[0][3];
          grid[0][3] = grid[0][4];
          grid[0][4] = grid[0][5];
          grid[0][5] = grid[0][6];
          grid[0][6] = new Tile(jasperZoisite, new Connector(Unicode.BAR, Orientation.ZERO));
          assertFalse(new Board(grid).isReachable(new Position(1, 2), new Position(0, 3)));
     }
     //endregion

     //region getAllGemPairs()
     @Test
     public void passGetAllGemPairs() {
          assertEquals(new HashSet<>(generateGemPairs(49)), BoardTest.generateBoard(1, 7, 7).getAllGemPairs(),
                  "getAllGemPairs() failed");
     }

     //endregion

     //region getGridDeepCopy()

     @Test
     public void passArrayEquals() {
          Tile[] tile1 = new Tile[]{bZTile};
          Tile[] tile2 = new Tile[]{bZTile};
          assertArrayEquals(tile1, tile2);
     }
     @Test
     public void passGetGridDeepCopy() {
          assertArrayEquals(testGrid7x7Expected, testBoard7x7.getGridDeepCopy());
     }
     //endregion

     //region BoardAndSpare
     @Test
     public void boardAndSpareEquals() {
          assertEquals(new BoardAndSpare(testBoard7x7Expected, new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO))),
              new BoardAndSpare(testBoard7x7, bZTile));
     }

     @Test
     public void boardAndSpareHashCode() {
          assertEquals(new BoardAndSpare(testBoard7x7Expected, new Tile(berylZircon,
                  new Connector(Unicode.CORNER, Orientation.ZERO))).hashCode(),
              new BoardAndSpare(testBoard7x7, bZTile).hashCode());
     }
     //endregion

     //region getMoveable()
     @Test
     public void passGetMoveable() {
          List<Pair<Direction[], Integer>> moveableExpected= new ArrayList<>();

          moveableExpected.add(new Pair(true, 0));
          moveableExpected.add(new Pair(true, 2));
          moveableExpected.add(new Pair(true, 4));
          moveableExpected.add(new Pair(true, 6));
          moveableExpected.add(new Pair(false, 0));
          moveableExpected.add(new Pair(false, 2));
          moveableExpected.add(new Pair(false, 4));
          moveableExpected.add(new Pair(false, 6));

          assertEquals(moveableExpected, testBoard7x7.getMoveable());
     }
     //endregion

     //region getFixedPositions()
     @Test
     public void passGetFixedPositions() {
          List<Position> expected = new ArrayList<>();
          for (int row = 0; row < testBoard7x7.getNumRows(); row += 1) {
               for (int col = 0; col < testBoard7x7.getNumColumns(); col += 1) {
                    if (row % 2 != 0 && col % 2 != 0) {
                         expected.add(new Position(row, col));
                    }
               }
          }
          assertEquals(expected, testBoard7x7.getFixedPositions());
     }
     //endregion
}
