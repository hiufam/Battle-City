package environments;

import java.awt.Graphics2D;

import classes.GameComponent;
import common.Vector2D;
import enums.GameComponentType;
import managers.GameComponentsManager;

public class BrickWall extends GameComponent {
  private BrickBlock[] bricks = new BrickBlock[4];

  public BrickWall(Vector2D position, int width, int height) {
    super(GameComponentType.BRICK, position, width, height);

    for (int i = 0; i < bricks.length; i++) {
      int row = (int) Math.floor(i / 2);
      int col = i % 2;

      Vector2D brickPosition = new Vector2D(row * width / 2, col * height / 2);

      bricks[i] = new BrickBlock(position.add(brickPosition), width / 2, height / 2);
    }
  }

  public void draw(Graphics2D graphics2d) {
    for (BrickBlock brick : bricks) {
      if (brick == null || brick.destroyed) {
        continue;
      }
      brick.draw(graphics2d);
    }
  }

  public void update(double deltaTime) {
  }

  public void move(Vector2D velocity) {
    setPosition(getPosition().add(velocity));
  }

  public void destroy() {
    setCollision(null); // can disable collision box instead
    GameComponentsManager.remove(this);
  }
}
