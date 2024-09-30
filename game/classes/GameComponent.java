package classes;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;

import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import managers.GameComponentsManager;
import utils.CollisionUtil;

public abstract class GameComponent extends Component {
  public int x;
  public int y;
  public int width;
  public int height;

  private GameComponentType type;
  private Vector2D velocity;
  private Vector2D center;
  private Vector2D position;

  protected CollisionBox collisionBox;
  protected GameSprite sprite;

  public GameComponent(GameComponentType type, Vector2D position, int width, int height) {
    this.x = (int) position.x;
    this.y = (int) position.y;
    this.width = width;
    this.height = height;

    this.type = type;
    this.velocity = new Vector2D(0, 0);
    this.center = new Vector2D(x + width / 2, y + height / 2);
    this.position = new Vector2D(x, y);

    GameComponentsManager.add(this);
  }

  public void draw(Graphics2D graphics2d) {
  };

  public void update(double deltaTime) {
  };

  public void destroy() {
  };

  public Vector2D getPosition() {
    return position;
  }

  public void setPosition(Vector2D position) {
    this.position = position;
    this.x = (int) position.x;
    this.y = (int) position.y;

    setCenter(new Vector2D(x + width / 2, y + height / 2));

    if (collisionBox != null) {
      collisionBox.setPosition(position);
    }
  }

  public Vector2D getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2D velocity) {
    this.velocity = velocity;
  }

  public Vector2D getCenter() {
    return center;
  }

  public void setCenter(Vector2D center) {
    this.center = center;
  }

  public CollisionBox getCollision() {
    return this.collisionBox;
  }

  public void setCollision(CollisionBox collisionBox) {
    this.collisionBox = collisionBox;
  }

  public GameSprite getSprite() {
    return this.sprite;
  }

  public void setSprite(GameSprite sprite) {
    this.sprite = sprite;
  }

  public GameComponentType getType() {
    return type;
  }

  public ArrayList<GameComponent> checkCollision(ArrayList<GameComponent> gameComponents,
      double deltaTime) {
    if (!getCollision().enabled) {
      return null;
    }

    ArrayList<GameComponent> collidedGameComponents = new ArrayList<>();

    for (GameComponent gameComponent : gameComponents) {
      if (gameComponent.getCollision() == null || !gameComponent.getCollision().enabled) {
        continue;
      }

      boolean hasCollision = CollisionUtil.checkAABBCollision(this, gameComponent, deltaTime);
      if (hasCollision) {
        collidedGameComponents.add(gameComponent);
      }
    }

    if (collidedGameComponents.size() > 0) {
      return collidedGameComponents;
    }

    return null;
  }
}
