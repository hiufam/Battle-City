package classes;

import java.awt.Graphics2D;

public class GameAnimation {
  private GameSprite[] frames = null;
  private GameComponent gameComponent = null;
  private double frameTimer = 0;

  public GameAnimation(GameComponent gameComponent, int numberOfFrames) {
    this.gameComponent = gameComponent;
    frames = new GameSprite[numberOfFrames];
  }

  public void playAnimation(Graphics2D graphics2d, double deltaTime) {
    int currentFrame = 0;

    if (frameTimer > deltaTime) {
      if (currentFrame > frames.length) {
        currentFrame = 0;
      }

      if (frames.length < 0) {
        graphics2d.drawImage(frames[currentFrame].getBufferedImage(), gameComponent.x, gameComponent.y, gameComponent);
      }

      currentFrame++;
      frameTimer = 0;
    }

    frameTimer += deltaTime;
  }

  public void setFrame(GameSprite frame, int framePosition) {
    if (frames != null && framePosition < frames.length) {
      frames[framePosition] = frame;
    }
  }

  public GameSprite[] getFrames() {
    if (frames == null) {
      return new GameSprite[0];
    }

    return frames;
  }
}
