package org.example.battle;

import org.example.arenas.Arena;
import org.example.droids.Droid;
import org.example.droids.MedicDroid;
import org.example.droids.Team;
import org.example.ui.ConsoleRenderer;

import java.util.Random;

public class BattleEngine {
    private Arena arena;
    private BattleLogger logger;
    private Strategy strategy;
    private Random rnd = new Random();
    private ConsoleRenderer renderer = new ConsoleRenderer();

    public BattleEngine(Arena arena, BattleLogger logger, Strategy strategy) {
        this.arena = arena;
        this.logger = logger;
        this.strategy = strategy;
    }

    public BattleResult duel(Droid a, Droid b) {
        logger.log(String.format("Бій 1v1 на аренi: %s - %s",
                arena.getName(), arena.getDescription()));
        logger.log(String.format("%s VS %s",
                a.detailedInfo(), b.detailedInfo()));

        renderer.clearScreen();
        renderer.drawTitle("⚔ Бій 1 на 1");

        int round = 1;

        while (a.isAlive() && b.isAlive()) {

            renderer.drawRound(round);
            renderer.drawDuelState(a, b);

            takeTurn(a, b);
            if (!b.isAlive()) break;

            renderer.pause(700);
            renderer.drawBattleAction(a, b);
            renderer.drawDuelState(a, b);

            takeTurn(b, a);
            renderer.pause(700);
            renderer.drawBattleAction(b, a);

            renderer.drawDuelState(a, b);

            a.regenTick();
            b.regenTick();

            round++;
            renderer.pause(900);
        }

        Droid winner = a.isAlive() ? a : b;
        logger.log("Переможець: " + winner.getName());

        renderer.drawWinner(winner.getName());
        renderer.pause(2000);

        return new BattleResult(winner.getName(), round);
    }

    public BattleResult teamBattle(Team t1, Team t2) {

        logger.log(String.format("Team Fight на арені: %s", arena.getName()));
        renderer.clearScreen();
        renderer.drawTitle("⚔ Бій Команда vs Команда");

        int round = 1;

        while (t1.hasAlive() && t2.hasAlive()) {

            renderer.drawRound(round);
            renderer.drawTeamState(t1, t2);

            Droid a = strategy.pick(t1, t2);
            Droid b = strategy.pick(t2, t1);

            if (a == null || b == null)
                break;

            takeTurn(a, b);
            renderer.drawBattleAction(a, b);
            renderer.pause(500);

            if (!t2.hasAlive())
                break;

            Droid aa = strategy.pick(t2, t1);
            Droid bb = strategy.pick(t1, t2);

            if (aa == null || bb == null)
                break;

            takeTurn(aa, bb);
            renderer.drawBattleAction(aa, bb);
            renderer.pause(500);

            supportiveActions(t1);
            supportiveActions(t2);

            renderer.drawTeamState(t1, t2);
            renderer.pause(1200);

            round++;
        }

        String winner = t1.hasAlive() ? t1.getName() : t2.getName();
        logger.log("Переможець команди: " + winner);

        renderer.drawWinner(winner);
        renderer.pause(2000);

        return new BattleResult(winner, round);
    }

    private void supportiveActions(Team t) {
        for (Droid d : t.getMembers()) {
            if (!d.isAlive()) continue;

            if (d instanceof MedicDroid) {
                Droid need = null;
                int minHP = Integer.MAX_VALUE;

                for (Droid s : t.getMembers()) {
                    if (s.isAlive() && s.getHealth() < minHP) {
                        need = s;
                        minHP = s.getHealth();
                    }
                }

                if (need != null && need != d && need.getHealth() < need.getMaxHealth()) {
                    AttackResult res = ((MedicDroid) d).attack(need, arena);
                    logger.log("Підтримка: " + res.toString());
                    renderer.drawHealAction(d, need);
                    renderer.pause(500);
                }
            }
            d.regenTick();
        }
    }

    private void takeTurn(Droid actor, Droid target) {
        AttackResult r = actor.attack(target, arena);
        logger.log(r.toString());
    }
}
