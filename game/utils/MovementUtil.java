package utils;

import classes.GameComponent;
import common.Vector2D;
import enemy.Enemy;
import managers.GameScreen;
import player.Player;

public class MovementUtil {
  /**
   * Assist player to align themselves with tilemap position after changing
   * direction
   */
  public static void tileMapPositionAssist(Player player) {
    if (player.getPrevDirection().dotProduct(player.getDirection()) == 0 &&
        !player.getPrevDirection().isZero()
        && !player.getDirection().isZero()) {
      double newXC = (Math.ceil(player.getPosition().x / (GameScreen.TILE_WIDTH /
          2)) * (GameScreen.TILE_WIDTH / 2));
      double newYC = (Math.ceil(player.getPosition().y / (GameScreen.TILE_HEIGHT /
          2)) * (GameScreen.TILE_HEIGHT / 2));

      double newXF = (Math.floor(player.getPosition().x / (GameScreen.TILE_WIDTH /
          2)) * (GameScreen.TILE_WIDTH / 2));
      double newYF = (Math.floor(player.getPosition().y / (GameScreen.TILE_HEIGHT /
          2)) * (GameScreen.TILE_HEIGHT / 2));

      int newX;
      if (Math.abs(newXC - player.getPosition().x) < Math.abs(newXF -
          player.getPosition().x)) {
        newX = (int) newXC;
      } else {
        newX = (int) newXF;
      }

      int newY;
      if (Math.abs(newYC - player.getPosition().y) < Math.abs(newYF -
          player.getPosition().y)) {
        newY = (int) newYC;
      } else {
        newY = (int) newYF;
      }

      player.setPosition(new Vector2D(newX, newY));
    }
  }

  public static void tileMapPositionAssist(Enemy player) {
    if (player.getPrevDirection().dotProduct(player.getDirection()) == 0 && !player.getPrevDirection().isZero()
        && !player.getDirection().isZero()) {
      double newXC = (Math.ceil(player.getPosition().x / (GameScreen.TILE_WIDTH / 2)) * (GameScreen.TILE_WIDTH / 2));
      double newYC = (Math.ceil(player.getPosition().y / (GameScreen.TILE_HEIGHT / 2)) * (GameScreen.TILE_HEIGHT / 2));

      double newXF = (Math.floor(player.getPosition().x / (GameScreen.TILE_WIDTH / 2)) * (GameScreen.TILE_WIDTH / 2));
      double newYF = (Math.floor(player.getPosition().y / (GameScreen.TILE_HEIGHT / 2)) * (GameScreen.TILE_HEIGHT / 2));

      int newX;
      if (Math.abs(newXC - player.getPosition().x) < Math.abs(newXF -
          player.getPosition().x)) {
        newX = (int) newXC;
      } else {
        newX = (int) newXF;
      }

      int newY;
      if (Math.abs(newYC - player.getPosition().y) < Math.abs(newYF -
          player.getPosition().y)) {
        newY = (int) newYC;
      } else {
        newY = (int) newYF;
      }

      player.setPosition(new Vector2D(newX, newY));
    }
  }
}
