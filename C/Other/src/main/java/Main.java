import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;

class Main {

     public static void main(String[] args) {
          try {
               // The ith index in the jsonFields list corresponds to the ith index of the characters list
               List<Pair<String, String>> jsonFields = getJsonFields();
               List<Character> characters = getCharacters();

               new C(jsonFields, characters, System.in, System.out).start();
          } catch (Exception e) {
               System.out.println("There was an error");
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