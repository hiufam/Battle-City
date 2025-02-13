package components;

import common.AABB;
import common.Vector2D;
import classes.GameComponent;

public class CollisionBox {
  public double x;
  public double y;
  public int width;
  public int height;

  public Vector2D center;
  public Vector2D extents;
  public Vector2D relativePosition; // relative to game component position
  public Vector2D globalPosition;
  public GameComponent gameComponent;

  private boolean enabled = true;
  private boolean enableFrontCollisionCheck = false;
  private boolean enableCollisionResponse = true;

  public CollisionBox(GameComponent gameComponent, Vector2D relativePosition, int width, int height) {
    x = relativePosition.add(gameComponent.getPosition()).x;
    y = relativePosition.add(gameComponent.getPosition()).y;

    this.width = width;
    this.height = height;

    this.center = new Vector2D(x + width / 2, y + height / 2);
    this.extents = new Vector2D(width / 2, height / 2);
    this.relativePosition = relativePosition;
    this.globalPosition = gameComponent.getPosition().add(relativePosition);
    this.gameComponent = gameComponent;
  }

  public Vector2D getCenter() {
    return this.center;
  }

  public void setPosition(Vector2D gameComponentPosition) {
    globalPosition = gameComponentPosition.add(relativePosition);
    this.x = globalPosition.x;
    this.y = globalPosition.y;
    this.center = new Vector2D(x + (double) width / 2, y + (double) height / 2);
  }

  /**
   * Enable collision box to check what is in front of it
   * 
   * @param enable : true if allow, and false otherwise
   */
  public void setEnableFrontCollisionCheck(boolean enable) {
    enableFrontCollisionCheck = enable;
  }

  public boolean isFrontCollisionChecked() {
    return enableFrontCollisionCheck;
  }

  public void setEnabled(boolean enable) {
    this.enabled = enable;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnableCollisionResponse(boolean enable) {
    enableCollisionResponse = enable;
  }

  public boolean isCollisionResponseEnabled() {
    return enableCollisionResponse;
  }

  public AABB getAABB() {
    return new AABB(this);
  }

  public String toString() {
    return "CollisionBox(x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ")";
  }
}
