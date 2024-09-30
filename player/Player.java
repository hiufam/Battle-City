package player;

import java.awt.Color;
import java.awt.Graphics2D;

import bullet.Bullet;
import classes.GameComponent;
import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import managers.GameComponentsManager;
import utils.DrawUtils;

public class Player extends GameComponent {
  private Vector2D direction = new Vector2D(0, 0);
  private Vector2D prevDirection = new Vector2D(0, 0);
  private Color color;
  private Bullet bullet;
  private double speed = 180;
  private double attackInterval = 0.25;
  private double attackIntervalTimer = 0;

  public Player(Vector2D position, int width, int height, Color color) {
    super(GameComponentType.PLAYER, position, width, height);

    this.color = color; // set to (-36, -36) so it doesn't collide with player
    this.bullet = new Bullet(getCenter(), 0, 0, Color.RED, this);

    setCollision(new CollisionBox(this, new Vector2D(0.5, 0.5), width - 1, height - 1));
  }

  public void draw(Graphics2D graphics2d) {
    DrawUtils.drawRectangle(graphics2d, getPosition(), width, height, color);
  }

  @Override
  public void update(double deltaTime) {
    if (checkCollision(GameComponentsManager.getPlayerCollisionComponents(), deltaTime) == null) {
      move(deltaTime);
    }

    attackIntervalTimer += deltaTime;
  }

  public void move(double deltaTime) {
    setVelocity(direction.multiply(speed));
    setPosition(getPosition().add(getVelocity().multiply(deltaTime)));

    if (!getDirection().isZero()) {
      prevDirection = getDirection();
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