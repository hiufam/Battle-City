package utils;

import java.util.ArrayList;

import common.AABB;
import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
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

  public static Vector2D getCollisionOffset(GameComponent gameComponentA, GameComponent gameComponentB) {

    return null;
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

  public static boolean checkAABBCollision(
      GameComponent gameComponentA,
      GameComponent gameComponentB,
      double deltaTime) {

    if (gameComponentB.getCollision() == null) {
      return false;
    }

    AABB collBoxA_AABB = new AABB(gameComponentA.getCollision());
    AABB collBoxB_AABB = new AABB(gameComponentB.getCollision());
    AABB MinkowskiDiff = collBoxB_AABB.minkowskiDifference(collBoxA_AABB);

    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {

      // Lazy calculation of offset
      double offsetX = Math.min(Math.abs(MinkowskiDiff.min.x), Math.abs(MinkowskiDiff.max.x));
      double offsetY = Math.min(Math.abs(MinkowskiDiff.min.y), Math.abs(MinkowskiDiff.max.y));

      if (!gameComponentA.getVelocity().isZero()) { // For rigid body (1 component has velocity)
        boolean hasCollision = false;

        // TODO: Fix this ASAP, need a case for no need to check front
        if (gameComponentA.getType() == GameComponentType.BULLET) {
          return true;
        }

        for (Vector2D collisionPoint : forwardCollisionPoints(gameComponentA,
            deltaTime)) {
          if (collBoxB_AABB.getRayIntersectionFraction(collisionPoint,
              gameComponentA.getVelocity()) < Double.POSITIVE_INFINITY) {
            hasCollision = true;
          }
        }

        if (hasCollision) {
          Vector2D offset = gameComponentA.getVelocity().normalized().multiply(-1);
          gameComponentA.setPosition(gameComponentA.getPosition().add(offset));
          gameComponentA.setVelocity(new Vector2D(0, 0));
        }
      } else { // For static body (2 components have zero velocity)
        Vector2D totalOffset = collBoxB_AABB.getOverlappingDirection(collBoxA_AABB,
            offsetX, offsetY);

        gameComponentB.setPosition(
            gameComponentB.getPosition().add(totalOffset));

        /*
         * Note: In some rare case, the offset will be messed up*
         */
      }

      if (!gameComponentB.getVelocity().isZero()) {
        boolean hasCollision = false;

        if (gameComponentB.getType() == GameComponentType.BULLET) {
          return true;
        }

        for (Vector2D collisionPoint : forwardCollisionPoints(gameComponentB,
            deltaTime)) {
          if (collBoxA_AABB.getRayIntersectionFraction(collisionPoint,
              gameComponentB.getVelocity()) < Double.POSITIVE_INFINITY) {
            hasCollision = true;
          }
        }

        if (hasCollision) {
          Vector2D offset = gameComponentB.getVelocity().normalized().multiply(-1);
          gameComponentB.setPosition(gameComponentB.getPosition().add(offset));
          gameComponentB.setVelocity(new Vector2D(0, 0));
        }
      } else {
        Vector2D totalOffset = collBoxA_AABB.getOverlappingDirection(collBoxB_AABB,
            offsetX, offsetY);

        gameComponentA.setPosition(
            gameComponentA.getPosition().add(totalOffset));
      }

      return true;
    } else {
      Vector2D relativeMotion = (gameComponentA.getVelocity().minus(gameComponentB.getVelocity())).multiply(deltaTime);

      Double h = MinkowskiDiff.getRayIntersectionFraction(Vector2D.zero(), relativeMotion);

      if (h < Double.POSITIVE_INFINITY) {
        Vector2D offsetA = gameComponentA.getVelocity().multiply(deltaTime).multiply(h);
        Vector2D offsetB = gameComponentB.getVelocity().multiply(deltaTime).multiply(h);
        gameComponentA.setPosition(gameComponentA.getPosition().add(offsetA));
        gameComponentB.setPosition(gameComponentB.getPosition().add(offsetB));

        return true;
      }
      return false;
    }
  }

  public static ArrayList<Vector2D> forwardCollisionPoints(GameComponent gameComponent, double deltaTime) {
    final int collisionPointsCount = 5;
    ArrayList<Vector2D> collisionPoints = new ArrayList<>();
    CollisionBox collisionBox = gameComponent.getCollision();

    if (collisionBox == null) {
      return new ArrayList<>();
    }

    Vector2D direction = gameComponent.getVelocity().normalized();
    Vector2D startingPoint = collisionBox.globalPosition;

    if (direction.x != 0 && !Double.isNaN(direction.x)) { // Moving horizontally
      Vector2D startingPointH = startingPoint;

      if (direction.x > 0) {
        startingPointH = startingPointH.add(new Vector2D(collisionBox.width, 0));
      }

      collisionPoints.add(startingPointH);
      for (int i = 1; i < collisionPointsCount; i++) {
        collisionPoints
            .add(startingPointH.add(new Vector2D(0, ((double) collisionBox.height / (collisionPointsCount - 1)) * i)));
      }
    }

    if (direction.y != 0 && !Double.isNaN(direction.y)) { // Moving vertically
      Vector2D startingPointV = startingPoint;

      if (direction.y > 0) {
        startingPointV = startingPointV.add(new Vector2D(0, collisionBox.height));
      }

      collisionPoints.add(startingPointV);

      for (int i = 1; i < collisionPointsCount; i++) {
        collisionPoints
            .add(startingPointV.add(new Vector2D(((double) collisionBox.width / (collisionPointsCount - 1)) * i, 0)));
      }
    }

    return collisionPoints;
  }
}
