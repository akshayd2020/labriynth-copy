import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CTests {

     private List<Pair<String, String>> jsons;
     private List<Character> characters;
     private Map<Pair<String, String>, Character> jsonToUnicode;
     C testC;

     public CTests() {
          this.jsons = new ArrayList<>();
          this.jsons.add(new Pair("LEFT", "UP"));
          this.jsons.add(new Pair("LEFT", "DOWN"));
          this.jsons.add(new Pair("RIGHT", "UP"));
          this.jsons.add(new Pair("RIGHT", "DOWN"));

          this.characters = new ArrayList<>();
          characters.add('\u2518');
          characters.add('\u2510');
          characters.add('\u2514');
          characters.add('\u250C');

          this.jsonToUnicode = new HashMap<>();
          this.jsonToUnicode.put(new Pair("LEFT", "UP"), '\u2518');
          this.jsonToUnicode.put(new Pair("LEFT", "DOWN"), '\u2510');
          this.jsonToUnicode.put(new Pair("RIGHT", "UP"), '\u2514');
          this.jsonToUnicode.put(new Pair("RIGHT", "DOWN"), '\u250C');
     }

     @Before
     public void setupTestFixture() {
          testC = new C(this.jsons, this.characters, System.in, System.out);
     }

     @Test
     public void getJsonToUnicode() {
          assertEquals(this.jsonToUnicode, C.getJsonToUnicode(this.jsons, this.characters));
     }
}