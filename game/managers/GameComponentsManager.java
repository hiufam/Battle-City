package managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.HashMap;

import enums.GameComponentType;
import utils.CommonUtil;
import classes.GameComponent;
import common.Vector2D;

public class GameComponentsManager {
  private static ArrayList<GameComponent> gameComponents = new ArrayList<>();
  private static HashMap<GameComponentType, ArrayList<GameComponent>> gameCollisionComponents = new HashMap<>();

  public void GameComponent() {
  }

  /*
   * This stupid thing might cause an error
   */
  public static void remove(GameComponent gameComponent) {
    if (gameComponent == null) {
      return;
    }

    for (Iterator<GameComponent> iterator = gameComponents.iterator(); iterator.hasNext();) {
      GameComponent value = iterator.next();
      if (value != gameComponent) {
        continue;
      }

      if (value.getCollision() != null) {
        value.setCollision(null);
      }
      iterator.remove();
    }
  }

  public static boolean gameComponentExist(GameComponent gameComponent) {
    return gameComponents.indexOf(gameComponent) != -1;
  }

  public static void replaceGameComponent(GameComponent replacedComponent, GameComponent gameComponent) {
    if (replacedComponent == null || gameComponent == null) {
      return;
    }

    int replacedComponentIndex = gameComponents.indexOf(replacedComponent);

    if (replacedComponentIndex == -1) {
      return;
    }

    gameComponents.set(replacedComponentIndex, gameComponent);
  }

  public static void add(GameComponent gameComponent) {
    if (gameComponent != null) {
      gameComponents.add(gameComponent);
    }
  }

  public static ArrayList<GameComponent> getGameComponents() {
    return gameComponents;
  }

  public static ArrayList<GameComponent> getGameComponents(GameComponentType type) {
    return new ArrayList<>(Arrays.asList(gameComponents
        .stream()
        .filter(gameComponent -> gameComponent.getType() != null)
        .filter(gameComponent -> gameComponent.getType().equals(type))
        .toArray(GameComponent[]::new)));
  }

  public static GameComponent getGameComponent(Vector2D position) {
    GameComponent[] gameComponentMap = GameScreen.getGameComponentMap();

    if (gameComponentMap == null) {
      return null;
    }

    GameComponent gameComponent = gameComponentMap[CommonUtil.getTileIndex(position)];

    return gameComponent;
  }

  /**
   * Setting what type of GameComponent which the source GameComponent will
   * collide with
   * 
   * @param sourceComponentType - a type of source GameComponent
   * @param gameComponentTypes  - type of GameComponent that the source collide
   *                            with
   */
  public static void setCollisionComponents(
      GameComponentType sourceComponentType,
      GameComponentType[] gameComponentTypes) {
    if (gameCollisionComponents.get(sourceComponentType) == null) {
      gameCollisionComponents.put(sourceComponentType, new ArrayList<GameComponent>());
    }

    for (GameComponentType gameComponentType : gameComponentTypes) {
      for (GameComponent gameComponent : gameComponents) {
        if (gameComponent.getType() == gameComponentType) {
          gameCollisionComponents.get(sourceComponentType).add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameComponent> getCollisionComponents(GameComponentType sourceComponentType) {
    return gameCollisionComponents.get(sourceComponentType);
  }
}
