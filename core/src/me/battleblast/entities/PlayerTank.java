package me.battleblast.entities;

public class PlayerTank extends Tank {
    protected float nextBulletSpawnTime() {
         return 500 * ONE_MILLISECOND;
    }
}
