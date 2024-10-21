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
import utils.CommonUtil;
import utils.DrawUtils;
import utils.MovementUtil;

public class Enemy extends GameComponent {
  private Vector2D direction = randomDirection();;
  private Vector2D prevDirection = new Vector2D(0, 0);
  private Bullet bullet;
  private double speed = 180;

  private double attackInterval = 0.25;
  private double attackIntervalTimer = 0;

  private double randomDirectionInterval = CommonUtil.randomDouble(1, 2);
  private double randomDirectionTimer = 0;

  private double randomAttackInterval = CommonUtil.randomInteger(4, 5);
  private double randomAttackIntervalTimer = 0;

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

  public Enemy(Vector2D position) {
    super(GameComponentType.ENEMY, position, 32, 32);

    this.bullet = new Bullet(getCenter(), 0, 0, Color.RED, this, GameComponentType.PLAYER);

    setCollision(new CollisionBox(this, new Vector2D(1, 1), width - 2, height - 2));
  }

  public void draw(Graphics2D graphics2d) {
    graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
  }

  @Override
  public void update(double deltaTime) {
    randomDirection();
    shoot(direction);

    if (frameIndex > 1) {
      frameIndex = 0;
    }

    if (animationTimer > 0.1) {
      animationTimer = 0;
      frameIndex++;
    } else {
      animationTimer += deltaTime;
    }

    if (checkCollision(GameComponentsManager.getCollisionComponents(type), null, deltaTime) == null) {
      move(deltaTime);
      MovementUtil.tileMapPositionAssist(this);
    }

    randomDirectionTimer += deltaTime;
    attackIntervalTimer += deltaTime;
    randomAttackIntervalTimer += deltaTime;
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
    GameComponentsManager.remove(this);
  }

  public void shoot(Vector2D direction) {
    if (randomAttackIntervalTimer < randomAttackInterval) {
      return;
    }

    if (attackIntervalTimer < attackInterval) {
      return;
    }

    createBullet(direction);

    attackIntervalTimer = 0;
    randomAttackIntervalTimer = 0;
    randomAttackInterval = CommonUtil.randomInteger(1, 3);
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

  private Vector2D randomDirection() {
    if (randomDirectionTimer < randomDirectionInterval) {
      return new Vector2D(0, 0);
    }
    int randomInt = CommonUtil.randomInteger(0, 3);

    randomDirectionTimer = 0;
    switch (randomInt) {
      case 0:
        return direction = new Vector2D(0, 1);
      case 1:
        return direction = new Vector2D(0, -1);
      case 2:
        return direction = new Vector2D(1, 0);
      case 3:
        return direction = new Vector2D(-1, 0);
      default:
        return direction = new Vector2D(0, 0);
    }
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