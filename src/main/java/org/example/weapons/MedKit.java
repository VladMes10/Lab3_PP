package org.example.weapons;

import org.example.arenas.Arena;
import org.example.droids.Droid;

public class MedKit extends Weapon {
    private int heal;
    public MedKit(int baseDamage, double accuracy, int energyCost, int heal) { super("MedKit", baseDamage, accuracy, energyCost); this.heal = heal; }

    @Override
    public int computeDamage(Droid attacker, Droid target, Arena arena, boolean[] wasCrit) {
        double acc = attacker.getAccuracy() * accuracy * arena.getAccuracyMultiplier();
        if (Math.random() > acc) { wasCrit[0] = false; return 0; }
        wasCrit[0] = false;
        int h = heal + attacker.getLevel();
        return -h;
    }
}
