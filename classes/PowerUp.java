package classes;

import common.Vector2D;
import player.Player;

public class PowerUp {
  private boolean enabled = false;
  private boolean visibility = false;
  public int points = 500;
  public Vector2D position;
  public float duration;
  private Player player;

  public PowerUp(boolean enabled, boolean visibility, int points, Vector2D position, float duration) {
    this.enabled = enabled;
    this.visibility = visibility;
    this.points = points;
    this.position = position;
    this.duration = duration;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public void setVisibility(boolean visibility) {
    this.visibility = visibility;
  }

  public boolean getVisibility() {
    return this.visibility;
  }

  public void onPickedUp(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return this.player;
  }
}
