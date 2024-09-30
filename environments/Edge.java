package environments;

import java.awt.Graphics2D;

import common.Vector2D;
import enums.GameComponentType;

import classes.GameComponent;
import classes.GameSprite;

public class Edge extends GameComponent {
  public Edge(Vector2D position, int width, int height) {
    super(GameComponentType.EDGE, position, width, height);

    sprite = new GameSprite("images\\edge.png");
  }

  public void draw(Graphics2D graphics2d) {
    graphics2d.drawImage(sprite.getBufferedImage(), x, y, this);
  }
}
