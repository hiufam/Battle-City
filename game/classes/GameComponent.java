package classes;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import managers.GameComponentsManager;
import utils.CollisionUtil;

public abstract class GameComponent extends Component {
  public double x;
  public double y;
  public int width;
  public int height;

  protected GameComponentType type;
  protected Vector2D velocity;
  protected Vector2D center;
  protected Vector2D position;

  protected CollisionBox collisionBox;
  protected GameSprite sprite;

  public GameComponent(GameComponentType type, Vector2D position, int width, int height) {
    this.x = position.x;
    this.y = position.y;
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

  public void draw(Graphics2D graphics2d, double deltaTime) {
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
    this.x = position.x;
    this.y = position.y;

    setCenter(new Vector2D(position.x + width / 2, position.y + height / 2));

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

  public String toString() {
    return "GameComponent(type=" + type + ", Position=" + getPosition() + ", width=" + width + ", height=" + height
        + ")";
  }

  public boolean equals(GameComponent gameComponent) {
    if (gameComponent == this) {
      return true;
    }

    if (!(gameComponent instanceof GameComponent)) {
      return false;
    }

    return this.getType() == gameComponent.getType();
  }

  /**
   * Check collision of this GameComponent
   * 
   * @param gameComponents
   * @param ignoreGameComponentType - What type of GameComponent will this ignore
   * @param deltaTime
   * @return
   */
  public ArrayList<GameComponent> checkCollision(
      ArrayList<GameComponent> gameComponents,
      GameComponentType[] ignoreGameComponentType,
      double deltaTime) {
    if (!getCollision().isEnabled()) {
      return null;
    }
    CollisionUtil.checkEdgeCollision(this);

    ArrayList<GameComponent> collidedGameComponents = new ArrayList<>();

    for (GameComponent gameComponent : gameComponents) {
      if (gameComponent.getCollision() == null ||
          !gameComponent.getCollision().isEnabled() ||
          gameComponent == this) {
        continue;
      }

      if (ignoreGameComponentType != null && Arrays.asList(ignoreGameComponentType).contains(gameComponent.getType())) {
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
