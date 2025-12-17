package org.example.battle;

import java.io.Serializable;

public class BattleResult implements Serializable {
    private String winner;
    private int rounds;
    public BattleResult(String winner, int rounds) { this.winner = winner; this.rounds = rounds; }
    public String summary() { return String.format("Переможець: %s, Раундів: %d", winner, rounds); }
}
