package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import common.Vector2D;

public class DrawUtils {
  public static void drawLine(Graphics2D graphics2d, Vector2D startPosition, Vector2D endPosition) {
    Line2D.Double line = new Line2D.Double(startPosition.x, startPosition.y, endPosition.x, endPosition.y);
    graphics2d.draw(line);
  }

  public static void drawArrowLine(Graphics2D graphics2d, Vector2D startPosition, Vector2D endPosition, int width,
      int height) {
    int startPostionX = (int) startPosition.x;
    int startPostionY = (int) startPosition.y;
    int endPositionX = (int) endPosition.x;
    int endPositionY = (int) endPosition.y;

    int dx = endPositionX - startPostionX, dy = endPositionY - startPostionY;
    double D = Math.sqrt(dx * dx + dy * dy);
    double xm = D - width, xn = xm, ym = height, yn = -height, x;
    double sin = dy / D, cos = dx / D;

    x = xm * cos - ym * sin + startPostionX;
    ym = xm * sin + ym * cos + startPostionY;
    xm = x;

    x = xn * cos - yn * sin + startPostionX;
    yn = xn * sin + yn * cos + startPostionY;
    xn = x;

    int[] xpoints = { endPositionX, (int) xm, (int) xn };
    int[] ypoints = { endPositionY, (int) ym, (int) yn };

    graphics2d.drawLine(startPostionX, startPostionY, endPositionX, endPositionY);
    graphics2d.fillPolygon(xpoints, ypoints, 3);
  }

  public static void drawGrid(Graphics2D graphics2d, int width, int height, int tileWidth, int tileHeight,
      Color color) {
    graphics2d.setColor(color);

    for (int i = 0; i < width; i += tileWidth) {
      drawLine(graphics2d, new Vector2D(i, 0), new Vector2D(i, height));
    }

    for (int i = 0; i < height; i += tileHeight) {
      drawLine(graphics2d, new Vector2D(0, i), new Vector2D(width, i));
    }
  }

  public static void drawCirlce(Graphics2D graphics2d, Vector2D position, int width, int height, Color color) {
    graphics2d.setColor(color);
    Ellipse2D.Double circle = new Ellipse2D.Double(position.x, position.y, width, height);
    graphics2d.fill(circle);
  }

  public static void drawRectangle(Graphics2D graphics2d, Vector2D position, int width, int height, Color color) {
    graphics2d.setColor(color);
    Rectangle.Double rectangle = new Rectangle.Double(position.x, position.y, width, height);
    graphics2d.fill(rectangle);
  }
}
