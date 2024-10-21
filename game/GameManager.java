
import java.awt.Color;
import java.util.ArrayList;

import common.Vector2D;
import enemy.Enemy;
import enums.GameComponentType;
import environments.BrickWall;
import environments.SteelBlock;
import layouts.MapLayout;
import layouts.ScreenLayout;
import managers.GameComponentsManager;
import managers.GameKeyManager;
import managers.GameLoop;
import managers.GameScreen;
import classes.GameComponent;
import utils.MovementUtil;
import player.Player;

public class GameManager extends GameLoop {
  GameScreen gameScreen;
  GameKeyManager gameKeyListener;
  Player player;

  ArrayList<GameComponent> playerCollisionComponents = new ArrayList<>();
  ArrayList<GameComponent> bulletCollisionComponents = new ArrayList<>();
  GameComponent[] tileMap = new GameComponent[GameScreen.TILE_WIDTH * GameScreen.TILE_HEIGHT];

  public GameManager() {
    gameScreen = new GameScreen();
    gameKeyListener = new GameKeyManager();
  }

  public void startGame() {
    GameScreen.createWindow(gameScreen, gameKeyListener);
    GameScreen.createGameLayout(ScreenLayout.getInstance(), MapLayout.generateLevel());

    new Enemy(new Vector2D(32, 16));
    new Enemy(new Vector2D(32 * 2, 16));
    new Enemy(new Vector2D(32 * 3, 16));
    new Enemy(new Vector2D(32 * 4, 16));
    new Enemy(new Vector2D(32 * 5, 16));
    new Enemy(new Vector2D(32 * 6, 16));
    new Enemy(new Vector2D(32 * 7, 16));
    new Enemy(new Vector2D(32 * 8, 16));
    new Enemy(new Vector2D(32 * 9, 16));
    new Enemy(new Vector2D(32 * 10, 16));
    player = new Player(GameComponentsManager.getGameComponent(GameComponentType.PLAYER_SPAWNER).get(0).getPosition());

    GameComponentsManager.setCollisionComponents(GameComponentType.PLAYER,
        new GameComponentType[] {
            GameComponentType.BOX,
            GameComponentType.BRICK,
            GameComponentType.STEEL,
            GameComponentType.ENEMY,
        });

    GameComponentsManager.setCollisionComponents(GameComponentType.BULLET,
        new GameComponentType[] {
            GameComponentType.BOX,
            GameComponentType.BRICK,
            GameComponentType.STEEL,
            GameComponentType.PLAYER_SPAWNER,
            GameComponentType.BULLET,
            GameComponentType.PLAYER,
            GameComponentType.ENEMY,
        });

    GameComponentsManager.setCollisionComponents(GameComponentType.ENEMY,
        new GameComponentType[] {
            GameComponentType.BOX,
            GameComponentType.BRICK,
            GameComponentType.STEEL,
            GameComponentType.PLAYER,
        });

    GameScreen.setGameComponents(GameComponentsManager.getGameComponents());
    run();
  }

  @Override
  protected void processGameLoop() {
    long initalFrameTime = System.nanoTime();
    double lastFrameTime = 0.0;

    while (isGameRunning()) {
      double currentFrameTime = (System.nanoTime() - initalFrameTime) / 1E9;
      double deltaTime = currentFrameTime - lastFrameTime;

      update(deltaTime);
      processInput();
      render();

      lastFrameTime = currentFrameTime;
    }
  }

  protected void render() {
    gameScreen.draw();
  }

  protected void update(double deltaTime) {
    try {
      for (GameComponent gameComponent : GameComponentsManager.getGameComponents()) {
        gameComponent.update(deltaTime);
      }
    } catch (Exception e) {
      // TODO: handle exception
      // System.out.println(e);
    }
  }

  protected void processInput() {
    if (gameKeyListener.shootPressed) {
      player.shoot(player.getPrevDirection());
    }
    if (gameKeyListener.rightPressed) {
      player.setDirection(new Vector2D(1, 0));
    }
    if (gameKeyListener.leftPressed) {
      player.setDirection(new Vector2D(-1, 0));
    }
    if (gameKeyListener.upPressed) {
      player.setDirection(new Vector2D(0, -1));
    }
    if (gameKeyListener.downPressed) {
      player.setDirection(new Vector2D(0, 1));
    }
    if (!gameKeyListener.rightPressed &&
        !gameKeyListener.leftPressed &&
        !gameKeyListener.upPressed &&
        !gameKeyListener.downPressed) {
      player.setDirection(new Vector2D(0, 0));
    }

    MovementUtil.tileMapPositionAssist(player);
  }
}
