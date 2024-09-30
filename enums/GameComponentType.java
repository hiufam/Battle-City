package enums;

public enum GameComponentType {
  BRICK(1),
  STEEL(2),
  BASE(3),
  RIVER(4),
  TREE(5),
  EDGE(6),
  BOX(7),
  PLAYER(8),
  BULLET(9);

  // BLANK(0), BRICK(1), STEEL(2), BASE(3), RIVER(4), TREE(5), EDGE(6);

  private final int value;

  private GameComponentType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static GameComponentType getTypeFromInt(int value) {
    return GameComponentType.values()[value];
  }
}
