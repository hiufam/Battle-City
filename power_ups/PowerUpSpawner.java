package power_ups;

import java.util.ArrayList;

import classes.GameComponent;
import classes.PowerUp;
import common.Vector2D;
import enums.GameComponentType;
import managers.GameComponentsManager;
import player.Player;
import utils.CommonUtil;

public class PowerUpSpawner extends GameComponent {
  final private ArrayList<PowerUp> powerUps = new ArrayList<>();

  private PowerUp currentPowerUp = null;
  private Player player = null;

  private double powerUpSpawnInterval = 1;
  private double powerUpSpawnTimer = 0;

  public PowerUpSpawner() {
    powerUps.add(new Grenade());
    powerUps.add(new Shovel());
  }

  @Override
  public void update(double deltaTime) {
    if (player == null) {
      player = (Player) GameComponentsManager.getGameComponents(GameComponentType.PLAYER).get(0);
      PowerUp.setPlayer(player);
    }

    if (currentPowerUp != null && !currentPowerUp.isVisible() && !currentPowerUp.isEnabled()) {
      currentPowerUp = null;
      powerUpSpawnTimer = 0;
    }

    if (currentPowerUp == null && powerUpSpawnTimer > powerUpSpawnInterval) {
      PowerUp powerUp = selectRandomPowerUp();

      currentPowerUp = powerUp;

      currentPowerUp.setEnabled(true);
      currentPowerUp.setVisible(true);

      // TODO: set random position
      currentPowerUp.setPosition(new Vector2D(32 * 5, 32 * 10));
    }

    if (currentPowerUp == null) {
      powerUpSpawnTimer += deltaTime;
    }
  }

  private PowerUp selectRandomPowerUp() {
    int randomIndex = CommonUtil.randomInteger(0, powerUps.size() - 1);

    return powerUps.get(randomIndex);
  }
}
