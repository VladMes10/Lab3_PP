package org.example.weapons;

import org.example.arenas.Arena;
import org.example.droids.Droid;


public class Railgun extends Weapon {
    public Railgun(int baseDamage, double accuracy, int energyCost) { super("Railgun", baseDamage, accuracy, energyCost); }

    @Override
    public int computeDamage(Droid attacker, Droid target, Arena arena, boolean[] wasCrit) {
        double acc = attacker.getAccuracy() * accuracy * arena.getAccuracyMultiplier();
        if (Math.random() > acc) { wasCrit[0] = false; return 0; }
        boolean crit = Math.random() < attacker.getCritChance() * 1.2;
        wasCrit[0] = crit;
        int d = baseDamage + (int)(attacker.getLevel() * 1.8);
        if (crit) d += baseDamage;
        d = Math.max(0, d - target.getArmor() / 3);
        return d;
    }
}
