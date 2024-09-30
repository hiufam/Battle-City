package environments;

import java.awt.Graphics2D;

import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import interfaces.DestructibleComponent;
import managers.GameComponentsManager;
import classes.GameComponent;
import classes.GameSprite;

public class Box extends GameComponent implements DestructibleComponent {
  public int health = 4;

  public Box(Vector2D position, int width, int height) {
    super(GameComponentType.BOX, position, width, height);

    collisionBox = new CollisionBox(this, new Vector2D(0, 0), width, height);
    sprite = new GameSprite("images\\crate.png");
  }

  public void draw(Graphics2D graphics2d) {
    graphics2d.drawImage(sprite.getBufferedImage(), x, y, this);
  }

  public void update(double deltaTime) {
    if (health <= 0) {
      destroy();
    }
  }

  public void move(Vector2D velocity) {
    setPosition(getPosition().add(velocity));
  }

  public void destroy() {
    setCollision(null); // can disable collision box instead
    GameComponentsManager.remove(this);
  }

  @Override
  public void hit(int damage) {
    health -= damage;
  }
}