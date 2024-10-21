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

  /**
   * Get the out of bound offset which how much a GameComponent is inside of bound
   * 
   * @param gameComponent - a GameComponent
   * @return - a Vector2D which includes how many pixels left/right/up/down needed
   *         for adjustment to be inside of bound
   */
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

  /**
   * Check if a GameComponent is out of bound
   * 
   * @param gameComponent - a GameComponent.
   * @return true is the GameComponent is out of bound, and false otherwise.
   */
  public static boolean isOutOfBound(GameComponent gameComponent) {
    return !getOutOfBoundOffset(gameComponent).isEqual(Vector2D.zero());
  }

  /**
   * Check if position is solid give array of map which includes the position of
   * GameComponent(s)
   * 
   * @param position - the position of a GameComponent.
   * @param tileMap  - array of GameComponent(s).
   * @return true if current position already has another GameComponent, and false
   *         otherwise.
   */
  public static boolean isPositionSolid(Vector2D position, GameComponent[] tileMap) {
    int column = (int) (position.x / GameScreen.TILE_WIDTH);
    int row = (int) (position.y / GameScreen.TILE_HEIGHT);

    if (column >= GameScreen.WINDOW_WIDTH / GameScreen.TILE_WIDTH)
      return true;

    if (row >= GameScreen.WINDOW_HEIGHT / GameScreen.TILE_HEIGHT)
      return true;

    int tileMapIndex = CommonUtil.getTileIndex(position);

    boolean tileIsSolid = false;
    GameComponent gameComponent = tileMap[tileMapIndex];

    if (gameComponent != null) {
      tileIsSolid = true;
    }

    return tileIsSolid;
  }

  /**
   * Check if current position is overlapping another GameComponent.
   * 
   * @param position - Vector2D position.
   * @param map      - array of GameComponent(s).
   * @return GameComponent if current position is overlapping another
   *         GameComponent, and null otherwise.
   */
  public static GameComponent isPositionOverlapGameComponent(Vector2D position, GameComponent[] map) {
    GameComponent overlappedGameComponent = map[CommonUtil.getTileIndex(position)];

    if (overlappedGameComponent != null) {
      return overlappedGameComponent;
    }

    return null;
  }

  /**
   * Check if collision box is overlapping another GameComponent
   * 
   * @param collisionBox - GameComponent CollisionBox
   * @return GameComponent if collision box FULLY overlap another GameComponent,
   *         and null otherwise
   */
  public static GameComponent isCollisionBoxOverlapGameComponent(CollisionBox collisionBox) {
    GameComponent overlappedComponent1 = CollisionUtil.isPositionOverlapGameComponent(
        collisionBox.center,
        GameScreen.getGameComponentMap());
    GameComponent overlappedComponent2 = CollisionUtil.isPositionOverlapGameComponent(
        collisionBox.globalPosition,
        GameScreen.getGameComponentMap());
    GameComponent overlappedComponent3 = CollisionUtil.isPositionOverlapGameComponent(
        collisionBox.globalPosition.add(new Vector2D(collisionBox.width, collisionBox.height)),
        GameScreen.getGameComponentMap());

    if (overlappedComponent1 != null &&
        overlappedComponent2 != null &&
        overlappedComponent3 != null &&
        overlappedComponent1.equals(overlappedComponent2) &&
        overlappedComponent1.equals(overlappedComponent3)) {
      return overlappedComponent1;
    }

    return null;
  }

  public static void checkEdgeCollision(GameComponent gameComponentA) {
    if (isOutOfBound(gameComponentA)) {
      Vector2D boundOffset = getOutOfBoundOffset(gameComponentA);
      gameComponentA.setPosition(gameComponentA.getPosition().add(boundOffset));
    }
  }

  /**
   * Check for intersection
   * 
   * @param gameComponentA - a GameComponent.
   * @param gameComponentB - a GameComponent.
   * @param deltaTime      - time between frames.
   * @return true if intersection happened and false otherwise.
   */
  public static boolean checkAABBIntersection(
      GameComponent gameComponentA,
      GameComponent gameComponentB,
      double deltaTime) {

    AABB collBoxA_AABB = gameComponentA.getCollision().getAABB();
    AABB collBoxB_AABB = gameComponentB.getCollision().getAABB();
    AABB MinkowskiDiff = collBoxB_AABB.minkowskiDifference(collBoxA_AABB);

    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {
      return true;
    }

    return false;
  }

  /**
   * Check for simple AABB and swept AABB colision.
   * 
   * Ref:
   * https://blog.hamaluik.ca/posts/swept-aabb-collision-using-minkowski-difference/
   * https://developer.mozilla.org/en-US/docs/Games/Techniques/3D_collision_detection
   * 
   * Note:
   * - Workable but some bugs may still occur
   * 
   * @param gameComponentA - a GameComponent.
   * @param gameComponentB - a GameComponent.
   * @param deltaTime      - time between frames.
   * @return true if AABB collision happened and false otherwise.
   */
  public static boolean checkAABBCollision(
      GameComponent gameComponentA,
      GameComponent gameComponentB,
      double deltaTime) {

    if (gameComponentA.getCollision() == null ||
        gameComponentB.getCollision() == null) {
      return false;
    }

    AABB collBoxA_AABB = gameComponentA.getCollision().getAABB();
    AABB collBoxB_AABB = gameComponentB.getCollision().getAABB();

    AABB MinkowskiDiff = collBoxB_AABB.minkowskiDifference(collBoxA_AABB);

    if (MinkowskiDiff.min.x <= 0 &&
        MinkowskiDiff.max.x >= 0 &&
        MinkowskiDiff.min.y <= 0 &&
        MinkowskiDiff.max.y >= 0) {
      checkStaticCollision(gameComponentA, gameComponentB, deltaTime);

      if (!gameComponentA.getVelocity().isZero()) {
        checkRigidCollision(gameComponentA, gameComponentB, deltaTime);
      }

      if (!gameComponentB.getVelocity().isZero()) {
        checkRigidCollision(gameComponentB, gameComponentA, deltaTime);
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

  /**
   * Check the collision of a moving GameComponent by detecting which component
   * is in front of it and change its position based on its velocity.
   * 
   * @param source    - a GameComponent which is used as origin to detect
   *                  collision with other GameComponent(s).
   * @param target    - a GameComponent which is checked by source GameComponent
   *                  for collision.
   * @param deltaTime - time between frames.
   * @return true if rigid collision happened and false otherwise.
   */
  private static boolean checkRigidCollision(GameComponent source, GameComponent target, double deltaTime) {
    boolean hasCollision = false;

    if (!source.getCollision().isFrontCollisionChecked()) {
      return true;
    }

    // Check every single point to see if GameComponent has collided in the front
    for (Vector2D collisionPoint : forwardCollisionPoints(source, 5, deltaTime)) {
      AABB targetAABB = target.getCollision().getAABB();
      double pointToAABBFraction = targetAABB.getRayIntersectionFraction(collisionPoint, source.getVelocity());

      if (pointToAABBFraction < Double.POSITIVE_INFINITY) {
        hasCollision = true;
      }
    }

    // Adjust collision based on velocity
    if (target.getCollision().isEnabled() && hasCollision) {
      Vector2D offset = source.getVelocity().normalized().multiply(-1);
      source.setPosition(source.getPosition().add(offset));
    }

    return hasCollision;
  }

  /**
   * Check the collision of a static GameComponent by calculating the overlapping
   * area between 2 AABB(s) and change source GameComponent by the offset.
   * 
   * @param source - a GameComponent which is used as origin to detect
   *               collision with other GameComponent(s) and then be offset.
   * @param target - a GameComponent which is checked by source GameComponent
   *               for collision.
   * @return true if static collision happened and false otherwise.
   */
  private static boolean checkStaticCollision(GameComponent source, GameComponent target, double deltaTime) {
    AABB sourceAABB = source.getCollision().getAABB();
    AABB targetAABB = target.getCollision().getAABB();

    AABB MinkowskiDiff = targetAABB.minkowskiDifference(sourceAABB);

    // Calculation the offset
    double offsetX = Math.min(Math.abs(MinkowskiDiff.min.x), Math.abs(MinkowskiDiff.max.x));
    double offsetY = Math.min(Math.abs(MinkowskiDiff.min.y), Math.abs(MinkowskiDiff.max.y));

    // Apply direction to offset
    Vector2D totalOffset = sourceAABB.getOverlappingOffsetDirection(targetAABB, offsetX, offsetY);

    source.setPosition(source.getPosition().add(totalOffset));

    return true;
  }

  /**
   * Generate collision points which are placed in front of a moving GameComponent
   * for collision detection.
   * 
   * @param gameComponent        - a GameComponent
   * @param collisionPointsCount - number of collision points for detection, more
   *                             points equal more computation
   * @param deltaTime            - time between frames
   * @return
   */
  private static ArrayList<Vector2D> forwardCollisionPoints(
      GameComponent gameComponent,
      int collisionPointsCount,
      double deltaTime) {
    ArrayList<Vector2D> collisionPoints = new ArrayList<>();
    CollisionBox collisionBox = gameComponent.getCollision();

    if (collisionBox == null) {
      return new ArrayList<>();
    }

    Vector2D direction = gameComponent.getVelocity().normalized();
    Vector2D startingPoint = collisionBox.globalPosition;

    // GameComponent is moving horizontally
    if (direction.x != 0 && !Double.isNaN(direction.x)) {
      Vector2D startingPointH = startingPoint;

      if (direction.x > 0) {
        startingPointH = startingPointH.add(new Vector2D(collisionBox.width, 0));
      }

      collisionPoints.add(startingPointH);

      for (int i = 1; i < collisionPointsCount; i++) {
        double collisionPointY = ((double) collisionBox.height / (collisionPointsCount - 1)) * i;
        Vector2D collisionPointPosition = new Vector2D(0, collisionPointY);

        collisionPoints.add(startingPointH.add(collisionPointPosition));
      }
    }

    // GameComponent is moving vertically
    if (direction.y != 0 && !Double.isNaN(direction.y)) {
      Vector2D startingPointV = startingPoint;

      if (direction.y > 0) {
        startingPointV = startingPointV.add(new Vector2D(0, collisionBox.height));
      }

      collisionPoints.add(startingPointV);

      for (int i = 1; i < collisionPointsCount; i++) {
        double collisionPointX = ((double) collisionBox.width / (collisionPointsCount - 1)) * i;
        Vector2D collisionPointPosition = new Vector2D(collisionPointX, 0);

        collisionPoints.add(startingPointV.add(collisionPointPosition));
      }
    }

    return collisionPoints;
  }
}
