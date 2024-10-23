package bullet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import common.Vector2D;
import components.CollisionBox;
import enums.GameComponentType;
import environments.BrickBlock;
import classes.GameComponent;
import interfaces.DestructibleComponent;
import managers.GameComponentsManager;
import utils.CollisionUtil;
import utils.DrawUtils;

public class Bullet extends GameComponent {
  private boolean hidden = true;
  private int damage = 1;
  private Color color;
  private GameComponent source;
  private GameComponentType targetType;
  private GameComponentType[] ignoreType;

  public Bullet(Vector2D position, int width, int height, Color color, GameComponent source,
      GameComponentType targetType) {
    super(GameComponentType.BULLET, position, width, height);
    this.source = source;
    this.targetType = targetType;
    this.ignoreType = new GameComponentType[] { source.getType() };

    setCollision(new CollisionBox(this, new Vector2D(0, 0), 16, 16));
  }

  public void draw(Graphics2D graphics2d) {
    if (hidden) {
      return;
    }

    graphics2d.setColor(color);
    DrawUtils.drawRectangle(graphics2d, getPosition(), width, height, color);

    if (collisionBox != null) {
      DrawUtils.drawRectangle(graphics2d, collisionBox.globalPosition, collisionBox.width, collisionBox.height,
          Color.LIGHT_GRAY);
    }
  }

  @Override
  public void update(double deltaTime) {
    if (hidden) {
      return;
    }

    if (CollisionUtil.isOutOfBound(this)) {
      destroy();
    }

    ArrayList<GameComponent> collidedGameComponents = checkCollision(GameComponentsManager.getCollisionComponents(type),
        ignoreType, deltaTime);

    if (collidedGameComponents == null) {
      move(deltaTime);
    }

    if (collidedGameComponents != null && collidedGameComponents.size() > 0) {
      damageComponents(collidedGameComponents);
    }
  }

  public void move(double deltaTime) {
    setPosition(position.add(velocity.multiply(deltaTime)));

    if (collisionBox != null) {
      collisionBox.setPosition(position);
    }
  }

  public void destroy() {
    hidden = true;
    collisionBox.setEnabled(false);
    setVelocity(new Vector2D(0, 0));
    setPosition(source.getCenter());
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public boolean isVisible() {
    return hidden;
  }

  public void setVisibility(boolean hide) {
    this.hidden = hide;
  }

  public void setCollision(CollisionBox collisionBox) {
    this.collisionBox = collisionBox;
  }

  private void damageComponents(ArrayList<GameComponent> collidedGameComponents) {
    for (int i = 0; i < collidedGameComponents.size(); i++) {
      GameComponent collidedGameComponent = collidedGameComponents.get(i);
      if (collidedGameComponent.getType() == source.getType()) {
        continue;
      }

      // special case. Could generalize by using health component but lazy
      if (collidedGameComponent instanceof BrickBlock) {
        ((BrickBlock) collidedGameComponent).hitComponent(this);
      }

      if (collidedGameComponent instanceof DestructibleComponent) {
        ((DestructibleComponent) collidedGameComponent).hit(damage);
      }

      if (targetType != null && collidedGameComponent.getType() == targetType) {
        collidedGameComponent.destroy();
      }

      if (i == collidedGameComponents.size() - 1) {
        destroy();
      }
    }
  }
}