import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwingGridView extends JFrame {

     private JPanel grid = new JPanel();
     private final int TILE_SIZE = 110;
     private final int LINE_WIDTH = 5;
     private final int LINE_LENGTH = 50;

     private List<List<Character>> inputGrid;

     public SwingGridView(List<List<Character>> ig) {
          super();
          this.inputGrid = ig;
          this.grid.setLayout(new BoxLayout(grid, BoxLayout.PAGE_AXIS));
          renderGrid();
          handleMouseClick();
          pack();

          this.setVisible(true);
     }

     private void renderGrid() {
          HashMap<Character, List<Integer>> unicodeToCoordinates = returnUnicodeHashMap();
          int rowCount = unicodeToCoordinates.size();
          int columnCount = 0;
          for (List<Character> row : this.inputGrid) {
            JPanel imageRow = new JPanel();
            imageRow.setLayout(new BoxLayout(imageRow, BoxLayout.LINE_AXIS));
            columnCount = row.size();
            for (Character cell : row) {
                List<Integer> coordinates = unicodeToCoordinates.get(cell);
                JPanel newTile = getTile(coordinates.get(0), coordinates.get(1), coordinates.get(2),
                    coordinates.get(3));
                imageRow.add(newTile);
            }
            grid.add(imageRow);
          }
          this.add(grid);
          this.grid.setLayout(new BoxLayout(grid, BoxLayout.PAGE_AXIS));
     }

     private void handleMouseClick() {
          this.grid.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                    int[] coordinates = new int[] {e.getX(), e.getY()};
                    System.out.println(Arrays.toString(coordinates));
                    System.exit(0);
               }
          });
     }

     private HashMap<Character, List<Integer>> returnUnicodeHashMap() {
          HashMap<Character, List<Integer>> unicodeToCoordinates = new HashMap<>();

          Integer[] leftUp = new Integer[] {10, 55, 55, 5};
          Integer[] leftDown = new Integer[] {10, 55, 55, 55};
          Integer[] rightUp = new Integer[] {55, 55, 55, 5};
          Integer[] rightDown = new Integer[] {55, 55, 55, 55};

          unicodeToCoordinates.put('\u2518', new ArrayList<>(Arrays.asList(leftUp)));
          unicodeToCoordinates.put('\u2510', new ArrayList<>(Arrays.asList(leftDown)));
          unicodeToCoordinates.put('\u2514', new ArrayList<>(Arrays.asList(rightUp)));
          unicodeToCoordinates.put('\u250C', new ArrayList<>(Arrays.asList(rightDown)));

          return unicodeToCoordinates;
    }

     private JPanel getTile(int hX, int hY, int vX, int vY) {

          JPanel tile = new JPanel();
          tile.setBackground(Color.yellow);
          tile.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
          tile.setLayout(null);

          JPanel horizontal = new JPanel();
          horizontal.setBackground(Color.red);
          horizontal.setBounds(hX, hY, LINE_LENGTH, LINE_WIDTH);
          JPanel vertical = new JPanel();
          vertical.setBounds(vX, vY, LINE_WIDTH, LINE_LENGTH);
          vertical.setBackground(Color.red);

          tile.add(horizontal);
          tile.add(vertical);
          return tile;
     }
}