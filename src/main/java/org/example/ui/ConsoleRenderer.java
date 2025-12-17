package org.example.ui;

public class ConsoleRenderer {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    public void drawTitle(String title) {
        System.out.println(CYAN + "========================================" + RESET);
        System.out.println(YELLOW + title + RESET);
        System.out.println(CYAN + "========================================" + RESET);
    }

    public void drawRound(int round) {
        System.out.println("\n" + CYAN + "─── Раунд " + round + " ───" + RESET);
    }

    private String hpBar(String name, int hp, int maxHp) {
        int len = 20;
        int filled = (int)((double)hp / maxHp * len);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < filled; i++)
            sb.append("#");
        for (int i = filled; i < len; i++)
            sb.append("-");

        String color =
                hp > maxHp * 0.6 ? GREEN :
                        hp > maxHp * 0.3 ? YELLOW : RED;

        return String.format("%s%s [%s] HP:%d/%d%s",
                color, name, sb.toString(), hp, maxHp, RESET);
    }

    public void drawDuelState(
            org.example.droids.Droid a,
            org.example.droids.Droid b
    ) {
        System.out.println(
                hpBar(a.getName(), a.getHealth(), a.getMaxHealth())
        );
        System.out.println(
                hpBar(b.getName(), b.getHealth(), b.getMaxHealth())
        );
    }

    public void drawTeamState(
            org.example.droids.Team t1,
            org.example.droids.Team t2
    ) {
        System.out.println("\n" + CYAN + "Команда 1:" + RESET);
        t1.getMembers().forEach(
                d -> System.out.println(
                        hpBar(d.getName(), d.getHealth(), d.getMaxHealth())
                )
        );

        System.out.println("\n" + CYAN + "Команда 2:" + RESET);
        t2.getMembers().forEach(
                d -> System.out.println(
                        hpBar(d.getName(), d.getHealth(), d.getMaxHealth())
                )
        );
    }

    public void drawBattleAction(
            org.example.droids.Droid attacker,
            org.example.droids.Droid target
    ) {
        System.out.printf(
                "\n%s%s атакує %s! \uD83D\uDCA5%s\n",
                YELLOW, attacker.getName(), target.getName(), RESET
        );
    }

    public void drawHealAction(
            org.example.droids.Droid healer,
            org.example.droids.Droid target
    ) {
        System.out.printf(
                GREEN + "%s лікує %s! \\uD83D\\uDC9A%s\n",
                healer.getName(), target.getName(), RESET
        );
    }

    public void drawWinner(String name) {
        System.out.println("\n" + YELLOW + "\uD83C\uDFC6 Переможець: " + name + " \uD83C\uDFC6" + RESET);

    }
}
