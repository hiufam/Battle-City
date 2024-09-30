
import java.awt.Color;
import java.util.ArrayList;

import common.Vector2D;
import enums.GameComponentType;

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
  GameComponentsManager gameComponentsManager;

  ArrayList<GameComponent> playerCollisionComponents = new ArrayList<>();
  ArrayList<GameComponent> bulletCollisionComponents = new ArrayList<>();
  GameComponent[] tileMap = new GameComponent[GameScreen.TILE_WIDTH * GameScreen.TILE_HEIGHT];

  public GameManager() {
    gameScreen = new GameScreen();
    gameKeyListener = new GameKeyManager();
    gameComponentsManager = new GameComponentsManager();
  }

  Player player = new Player(new Vector2D(GameScreen.WINDOW_WIDTH / 2, GameScreen.WINDOW_HEIGHT / 2), 32, 32,
      Color.WHITE);

  public void startGame() {
    GameScreen.createWindow(gameScreen, gameKeyListener);
    GameScreen.createGameLayout(ScreenLayout.getInstance(), MapLayout.generateLevel());
    GameScreen.setGameComponents(GameComponentsManager.getGameComponents());

    // TODO: A little crude might change later not generic enough
    GameComponentsManager.setPlayerCollisionComponents(new GameComponentType[] {
        GameComponentType.BOX,
        GameComponentType.BRICK,
        GameComponentType.STEEL,
    });
    GameComponentsManager.setBulletCollisionComponents(new GameComponentType[] {
        GameComponentType.BOX,
        GameComponentType.BRICK,
        GameComponentType.STEEL,
    });

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

    MovementUtil.tileMapPositionAssist(player, tileMap);
  }
}
