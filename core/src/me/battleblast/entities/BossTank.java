package me.battleblast.entities;

public class BossTank extends EnemyTank {
    @Override
    protected float nextBulletSpawnTime() {
        return 10 * ONE_MILLISECOND;
    }

    @Override
    protected boolean timeToShootAgain() {
        return nPercentChance(15);
    }
}
