package environments;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import classes.GameComponent;
import classes.GameSprite;
import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import interfaces.DestructibleComponent;
import managers.GameComponentsManager;

public class BrickBlock extends GameComponent implements DestructibleComponent {
  public boolean destroyed = false;
  public int health = 2;

  private Vector2D damagedDirection;
  private BufferedImage maskImage;

  public BrickBlock(Vector2D position, int width, int height) {
    super(GameComponentType.BRICK, position, width, height);

    this.collisionBox = new CollisionBox(this, new Vector2D(0, 0), width, height);
    this.sprite = new GameSprite("images\\wall_brick.png");
  }

  @Override
  public void draw(Graphics2D graphics2d) {
    if (health > 1) {
      graphics2d.drawImage(sprite.getBufferedImage(), x, y, this);
    } else {
      drawDamagedBrick(graphics2d);
    }
  }

  @Override
  public void update(double deltaTime) {
    if (health <= 0) {
      destroy();
    }
  }

  @Override
  public void destroy() {
    maskImage = null;

    setCollision(null); // can disable collision box instead
    setSprite(null);

    GameComponentsManager.remove(this);
    destroyed = true;
  }

  @Override
  public void hit(int damage) {
    health -= damage;
  }

  public void hitComponent(GameComponent gameComponent) {
    damagedDirection = gameComponent.getVelocity();
  }

  private void drawDamagedBrick(Graphics2D graphics2d) {
    if (maskImage != null) {
      sprite.applyGrayscaleMaskToAlpha(maskImage);
      graphics2d.drawImage(sprite.getBufferedImage(), x, y, this);
      return;
    }

    if (damagedDirection == null) {
      return;
    }

    Vector2D damagedRelativeDirection = damagedDirection.multiply(-1);

    /*
     * Draw everything with just half width/height and
     * change position by half of width/height
     */
    if (damagedRelativeDirection.x > 0) { // right side dmg
      maskImage = GameSprite.createdGrayScaleMask(0, 0, width / 2, height, sprite);
    } else if (damagedRelativeDirection.x < 0) { // left side dmg
      maskImage = GameSprite.createdGrayScaleMask(width / 2, 0, width / 2, height, sprite);
    } else if (damagedRelativeDirection.y > 0) { // bottom side dmg
      maskImage = GameSprite.createdGrayScaleMask(0, 0, width, height / 2, sprite);
    } else if (damagedRelativeDirection.y < 0) { // top side dmg
      maskImage = GameSprite.createdGrayScaleMask(0, height / 2, width, height / 2, sprite);
    }

    damagedDirection = null;
  }
}
