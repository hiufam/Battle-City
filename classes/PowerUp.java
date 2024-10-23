package classes;

import common.Vector2D;
import components.CollisionBox;
import player.Player;

public class PowerUp extends GameComponent {
  public final int points = 500;

  protected static Player player = null;
  protected boolean enabled = false;
  protected boolean visible = false;

  protected static final Vector2D DEFAULT_POSITION = new Vector2D(-360, -360);

  public PowerUp() {
    setPosition(DEFAULT_POSITION);
    setCollision(new CollisionBox(this, new Vector2D(0, 0), 32, 32));

    collisionBox.setEnableCollisionResponse(false);
  }

  public void setEnabled(boolean enable) {
    this.enabled = enable;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public boolean isVisible() {
    return this.visible;
  }

  public static void setPlayer(Player newPlayer) {
    player = newPlayer;
  }
}
