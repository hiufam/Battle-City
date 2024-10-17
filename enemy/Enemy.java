package enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.random.RandomGenerator;

import bullet.Bullet;
import classes.GameComponent;
import classes.GameSprite;
import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import managers.GameComponentsManager;
import utils.DrawUtils;
import utils.MovementUtil;

public class Enemy extends GameComponent {
  private Vector2D direction = new Vector2D(1, 0);
  private Vector2D prevDirection = new Vector2D(0, 0);
  private Color color;
  private Bullet bullet;
  private double speed = 180;
  private double attackInterval = 0.25;
  private double attackIntervalTimer = 0;

  private double randomDirectionInterval = 1.5;
  private double randomDirectionTimer = 0;

  // NEED IMMEDIATE FIX
  private int frameIndex = 0;
  private double animationTimer = 0;
  GameSprite moveUpFrame1 = new GameSprite("images\\tank_player1_up_c0_t1.png");
  GameSprite moveUpFrame2 = new GameSprite("images\\tank_player1_up_c0_t2.png");
  GameSprite[] moveUpFrames = { moveUpFrame1, moveUpFrame2 };

  GameSprite moveDownFrame1 = new GameSprite("images\\tank_player1_down_c0_t1.png");
  GameSprite moveDownFrame2 = new GameSprite("images\\tank_player1_down_c0_t2.png");
  GameSprite[] moveDownFrames = { moveDownFrame1, moveDownFrame2 };

  GameSprite moveRightFrame1 = new GameSprite("images\\tank_player1_right_c0_t1.png");
  GameSprite moveRightFrame2 = new GameSprite("images\\tank_player1_right_c0_t2.png");
  GameSprite[] moveRightFrames = { moveRightFrame1, moveRightFrame2 };

  GameSprite moveLeftFrame1 = new GameSprite("images\\tank_player1_left_c0_t1.png");
  GameSprite moveLeftFrame2 = new GameSprite("images\\tank_player1_left_c0_t2.png");
  GameSprite[] moveLeftFrames = { moveLeftFrame1, moveLeftFrame2 };

  public Enemy(Vector2D position, int width, int height, Color color) {
    super(GameComponentType.ENEMY, position, width, height);

    this.color = color;
    this.bullet = new Bullet(getCenter(), 0, 0, Color.RED, this);

    setCollision(new CollisionBox(this, new Vector2D(1, 1), width - 2, height - 2));
  }

  public void draw(Graphics2D graphics2d) {
    graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
  }

  @Override
  public void update(double deltaTime) {
    randomDirection();

    if (frameIndex > 1) {
      frameIndex = 0;
    }

    if (animationTimer > 0.1) {
      animationTimer = 0;
      frameIndex++;
    } else {
      animationTimer += deltaTime;
    }

    if (checkCollision(GameComponentsManager.getEnemyCollisionComponents(), deltaTime) == null) {
      move(deltaTime);
      MovementUtil.tileMapPositionAssist(this);
    }

    if (!direction.isEqual(prevDirection)) {
    }

    randomDirectionTimer += deltaTime;
  }

  public void move(double deltaTime) {
    setVelocity(direction.multiply(speed));
    setPosition(getPosition().add(getVelocity().multiply(deltaTime)));

    if (!prevDirection.isEqual(direction)) {
      prevDirection = direction;
    }

    if (frameIndex < 2 && !velocity.isZero()) {
      if (velocity.x > 0) {
        setSprite(moveRightFrames[frameIndex]);
      }
      if (velocity.x < 0) {
        setSprite(moveLeftFrames[frameIndex]);
      }
      if (velocity.y > 0) {
        setSprite(moveDownFrames[frameIndex]);
      }
      if (velocity.y < 0) {
        setSprite(moveUpFrames[frameIndex]);
      }
    }
  }

  @Override
  public void destroy() {
    return;
  }

  public void shoot(Vector2D direction) {
    if (attackIntervalTimer < attackInterval) {
      return;
    }
    createBullet(direction);

    attackIntervalTimer = 0;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public double getSpeed() {
    return this.speed;
  }

  public void setDirection(Vector2D direction) {
    this.direction = direction;
  }

  public Vector2D getDirection() {
    return this.direction;
  }

  public void setAttackInterval(double speed) {
    this.attackInterval = speed;
  }

  public double getAttackInterval() {
    return this.attackInterval;
  }

  public Vector2D getPrevDirection() {
    return this.prevDirection;
  }

  private void randomDirection() {
    if (randomDirectionTimer < randomDirectionInterval) {
      return;
    }
    int minValue = 0;
    int maxValue = 3;
    Random random = new Random();
    int randomInt = minValue + random.nextInt(maxValue - minValue + 1);

    switch (randomInt) {
      case 0:
        direction = new Vector2D(0, 1);
        break;
      case 1:
        direction = new Vector2D(0, -1);
        break;
      case 2:
        direction = new Vector2D(1, 0);
        break;
      case 3:
        direction = new Vector2D(-1, 0);
        break;
      default:
        direction = new Vector2D(0, 0);
        break;
    }
    randomDirectionTimer = 0;
  }

  private void createBullet(Vector2D direction) {
    if (bullet == null || !bullet.isVisible()) {
      return;
    }

    if (direction.x != 0) {
      bullet.width = 16;
      bullet.height = 8;
    }
    if (direction.y != 0) {
      bullet.width = 8;
      bullet.height = 16;
    }

    Vector2D newBulletPosition = getCenter().minus(new Vector2D(bullet.width / 2, bullet.height / 2));
    bullet.setPosition(newBulletPosition);
    bullet.setVisibility(false);
    bullet.setCollision(new CollisionBox(bullet, new Vector2D(0, 0), bullet.width, bullet.height));
    bullet.setVelocity(direction.multiply(280));
  }
}