public enum Direction {
     UP,
     DOWN,
     LEFT,
     RIGHT;

     private static final int DEGREES_IN_SINGLE_ROTATION = 90;

     public static Direction getOpposite(Direction direction) {
          if (direction == Direction.UP) {
               return Direction.DOWN;
          } else if (direction == Direction.DOWN) {
               return Direction.UP;
          } else if (direction == Direction.LEFT) {
               return Direction.RIGHT;
          } else {
               return Direction.LEFT;
          }
     }

     /**
      * Is this a valid degree to rotate by?
      * @param degrees   the number of degrees to rotate
      * @return          whether or not this is a valid degree to rotate by
      */
     public static boolean isValidDegreeToRotateBy(int degrees) {
          return degrees % DEGREES_IN_SINGLE_ROTATION == 0;
     }

     /**
      * Is this direction moving in a horizontal direction?
      * @param d direction to move
      * @return  whether or not this direction is horizontal
      */
     public static boolean isHorizontalDirection(Direction d) {
          return (d == Direction.LEFT) || (d == Direction.RIGHT);
     }

     /**
      * Get the Direction from the given string
      * @return     the direction
      */
     public static Direction getDirectionFromString(String str) {
          switch(str) {
               case "LEFT": return Direction.LEFT;
               case "RIGHT": return Direction.RIGHT;
               case "UP": return Direction.UP;
               case "DOWN": return Direction.DOWN;
               default: throw new IllegalArgumentException("The string to convert to direction was invalid");
          }
     }
}