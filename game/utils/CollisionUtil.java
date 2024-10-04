package utils;

import java.util.ArrayList;

import common.AABB;
import common.Vector2D;
import components.CollisionBox;
import managers.GameScreen;
import classes.GameComponent;

public class CollisionUtil {
  private static final int MAX_WIDTH = GameScreen.GAME_SCREEN_WIDTH + GameScreen.WINDOW_WIDTH_SHIFT;
  private static final int MAX_HEIGHT = GameScreen.GAME_SCREEN_HEIGHT + GameScreen.WINDOW_HEIGHT_SHIFT;

  public static int getTileIndex(Vector2D position) {
    int column = (int) (position.x / GameScreen.TILE_WIDTH);
    int row = (int) (position.y / GameScreen.TILE_HEIGHT);

    return column + row * GameScreen.TILE_WIDTH;
  }

  public static Vector2D getPositionByIndex(int index, int tileWidth, int tileHeight) {
    int column = (int) index % tileWidth;
    int row = (int) index / tileHeight;

    return new Vector2D(column * tileWidth, row * tileHeight);
  }

  public static Vector2D getOutOfBoundOffset(GameComponent gameComponent) {
    double dx1 = gameComponent.getCenter().x + gameComponent.width / 2 - MAX_WIDTH;
    double dx2 = GameScreen.WINDOW_WIDTH_SHIFT - (gameComponent.getCenter().x - gameComponent.width / 2);

    if (dx1 > 0 || dx2 > 0) {
      if (dx1 > 0) {
        return new Vector2D(-dx1, 0);
      }
      if (dx2 > 0) {
        return new Vector2D(dx2, 0);
      }
    }

    double dy1 = gameComponent.getCenter().y + gameComponent.height / 2 - MAX_HEIGHT;
    double dy2 = GameScreen.WINDOW_HEIGHT_SHIFT - (gameComponent.getCenter().y - gameComponent.height / 2);

    if (dy1 > 0 || dy2 > 0) {
      if (dy1 > 0) {
        return new Vector2D(0, -dy1);
      }
      if (dy2 > 0) {
        return new Vector2D(0, dy2);
      }
    }

    return Vector2D.zero();
  }

  public static boolean isOutOfBound(GameComponent gameComponent) {
    return !getOutOfBoundOffset(gameComponent).isEqual(Vector2D.zero());
  }

  public static boolean isWorldPositionSolid(Vector2D position, GameComponent[] tileMap) {
    int column = (int) (position.x / GameScreen.TILE_WIDTH);
    int row = (int) (position.y / GameScreen.TILE_HEIGHT);

    if (column >= GameScreen.WINDOW_WIDTH / GameScreen.TILE_WIDTH)
      return true;

    if (row >= GameScreen.WINDOW_HEIGHT / GameScreen.TILE_HEIGHT)
      return true;

    int tileMapIndex = getTileIndex(position);

    boolean tileIsSolid = false;
    GameComponent gameComponent = tileMap[tileMapIndex];

    if (gameComponent != null) {
      tileIsSolid = true;
    }

    return tileIsSolid;
  }

  public static void checkEdgeCollision(GameComponent gameComponentA) {
    if (isOutOfBound(gameComponentA)) {
      Vector2D boundOffset = getOutOfBoundOffset(gameComponentA);
      gameComponentA.setPosition(gameComponentA.getPosition().add(boundOffset));
    }
  }

  public static boolean checkAABBCollision(GameComponent gameComponentA, GameComponent gameComponentB,
      double deltaTime) {

    if (gameComponentB.getCollision() == null) {
      return false;
    }

    AABB collBoxA_AABB = new AABB(gameComponentA.getCollision());
    AABB collBoxB_AABB = new AABB(gameComponentB.getCollision());

    AABB MinkowskiDiff = collBoxA_AABB.minkowskiDifference(collBoxB_AABB);

    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {

      double offsetX = MinkowskiDiff.center.x / MinkowskiDiff.size.x;
      double offsetY = MinkowskiDiff.center.y / MinkowskiDiff.size.y;

      // Stop the game component from SLIDING
      if (!gameComponentA.getVelocity().isNaN()) {
        Vector2D direction = gameComponentA.getVelocity().normalized();
        offsetX *= Math.abs(direction.x);
        offsetY *= Math.abs(direction.y);
      }

      Vector2D offset = new Vector2D(offsetX, offsetY);

      gameComponentA.setPosition(gameComponentA.getPosition().add(offset.multiply(deltaTime)));

      return true;
    } else {
      Vector2D relativeMotion = (gameComponentA.getVelocity().minus(gameComponentB.getVelocity())).multiply(deltaTime);

      Double h = MinkowskiDiff.getRayIntersectionFraction(Vector2D.zero(), relativeMotion);

      if (h < Double.POSITIVE_INFINITY) {
        collBoxA_AABB.center = collBoxA_AABB.center.add(gameComponentA.getVelocity()).multiply(deltaTime).multiply(h);
        collBoxB_AABB.center = collBoxB_AABB.center.add(gameComponentB.getVelocity()).multiply(deltaTime).multiply(h);

        Vector2D tangent = relativeMotion.normalized().getTangent();
        gameComponentA.setVelocity(tangent.multiply(Vector2D.dotProduct(gameComponentA.getVelocity(), tangent)));
        gameComponentB.setVelocity(tangent.multiply(Vector2D.dotProduct(gameComponentB.getVelocity(), tangent)));
      } else {
        return false;
      }
    }
    return false;
  }

  public static ArrayList<Vector2D> checkTileMapCollision(GameComponent gameComponent, GameComponent[] tileMap,
      double deltaTime) {
    ArrayList<Vector2D> collisionPoints = new ArrayList<>();

    CollisionBox collisionBox = gameComponent.getCollision();

    if (collisionBox == null) {

    }

    Vector2D direction = gameComponent.getVelocity().normalized();
    Vector2D startingPoint = collisionBox.globalPosition;

    if (direction.x != 0 && !Double.isNaN(direction.x)) { // Moving horizontally
      Vector2D startingPointH = startingPoint.add(new Vector2D(collisionBox.width / 2, 0).multiply(direction.x + 1));

      collisionPoints.add(startingPointH);

      for (int i = 1; i <= 4; i++) {
        collisionPoints.add(startingPointH.add(new Vector2D(0, 8 * i)));
      }
    }
    if (direction.y != 0 && !Double.isNaN(direction.y)) { // Moving vertically

      Vector2D startingPointV = startingPoint.add(new Vector2D(0, collisionBox.height / 2).multiply(direction.y + 1));

      collisionPoints.add(startingPointV);

      for (int i = 1; i <= 4; i++) {
        collisionPoints.add(startingPointV.add(new Vector2D(8 * i, 0)));
      }
    }

    gameComponent.update(deltaTime);
    return collisionPoints;
  }
}
