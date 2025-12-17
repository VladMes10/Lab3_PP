package org.example.arenas;

import java.util.Random;
import java.util.Scanner;

public class Arena {
    private String name;
    private double accuracyMultiplier;
    private String description;

    public Arena(String name, double accMult, String description) { this.name = name; this.accuracyMultiplier = accMult; this.description = description; }

    public double getAccuracyMultiplier() { return accuracyMultiplier; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public static Arena randomArena() {
        Arena[] a = new Arena[] {
                new Arena("Забруднений цех", 0.85, "Пил і пар знижують точність"),
                new Arena("Рівнина", 1.0, "Нічого особливого"),
                new Arena("Гірська місцевість", 0.95, "Перешкоди знижують мобільність"),
                new Arena("Антенний майданчик", 1.05, "Вигідні умови для снайперів"),
                new Arena("Туман", 0.8, "Сильний туман знижує точність"),
                new Arena("Болото", 0.9, "Повільна місцевість"),
                new Arena("Пустеля", 0.88, "Пісок знижує електроніку"),
                new Arena("Космос (станція)", 1.02, "Слабка гравітація")
        };
        return a[new Random().nextInt(a.length)];
    }

    public static Arena chooseInteractive() {
        Arena[] a = new Arena[] {
                new Arena("Забруднений цех", 0.85, "Пил і пар знижують точність"),
                new Arena("Рівнина", 1.0, "Нічого особливого"),
                new Arena("Антенний майданчик", 1.05, "Вигідні умови для снайперів"),
                new Arena("Туман", 0.8, "Сильний туман знижує точність")
        };
        System.out.println("Оберіть арену:");
        for (int i=0;i<a.length;i++) System.out.printf("%d) %s - %s%n", i+1, a[i].getName(), a[i].getDescription());
        System.out.print("Ваш вибір (Enter для випадкової): ");
        String s = new Scanner(System.in).nextLine().trim();
        if (s.isEmpty()) return randomArena();
        try {
            int idx = Integer.parseInt(s)-1;
            if (idx<0 || idx>=a.length) return randomArena();
            return a[idx];
        } catch (Exception e) { return randomArena(); }
    }
}
