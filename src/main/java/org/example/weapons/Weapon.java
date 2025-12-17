package org.example.weapons;

import org.example.arenas.Arena;
import org.example.droids.Droid;

import java.io.Serializable;

public abstract class Weapon implements Serializable {
    protected String name;
    protected int baseDamage;
    protected double accuracy;
    protected int energyCost;

    public Weapon(String name, int baseDamage, double accuracy, int energyCost) {
        this.name = name;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
        this.energyCost = energyCost;
    }

    public String getName() { return name; }

    public abstract int computeDamage(Droid attacker, Droid target, Arena arena, boolean[] wasCrit);
}
