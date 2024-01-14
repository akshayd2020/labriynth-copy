import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Main {
     public static void main(String[] args) {
          try {
               // The ith index in the jsonFields list corresponds to the ith index of the characters list
               List<Pair<String, String>> jsonFields = getJsonFields();
               List<Character> characters = getCharacters();

               EServer server = new EServer(Integer.parseInt(args[0]), jsonFields, characters);
               server.start();
          } catch (Exception e) { // narrow down what you catch
               System.out.println("Encounter an error");
               System.out.println(e.toString());
          }
     }

     private static List<Pair<String, String>> getJsonFields() {
          List<Pair<String, String>> jsonFields = new ArrayList<>();
          jsonFields.add(new Pair("LEFT", "UP"));
          jsonFields.add(new Pair("LEFT", "DOWN"));
          jsonFields.add(new Pair("RIGHT", "UP"));
          jsonFields.add(new Pair("RIGHT", "DOWN"));

          return jsonFields;
     }

     private static List<Character> getCharacters() {
          List<Character> characters = new ArrayList<>();
          characters.add('\u2518');
          characters.add('\u2510');
          characters.add('\u2514');
          characters.add('\u250C');

          return characters;
     }
}