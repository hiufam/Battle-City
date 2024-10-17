package managers;

import java.util.ArrayList;
import java.util.Iterator;

import enums.GameComponentType;
import classes.GameComponent;

public class GameComponentsManager {
  private static ArrayList<GameComponent> gameComponents = new ArrayList<>();
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
      if (value == gameComponent) {
        iterator.remove();
      }
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

  public static GameComponent[] getGameComponent(GameComponentType type) {
    return gameComponents
        .stream()
        .filter(gameComponent -> gameComponent.getType().equals(type))
        .toArray(GameComponent[]::new);
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
