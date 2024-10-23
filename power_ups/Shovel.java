package power_ups;

import java.awt.Color;
import java.awt.Graphics2D;

import classes.PowerUp;
import common.Vector2D;
import environments.BrickBlock;
import environments.SteelBlock;
import managers.GameComponentsManager;

public class Shovel extends PowerUp {
  private final Vector2D[] BASE_BRICKS_POSITIONS = {
      new Vector2D(16 * 13, 16 * 24),
      new Vector2D(16 * 14, 16 * 24),
      new Vector2D(16 * 15, 16 * 24),
      new Vector2D(16 * 16, 16 * 24),
      new Vector2D(16 * 13, 16 * 25),
      new Vector2D(16 * 16, 16 * 25),
      new Vector2D(16 * 13, 16 * 26),
      new Vector2D(16 * 16, 16 * 26),
  };

  private BrickBlock[] baseBrickBlocks = new BrickBlock[BASE_BRICKS_POSITIONS.length];

  public Shovel() {
    getBrickBlocks();
  }

  @Override
  public void draw(Graphics2D graphics2d) {
    if (visible && enabled) {
      graphics2d.setColor(Color.BLUE);
      graphics2d.fillRect((int) position.x, (int) position.y, 32, 32);
    }
  }

  @Override
  public void update(double deltaTime) {
    if (visible && enabled) {
      getBrickBlocks();
    }

    if (player != null && checkIntersection(player, deltaTime)) {
      turnBrickIntoSteel();

      visible = false;
      enabled = false;
      position = DEFAULT_POSITION;
    }
  }

  private void getBrickBlocks() {
    for (int i = 0; i < BASE_BRICKS_POSITIONS.length; i++) {
      BrickBlock BrickBlock = (BrickBlock) GameComponentsManager.getGameComponent(BASE_BRICKS_POSITIONS[i]);
      if (BrickBlock != null) {
        baseBrickBlocks[i] = BrickBlock;
      }
    }
  }

  private void turnBrickIntoSteel() {
    for (BrickBlock brickBlock : baseBrickBlocks) {
      if (brickBlock == null) {
        continue;
      }

      if (GameComponentsManager.gameComponentExist(brickBlock)) {
        GameComponentsManager.replaceGameComponent(brickBlock, new SteelBlock(brickBlock.getPosition(), 32, 32));
      }
    }
  }
}
