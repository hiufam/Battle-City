package components;

import java.awt.Rectangle;

import common.Vector2D;
import classes.GameComponent;

public class CollisionBox extends Rectangle {
  public Vector2D center;
  public Vector2D extents;
  public Vector2D relativePosition; // relative to game component position
  public Vector2D globalPosition;
  public GameComponent gameComponent;
  public boolean enabled = true;

  public CollisionBox(GameComponent gameComponent, Vector2D relativePosition, int width, int height) {
    super((int) relativePosition.add(gameComponent.getPosition()).x,
        (int) relativePosition.add(gameComponent.getPosition()).y, width, height);

    this.center = new Vector2D(x + width / 2, y + height / 2);
    this.extents = new Vector2D(width / 2, height / 2);
    this.relativePosition = relativePosition;
    this.globalPosition = gameComponent.getPosition().add(relativePosition);
    this.gameComponent = gameComponent;
  }

  public void setPosition(Vector2D gameComponentPosition) {
    globalPosition = gameComponentPosition.add(relativePosition);

    this.center = new Vector2D(globalPosition.x + width / 2, globalPosition.y + height / 2);
    super.setLocation((int) globalPosition.x, (int) globalPosition.y);
  }
}
