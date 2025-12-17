package org.example.droids;

import org.example.arenas.Arena;

import org.example.battle.AttackResult;
import org.example.weapons.MedKit;

public class MedicDroid extends Droid {
    public MedicDroid(String name) {
        super((name == null || name.isEmpty()) ? "Medic-" + new java.util.Random().nextInt(999) : name, 100, 8);
        this.armor = 2; this.accuracy = 0.85; this.crit = 0.05; this.regen = 4;
    }

    @Override
    public AttackResult attack(Droid target, Arena arena) {
        if (equipped instanceof MedKit) {
            boolean[] wasCrit = new boolean[1];
            int val = equipped.computeDamage(this, target, arena, wasCrit);
            if (val == 0) return new AttackResult(this, target, 0, false, "промах");
            if (val < 0) { target.heal(-val); return new AttackResult(this, target, val, true, "лікування"); }
            target.takeDamage(val);
            return new AttackResult(this, target, val, true, "удар");
        }
        if (Math.random() < 0.7) {
            int h = 12 + level;
            target.heal(h);
            return new AttackResult(this, target, -h, true, "лікування");
        } else {
            int dmg = baseDamage;
            target.takeDamage(dmg);
            return new AttackResult(this, target, dmg, true, "удар");
        }
    }
}
