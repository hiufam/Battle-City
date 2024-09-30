package managers;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import classes.GameComponent;
import classes.GameLayout;

public class GameScreen extends JPanel {
  public static final int WINDOW_WIDTH = 32 * 16;
  public static final int WINDOW_HEIGHT = 32 * 14;
  public static final int GAME_SCREEN_WIDTH = 32 * 13;
  public static final int GAME_SCREEN_HEIGHT = 32 * 13;
  public static final int TILE_WIDTH = 32;
  public static final int TILE_HEIGHT = 32;
  public static final int BLOCK_WIDTH = 16;
  public static final int BLOCK_HEIGHT = 16;
  public static final int WINDOW_WIDTH_SHIFT = 32;
  public static final int WINDOW_HEIGHT_SHIFT = 16;

  private static ArrayList<GameComponent> gameComponents = new ArrayList<>();

  public GameScreen() {
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
  }

  public void draw() {
    this.repaint();
  }

  public static void setGameComponents(ArrayList<GameComponent> newGameComponents) {
    gameComponents = newGameComponents;
  }

  public Graphics2D getGraphics2d() {
    return (Graphics2D) getGraphics();
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D graphics2d = (Graphics2D) graphics;

    RenderingHints renderingHints = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2d.setRenderingHints(renderingHints);

    try {
      for (GameComponent gameComponent : gameComponents) {
        gameComponent.draw(graphics2d);
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  public static int[] getPosition(int mapIndexRow, int mapIndexCol) {
    int[] position = new int[2];

    position[0] = mapIndexCol * 32 / 2;
    position[1] = mapIndexRow * 32 / 2;

    return position;
  }

  public static void createWindow(Component component, KeyListener keyListener) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JFrame frame = new JFrame("Battle City");

        if (component != null) {
          frame.getContentPane().add(component);
        }

        if (keyListener != null) {
          frame.addKeyListener(keyListener);
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.setVisible(true);
      }
    });
  }

  public static void createGameLayout(GameLayout gameLayout, int[][] mapLayout) {
    for (int i = 0; i < mapLayout.length; i++) {
      for (int j = 0; j < mapLayout[i].length; j++) {
        int mapIndex = mapLayout[i][j];
        int[] position = getPosition(i, j);

        gameLayout.mapElements(mapIndex, position, BLOCK_WIDTH, BLOCK_HEIGHT);
      }
    }
  }
}
