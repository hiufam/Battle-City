package components;

import java.awt.Rectangle;

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
  public boolean enabled = true;

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

  public String toString() {
    return "CollisionBox(x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ")";
  }
}
