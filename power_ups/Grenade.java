package power_ups;

import classes.PowerUp;
import common.Vector2D;

public class Grenade extends PowerUp {
  public int damage = 100;

  public Grenade(boolean enabled, boolean visibility, int points, Vector2D position, float duration) {
    super(enabled, visibility, points, position, duration);
  }

}
