//import javax.swing.ImageIcon;

import java.util.HashMap;
import java.util.Map;
public enum Gem {
     ALEXANDRITE_PS("alexandrite-pear-shape"),
     ALEXANDRITE("alexandrite"),
     ALMANDINE("almandine-garnet"),
     AMETHYST("amethyst"),
     AMETRINE("ametrine"),
     AMMOLITE("ammolite"),
     APATITE("apatite"),
     APLITE("aplite"),
     APRICOT_SR("apricot-square-radiant"),
     AQUAMARINE("aquamarine"),
     AUSTRALIAN_M("australian-marquise"),
     AVENTURINE("aventurine"),
     AZURITE("azurite"),
     BERYL("beryl"),
     BLACK_OBS("black-obsidian"),
     BLACK_ONYX("black-onyx"),
     BLACK_SPINAL_C("black-spinel-cushion"),
     BLUE_CS("blue-ceylon-sapphire"),
     BLUE_C("blue-cushion"),
     BLUE_PS("blue-pear-shape"),
     BLUE_SH("blue-spinel-heart"),
     BULLS_E("bulls-eye"),
     CARNELIAN("carnelian"),
     CHROME_D("chrome-diopside"),
     CHRYSOBERYL_C("chrysoberyl-cushion"),
     CHRYSOLITE("chrysolite"),
     CITRINE_C("citrine-checkerboard"),
     CITRINE("citrine"),
     CLINOHUMITE("clinohumite"),
     COLOR_CO("color-change-oval"),
     CORDIERITE("cordierite"),
     DIAMOND("diamond"),
     DUMORTIERITE("dumortierite"),
     EMERALD("emerald"),
     FANCY_SM("fancy-spinel-marquise"),
     GARNET("garnet"),
     GOLDEN_DC("golden-diamond-cut"),
     GOLDSTONE("goldstone"),
     GRANDIDIERITE("grandidierite"),
     GRAY_A("gray-agate"),
     GREEN_A("green-aventurine"),
     GREEN_BA("green-beryl-antique"),
     GREEN_B("green-beryl"),
     GREEN_PC("green-princess-cut"),
     GROSSULAR_G("grossular-garnet"),
     HACKMANITE("hackmanite"),
     HELIOTROPE("heliotrope"),
     HEMATITE("hematite"),
     IOLITE_EC("iolite-emerald-cut"),
     JASPER("jasper"),
     JASPILITE("jaspilite"),
     KUNZITE_O("kunzite-oval"),
     KUNZITE("kunzite"),
     LABRADORITE("labradorite"),
     LAPIS_L("lapis-lazuli"),
     LEMON_QB("lemon-quartz-briolette"),
     MAGNESITE("magnesite"),
     MEXICAN_O("mexican-opal"),
     MOONSTONE("moonstone"),
     MORGANITE_O("morganite-oval"),
     MOSS_A("moss-agate"),
     ORANGE_R("orange-radiant"),
     PADPARADSCHA_OVAL("padparadscha-oval"),
     PADPARADSCHA_SAPPHIRE("padparadscha-sapphire"),
     PERIDOT("peridot"),
     PINK_EC("pink-emerald-cut"),
     PINK_OPAL("pink-opal"),
     PINK_ROUND("pink-round"),
     PINK_SC("pink-spinel-cushion"),
     PRASIOLITE("prasiolite"),
     PREHNITE("prehnite"),
     PURPLE_CABOCHON("purple-cabochon"),
     PURPLE_OVAL("purple-oval"),
     PURPLE_ST("purple-spinel-trillion"),
     PURPLE_SC("purple-square-cushion"),
     RAW_BERYL("raw-beryl"),
     RAW_CITRINE("raw-citrine"),
     RED_DIAMOND("red-diamond"),
     RED_SPINAL_SEC("red-spinel-square-emerald-cut"),
     RHODONITE("rhodonite"),
     ROCK_QUARTZ("rock-quartz"),
     ROSE_QUARTZ("rose-quartz"),
     RUBY_DP("ruby-diamond-profile"),
     RUBY("ruby"),
     SPHALERITE("sphalerite"),
     SPINEL("spinel"),
     STAR_CABOCHON("star-cabochon"),
     STILBITE("stilbite"),
     SUNSTONE("sunstone"),
     SUPER_SEVEN("super-seven"),
     TANZANITE_TRILLION("tanzanite-trillion"),
     TIGERS_EYE("tigers-eye"),
     TOURMALINE("tourmaline"),
     TOURMALINE_LC("tourmaline-laser-cut"),
     UNAKITE("unakite"),
     WHITE_SQUARE("white-square"),
     YELLOW_BAGUETTE("yellow-baguette"),
     YELLOW_BO("yellow-beryl-oval"),
     YELLOW_HEART("yellow-heart"),
     YELLOW_JASPER("yellow-jasper"),
     ZIRCON("zircon"),
     ZOISITE("zoisite");

     private final String name;
     private static final Map<String, Gem> map;

     static {
          map = new HashMap<String, Gem>();
          for (Gem g : Gem.values()) {
               map.put(g.name, g);
          }
     }

     // private final ImageIcon image,

//     Gem(String name, ImageIcon image) {
     private Gem(String name) {
          this.name = name;
          // this.image = image,
     }

     public static Gem getGem(String name) {
          if (!map.containsKey(name)) {
               throw new IllegalArgumentException("Gem does not exist " + name);
          }
          return map.get(name);
     }

     public static boolean isGem(String name) {
          return map.containsKey(name);
     }

     @Override
     public String toString() {
          return this.name;
     }

}