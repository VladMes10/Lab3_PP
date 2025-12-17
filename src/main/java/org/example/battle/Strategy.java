package org.example.battle;

import org.example.droids.Droid;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Strategy {
    public enum StrategyType { RANDOM, WEAKEST, STRONGEST, ROUND_ROBIN }
    private StrategyType type;
    private int rrIndex = 0;

    public Strategy(StrategyType type) { this.type = type; }

    public static Strategy chooseInteractive() {
        System.out.println("Оберіть стратегію бою:");
        System.out.println("1) RANDOM");
        System.out.println("2) WEAKEST");
        System.out.println("3) STRONGEST");
        System.out.println("4) ROUND_ROBIN");
        System.out.print("Вибір (Enter для RANDOM): ");
        String s = new Scanner(System.in).nextLine().trim();
        if (s.isEmpty()) return new Strategy(StrategyType.RANDOM);
        try {
            int v = Integer.parseInt(s);
            switch (v) {
                case 2: return new Strategy(StrategyType.WEAKEST);
                case 3: return new Strategy(StrategyType.STRONGEST);
                case 4: return new Strategy(StrategyType.ROUND_ROBIN);
                default: return new Strategy(StrategyType.RANDOM);
            }
        } catch (Exception e) { return new Strategy(StrategyType.RANDOM); }
    }

    public Droid pick(org.example.droids.Team own, org.example.droids.Team enemy) {
        List<Droid> alive = own.getAlive();
        if (alive.isEmpty()) return null;
        switch (type) {
            case RANDOM: return alive.get(new Random().nextInt(alive.size()));
            case WEAKEST:
                Droid min = null;
                for (Droid d : alive) if (min == null || d.getHealth() < min.getHealth()) min = d;
                return min;
            case STRONGEST:
                Droid max = null;
                for (Droid d : alive) if (max == null || d.getLevel() > max.getLevel()) max = d;
                return max;
            case ROUND_ROBIN:
                rrIndex = rrIndex % Math.max(1, alive.size());
                Droid pick = alive.get(rrIndex);
                rrIndex = (rrIndex + 1) % Math.max(1, alive.size());
                return pick;
            default: return alive.get(0);
        }
    }
}
