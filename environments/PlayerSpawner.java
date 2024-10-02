package environments;

import classes.GameComponent;
import common.Vector2D;
import enums.GameComponentType;

public class PlayerSpawner extends GameComponent {

  public PlayerSpawner(Vector2D position, int width, int height) {
    super(GameComponentType.PLAYER_SPAWNER, position, width, height);
  }
}
