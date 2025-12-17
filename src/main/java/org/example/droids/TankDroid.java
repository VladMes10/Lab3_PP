package org.example.droids;

import org.example.arenas.Arena;

import org.example.battle.AttackResult;
import org.example.weapons.Laser;
import org.example.weapons.Weapon;


public class TankDroid extends Droid {
    public TankDroid(String name) {
        super((name == null || name.isEmpty()) ? "Tank-" + new java.util.Random().nextInt(999) : name, 160, 12);
        this.armor = 8; this.accuracy = 0.75; this.crit = 0.08; this.regen = 2;
    }

    @Override
    public AttackResult attack(Droid target, Arena arena) {
        boolean[] wasCrit = new boolean[1];
        Weapon w = (equipped != null) ? equipped : new Laser(baseDamage, 0.8, 2);
        int val = w.computeDamage(this, target, arena, wasCrit);
        if (val == 0) return new AttackResult(this, target, 0, false, "промах");
        if (val < 0) { target.heal(-val); return new AttackResult(this, target, val, true, "лікування"); }
        target.takeDamage(val);
        return new AttackResult(this, target, val, true, wasCrit[0] ? "критичний" : "звичайний");
    }
}
