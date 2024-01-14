//import SwingGridView;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;

class D {
     public static void main(String[] args) {
          try {
               Reader inputStream = new InputStreamReader(System.in, "UTF-8");
               PrintStream outputStream = new PrintStream(System.out, true, "UTF-8");
               Scanner scanner = new Scanner(inputStream);
               StringBuilder inputStrBuilder = new StringBuilder();

               while (scanner.hasNext()) {
                    inputStrBuilder.append(scanner.next());
               }

               List<List<Character>> unicodeTiles = returnTiles(inputStrBuilder.toString());
               new SwingGridView(unicodeTiles);
          } catch (Exception e) {
               System.out.println("There was an error:\n" + e.toString());
          }
     }

     private static List<List<Character>> returnTiles(String input) {
          String[] rows = input.split("\"\"");
          cleanRows(rows);
          List<List<Character>> unicodeGrid = new ArrayList<>();
          for (String row : rows) {
               List<Character> unicodeRow = new ArrayList<>();
               for (int i = 0; i < row.length(); i += 1) {
                    unicodeRow.add(row.charAt(i));
               }
               unicodeGrid.add(unicodeRow);
          }
          return unicodeGrid;
     }

     // remove quotes from the first and last strings
     private static void cleanRows(String[] rows) {
          String firstRow = rows[0].substring(1);
          rows[0] = firstRow;

          int lastRowLength = rows[rows.length - 1].length();
          int lastRowIndex = rows.length - 1;
          String lastRow = rows[lastRowIndex].substring(0, lastRowLength - 1);
          rows[lastRowIndex] = lastRow;

     }
}