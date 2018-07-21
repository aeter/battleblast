package me.battleblast.entities;

public class BossTank extends EnemyTank {
    protected float nextBulletSpawnTime() {
        return 10 * ONE_MILLISECOND;
    }

    protected boolean timeToShootAgain() {
        return nPercentChance(15);
    }
}
