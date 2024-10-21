package managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.HashMap;

import enums.GameComponentType;
import classes.GameComponent;

public class GameComponentsManager {
  private static ArrayList<GameComponent> gameComponents = new ArrayList<>();
  private static HashMap<GameComponentType, ArrayList<GameComponent>> gameCollisionComponents = new HashMap<>();
  private static ArrayList<GameComponent> playerCollisionComponents = new ArrayList<>();
  private static ArrayList<GameComponent> bulletCollisionComponents = new ArrayList<>();
  private static ArrayList<GameComponent> enemyCollisionComponents = new ArrayList<>();

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

  public static void add(GameComponent gameComponent) {
    if (gameComponent != null) {
      gameComponents.add(gameComponent);
    }
  }

  public static ArrayList<GameComponent> getGameComponents() {
    return gameComponents;
  }

  public static ArrayList<GameComponent> getGameComponent(GameComponentType type) {
    return new ArrayList<>(Arrays.asList(gameComponents
        .stream()
        .filter(gameComponent -> gameComponent.getType().equals(type))
        .toArray(GameComponent[]::new)));
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
    for (GameComponentType gameComponentType : gameComponentTypes) {
      for (GameComponent gameComponent : GameComponentsManager.getGameComponents()) {
        if (gameCollisionComponents.get(sourceComponentType) == null) {
          gameCollisionComponents.put(sourceComponentType, new ArrayList<GameComponent>());
        }
        if (gameComponent.getType() == gameComponentType) {
          gameCollisionComponents.get(sourceComponentType).add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameComponent> getCollisionComponents(GameComponentType sourceComponentType) {
    return gameCollisionComponents.get(sourceComponentType);
  }

  public static void setPlayerCollisionComponents(GameComponentType[] gameComponentTypes) {
    for (GameComponentType gameComponentType : gameComponentTypes) {
      for (GameComponent gameComponent : GameComponentsManager.getGameComponents()) {
        if (gameComponent.getType() == gameComponentType) {
          playerCollisionComponents.add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameComponent> getPlayerCollisionComponents() {
    return playerCollisionComponents;
  }

  public static void setBulletCollisionComponents(GameComponentType[] gameComponentTypes) {
    for (GameComponentType gameComponentType : gameComponentTypes) {
      for (GameComponent gameComponent : GameComponentsManager.getGameComponents()) {
        if (gameComponent.getType() == gameComponentType) {
          bulletCollisionComponents.add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameComponent> getBulletCollisionComponents() {
    return bulletCollisionComponents;
  }

  public static void setEnemyCollisionComponents(GameComponentType[] gameComponentTypes) {
    for (GameComponentType gameComponentType : gameComponentTypes) {
      for (GameComponent gameComponent : GameComponentsManager.getGameComponents()) {
        if (gameComponent.getType() == gameComponentType) {
          enemyCollisionComponents.add(gameComponent);
        }
      }
    }
  }

  public static ArrayList<GameComponent> getEnemyCollisionComponents() {
    return enemyCollisionComponents;
  }
}
