package org.example.droids;

import org.example.arenas.Arena;

import org.example.battle.AttackResult;

import org.example.weapons.Laser;

public class AssaultDroid extends Droid {
    public AssaultDroid(String name) {
        super((name == null || name.isEmpty()) ? "Assault-" + new java.util.Random().nextInt(999) : name, 120, 16);
        this.armor = 4; this.accuracy = 0.9; this.crit = 0.12; this.regen = 3;
    }

    @Override
    public AttackResult attack(Droid target, Arena arena) {
        if (equipped == null) {
            boolean[] wasCrit = new boolean[1];
            int d = new Laser(baseDamage, 0.9, 5).computeDamage(this, target, arena, wasCrit);
            if (d == 0) return new AttackResult(this, target, 0, false, "промах");
            target.takeDamage(d);
            return new AttackResult(this, target, d, true, wasCrit[0] ? "критичний удар" : "удар");
        }
        boolean[] wasCrit = new boolean[1];
        int val = equipped.computeDamage(this, target, arena, wasCrit);
        if (val == 0) return new AttackResult(this, target, 0, false, "промах");
        if (val < 0) { target.heal(-val); return new AttackResult(this, target, val, true, "лікування"); }
        target.takeDamage(val);
        return new AttackResult(this, target, val, true, wasCrit[0] ? "критичний удар" : "удар");
    }
}
