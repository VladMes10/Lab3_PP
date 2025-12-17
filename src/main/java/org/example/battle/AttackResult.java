package org.example.battle;



import java.io.Serializable;

public class AttackResult implements Serializable {
    public final String attackerName;
    public final String targetName;
    public final int damage; 
    public final boolean success;
    public final String note;
    public AttackResult(org.example.droids.Droid a, org.example.droids.Droid t, int damage, boolean success, String note) {
        this.attackerName = a.getName();
        this.targetName = t.getName();
        this.damage = damage;
        this.success = success;
        this.note = note;
    }
    @Override
    public String toString() {
        if (!success) return String.format("%s -> %s: %s", attackerName, targetName, note);
        if (damage < 0) return String.format("%s лікує %s на %d HP (%s)", attackerName, targetName, -damage, note);
        return String.format("%s атакує %s на %d HP (%s)", attackerName, targetName, damage, note);
    }
}
