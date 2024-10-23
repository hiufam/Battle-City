package power_ups;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import classes.GameComponent;
import classes.PowerUp;
import enums.GameComponentType;
import managers.GameComponentsManager;

public class Grenade extends PowerUp {
  @Override
  public void draw(Graphics2D graphics2d) {
    if (visible && enabled) {
      graphics2d.setColor(Color.RED);
      graphics2d.fillRect((int) position.x, (int) position.y, 32, 32);
    }
  }

  @Override
  public void update(double deltaTime) {
    if (player != null && checkIntersection(player, deltaTime)) {
      destroyAllEnemyTanks();

      visible = false;
      enabled = false;
      position = DEFAULT_POSITION;
    }
  }

  private void destroyAllEnemyTanks() {
    ArrayList<GameComponent> enemyTanks = GameComponentsManager.getGameComponents(GameComponentType.ENEMY);

    for (GameComponent enemyTank : enemyTanks) {
      if (enemyTank != null) {
        enemyTank.destroy();
      }
    }
  }
}
