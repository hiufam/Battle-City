package environments;

import java.awt.Graphics2D;

import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;

import classes.GameComponent;
import classes.GameSprite;

public class SteelBlock extends GameComponent {
  public SteelBlock(Vector2D position, int width, int height) {
    super(GameComponentType.STEEL, position, width, height);

    // collisionBox = new CollisionBox(this, new Vector2D(0, 0), width, height);
    sprite = new GameSprite("images\\wall_steel.png");
  }

  public void draw(Graphics2D graphics2d) {
    graphics2d.drawImage(sprite.getBufferedImage(), x, y, this);
  }
}
