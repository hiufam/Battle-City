package tanks;

import classes.Tank;

public class PowerTank extends Tank {

  public PowerTank(String name, int health, int points, int movementSpeed, int bulletSpeed, int bulletsPerAttack,
      float attackSpeed, boolean immunity) {
    super(name, 1, 300, 2, 3, bulletsPerAttack, attackSpeed, immunity);
  }

}
