package tanks;

import classes.Tank;

public class FastTank extends Tank {

  public FastTank(String name, int health, int points, int movementSpeed, int bulletSpeed, int bulletsPerAttack,
      float attackSpeed, boolean immunity) {
    super(name, 1, 300, 3, 2, bulletsPerAttack, attackSpeed, immunity);
  }

}
