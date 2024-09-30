package power_ups;

import java.util.ArrayList;

import classes.Environment;
import classes.PowerUp;
import common.Vector2D;
import environments.BrickWall;

public class Shovel extends PowerUp {
  ArrayList<BrickWall> brickWalls = new ArrayList<>();

  public Shovel(boolean enabled, boolean visibility, int points, Vector2D position, float duration,
      ArrayList<BrickWall> brickWalls) {
    super(enabled, visibility, points, position, duration);

    this.brickWalls = brickWalls;
  }

  public void setBrickWalls(ArrayList<BrickWall> brickWalls) {
    this.brickWalls = brickWalls;
  }

  public ArrayList<BrickWall> getBrickwalls() {
    return this.brickWalls;
  }

  public void convertWall(Class<Environment> className) {
    for (BrickWall brickWall : brickWalls) {
      className.cast(brickWall);

      brickWalls.remove(brickWall);
    }
  }
}
