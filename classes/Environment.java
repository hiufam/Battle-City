package classes;

public class Environment {
  private boolean isDestructible;
  private int health;

  public Environment(boolean isDestructible, int health) {
    this.isDestructible = isDestructible;
    this.health = health;
  }

  public void setIsDestructible(boolean isDestructible) {
    this.isDestructible = isDestructible;
  }

  public boolean getIsDestructible() {
    return this.isDestructible;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int setHealth() {
    return this.health;
  }
}
