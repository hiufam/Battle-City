package player;

import java.awt.Color;
import java.awt.Graphics2D;

import bullet.Bullet;
import classes.GameComponent;
import classes.GameSprite;
import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import managers.GameComponentsManager;

public class Player extends GameComponent {
  final public double DEFAULT_SPEED = 180;

  private Vector2D direction = new Vector2D(0, 0);
  private Vector2D prevDirection = new Vector2D(0, 0);
  private Bullet bullet;
  private double speed = DEFAULT_SPEED;
  private double attackInterval = 0.25;
  private double attackIntervalTimer = 0;

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

  public Player(Vector2D position) {
    super(GameComponentType.PLAYER, position, 32, 32);

    this.bullet = new Bullet(getCenter(), 0, 0, Color.RED, this, GameComponentType.ENEMY);

    setSprite(moveUpFrame1);
    setCollision(new CollisionBox(this, new Vector2D(1, 1), width - 2, height - 2));
    collisionBox.setEnableFrontCollisionCheck(true);
  }

  public void draw(Graphics2D graphics2d) {
    graphics2d.drawImage(sprite.getBufferedImage(), (int) x, (int) y, this);
  }

  @Override
  public void update(double deltaTime) {

    // Used for ice env only
    // if (CollisionUtil.isCollisionBoxOverlapGameComponent(collisionBox) != null &&
    // CollisionUtil.isCollisionBoxOverlapGameComponent(collisionBox).getType() ==
    // GameComponentType.ICE) {
    // if (!getDirection().isZero()) {
    // prevDirection = getDirection();
    // }
    // setDirection(prevDirection);
    // }

    if (checkCollision(GameComponentsManager.getCollisionComponents(type), null, deltaTime) == null) {
      move(deltaTime);
    }

    if (frameIndex > 1) {
      frameIndex = 0;
    }

    if (animationTimer > 0.1) {
      animationTimer = 0;
      frameIndex++;
    } else {
      animationTimer += deltaTime;
    }

    attackIntervalTimer += deltaTime;
  }

  public void move(double deltaTime) {
    setVelocity(direction.multiply(speed));
    setPosition(getPosition().add(getVelocity().multiply(deltaTime)));

    // System.out.println(velocity);

    if (!getDirection().isZero()) {
      prevDirection = getDirection();
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
    } else {
      createBullet(direction);
    }

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
