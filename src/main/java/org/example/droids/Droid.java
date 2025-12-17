package org.example.droids;

import org.example.arenas.Arena;
import org.example.weapons.Weapon;

import java.io.Serializable;
import java.util.Random;

public abstract class Droid implements Cloneable, Serializable {
    protected String name;
    protected int maxHealth;
    protected int health;
    protected int baseDamage;
    protected int level = 1;
    protected int xp = 0;
    protected int energy = 100;
    protected int maxEnergy = 100;
    protected int armor = 0;
    protected double accuracy = 1.0;
    protected double crit = 0.1;
    protected int regen = 2;
    protected Weapon equipped;

    protected transient Random rnd = new Random();

    public Droid(String name, int health, int baseDamage) {
        this.name = (name == null || name.isEmpty()) ? "Droid-" + Math.abs(new Random().nextInt() % 1000) : name;
        this.maxHealth = health;
        this.health = health;
        this.baseDamage = baseDamage;
    }

    public boolean isAlive() { return health > 0; }
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getArmor() { return armor; }
    public double getAccuracy() { return accuracy; }
    public double getCritChance() { return crit; }
    public int getLevel() { return level; }

    public Weapon getEquipped() { return equipped; }
    public void equip(Weapon w) { this.equipped = w; }

    public void takeDamage(int d) { health -= d; if (health < 0) health = 0; }
    public void heal(int h) { health += h; if (health > maxHealth) health = maxHealth; }
    public void regenTick() { heal(regen); energy = Math.min(maxEnergy, energy + 5); }

    public String shortInfo() { return String.format("%s L%d HP:%d/%d E:%d/%d", name, level, health, maxHealth, energy, maxEnergy); }
    public String detailedInfo() {
        String wep = (equipped == null) ? "None" : equipped.getName();
        return String.format("%s | L%d | HP: %d/%d | E:%d/%d | ARM:%d | ACC:%.2f | W:%s | XP:%d", name, level, health, maxHealth, energy, maxEnergy, armor, accuracy, wep, xp);
    }

    public abstract org.example.battle.AttackResult attack(Droid target, Arena arena);

    public void gainXP(int amount) {
        xp += amount;
        while (xp >= level * 50) {
            xp -= level * 50;
            level++;
            improveOnLevel();
            System.out.println("\u001B[35m" + name + " піднявся до рівня " + level + "\u001B[0m");
        }
    }

    protected void improveOnLevel() {
        maxHealth += 10; health += 10; baseDamage += 2; armor += 1;
    }

    @Override
    public Droid clone() {
        try {
            Droid copy = (Droid) super.clone();
            copy.rnd = new Random();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
