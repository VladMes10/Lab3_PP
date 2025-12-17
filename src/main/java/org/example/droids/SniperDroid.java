package org.example.droids;

import org.example.arenas.Arena;

import org.example.battle.AttackResult;

import org.example.weapons.Weapon;
import org.example.weapons.Railgun;

public class SniperDroid extends Droid {
    public SniperDroid(String name) {
        super((name == null || name.isEmpty()) ? "Sniper-" + new java.util.Random().nextInt(999) : name, 85, 24);
        this.armor = 1; this.accuracy = 1.1; this.crit = 0.25; this.regen = 1;
    }

    @Override
    public AttackResult attack(Droid target, Arena arena) {
        boolean[] wasCrit = new boolean[1];
        Weapon w = (equipped != null) ? equipped : new Railgun(baseDamage, 0.85, 3);
        int val = w.computeDamage(this, target, arena, wasCrit);
        if (val == 0) return new AttackResult(this, target, 0, false, "промах");
        if (val < 0) { target.heal(-val); return new AttackResult(this, target, val, true, "лікування"); }
        target.takeDamage(val);
        return new AttackResult(this, target, val, true, wasCrit[0] ? "влучання у критичну точку" : "влучання");
    }
}
