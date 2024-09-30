package power_ups;

import classes.PowerUp;
import common.Vector2D;
import player.Player;

public class Helmet extends PowerUp {
  public Helmet(boolean enabled, boolean visibility, int points, Vector2D position, float duration) {
    super(enabled, visibility, points, position, duration);
  }

  public void onUse() {
    Player player = super.getPlayer();

    if (player != null) {
      player.setImmunity(true);
    }
  }

  public void onExpire() {
    Player player = super.getPlayer();

    if (player != null) {
      player.setImmunity(false);
    }
  }
}
