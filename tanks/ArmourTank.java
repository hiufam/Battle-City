package tanks;

import classes.Tank;

public class ArmourTank extends Tank {

  public ArmourTank(String name, int health, int points, int movementSpeed, int bulletSpeed, int bulletsPerAttack,
      float attackSpeed, boolean immunity) {
    super(name, 4, 400, 2, 2, bulletsPerAttack, attackSpeed, immunity);
  }
}
