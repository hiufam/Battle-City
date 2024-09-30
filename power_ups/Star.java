package power_ups;

import classes.PowerUp;
import classes.Tank;
import common.Vector2D;
import enums.StarTier;
import player.Player;
import tanks.PowerTank;

public class Star extends PowerUp {
  private StarTier tier;

  public Star(boolean enabled, boolean visibility, int points, Vector2D position, float duration) {
    super(enabled, visibility, points, position, duration);
  }

  public void onUse() {
    Player player = super.getPlayer();

    if (player != null) {

    }
  }

  public void onExpire() {
    Player player = super.getPlayer();

    if (player != null) {
      player.setImmunity(false);
    }
  }

  public void setTier(StarTier tier) {
    this.tier = tier;
  }

  public StarTier getTier() {
    return this.tier;
  }

  private void upgradeTank(StarTier tier, Tank tank) {
    switch (tier) {
      case DEFAULT:
        tank.setBulletSpeed(3);
        this.tier = StarTier.SECOND;
        break;

      case SECOND:
        tank.setBulletsPerAttack(2);
        this.tier = StarTier.THIRD;
        break;

      case THIRD:

      case FOURTH:
        break;
    }
  }
}
