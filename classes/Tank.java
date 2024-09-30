package classes;

import common.Vector2D;
import enums.Direction;

public class Tank {
  private String name;
  private int health;
  private int points;
  private int movementSpeed;
  private int bulletSpeed;
  private int bulletsPerAttack;
  private int damageAgainstBrickWall = 1;
  private float attackSpeed;
  private boolean immunity = false;
  private boolean canDestroySteelWall = false;

  private Vector2D position;
  private Direction direction;

  public Tank(String name, int health, int points, int movementSpeed, int bulletSpeed, int bulletsPerAttack,
      float attackSpeed, boolean immunity) {
    this.name = name;
    this.health = health;
    this.points = points;
    this.movementSpeed = movementSpeed;
    this.bulletSpeed = bulletSpeed;
    this.bulletsPerAttack = bulletsPerAttack;
    this.attackSpeed = attackSpeed;
    this.immunity = immunity;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getHealth() {
    return this.health;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public int getPoints() {
    return this.points;
  }

  public void setMovementSpeed(int movementSpeed) {
    this.movementSpeed = movementSpeed;
  }

  public int getMovementSpeed() {
    return this.movementSpeed;
  }

  public void setBulletSpeed(int speed) {
    this.bulletSpeed = speed;
  }

  public int bulletSpeed() {
    return this.bulletSpeed;
  }

  public void setAttackSpeed(int speed) {
    this.attackSpeed = speed;
  }

  public float getAttackSpeed() {
    return this.attackSpeed;
  }

  public void setImmunity(boolean immunity) {
    this.immunity = immunity;
  }

  public boolean getImmunity() {
    return this.immunity;
  }

  public void setBulletsPerAttack(int bullets) {
    this.bulletsPerAttack = bullets;
  }

  public int getBulletsPerAttack() {
    return this.bulletsPerAttack;
  }

  public void setPosition(Vector2D position) {
    this.position = position;
  }

  public Vector2D getPosition() {
    return this.position;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public Direction getDirection() {
    return this.direction;
  }

  public void setDamageAgainstBrickWall(int damage) {
    this.damageAgainstBrickWall = damage;
  }

  public int getDamageAgainstBrickWall() {
    return this.damageAgainstBrickWall;
  }

  public void setCanDestroySteelWall(boolean canDestroySteelWall) {
    this.canDestroySteelWall = canDestroySteelWall;
  }

  public boolean getCanDestroySteelWall() {
    return this.canDestroySteelWall;
  }
}
