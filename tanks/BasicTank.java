package tanks;

import classes.Tank;

public class BasicTank extends Tank {

  public BasicTank(String name, int health, int points, int movementSpeed, int bulletSpeed, int bulletsPerAttack,
      float attackSpeed, boolean immunity) {
    super(name, 1, 100, 1, 1, bulletsPerAttack, attackSpeed, immunity);
  }
}
