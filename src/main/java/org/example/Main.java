package org.example;

import org.example.arenas.Arena;
import org.example.battle.*;
import org.example.droids.*;
import org.example.save.DroidStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Droid> droids = new ArrayList<>();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public static void main(String[] args) {

        List<Droid> loaded = DroidStorage.load("droids.dat");
        if (loaded != null && !loaded.isEmpty()) {
            droids.addAll(loaded);
            System.out.println("Завантажено " + droids.size() + " дроїдів з диску.");
        }

        seedExampleDroids();
        printWelcome();

        while (true) {
            printMenu();
            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1": createDroid(); break;
                case "2": listDroids(); break;
                case "3": battleOneOnOne(); break;
                case "4": battleTeamOnTeam(); break;
                case "5": saveDroids(); break;
                case "6": loadDroidsInteractive(); break;
                case "7": replayFromFile(); break;
                case "8": exportDroids(); break;
                case "9": deleteDroid(); break;
                case "10":
                    System.out.println("До зустрічі! Зберігаю дроїдів...");
                    DroidStorage.save("droids.dat", droids);
                    return;
                default: System.out.println("Невідома команда. Спробуйте ще раз.");
            }
        }
    }

    private static void printWelcome() {
        System.out.println("\u001B[36m=== Лабораторна робота №3: Битва дроїдів ===\u001B[0m\n");
    }

    private static void printMenu() {
        System.out.println("Меню:");
        System.out.println("1. Створити дроїда");
        System.out.println("2. Показати список створених дроїдів");
        System.out.println("3. Запустити бій 1 на 1");
        System.out.println("4. Запустити бій команда на команду");
        System.out.println("5. Зберегти створених дроїдів на диск");
        System.out.println("6. Завантажити створених дроїдів з диску");
        System.out.println("7. Відтворити проведений бій зі збереженого файлу");
        System.out.println("8. Експортувати створених дроїдів як текстовий файл");
        System.out.println("9. Видалити дроїда");
        System.out.println("10. Вийти");
        System.out.print("Виберіть команду: ");
    }

    private static void seedExampleDroids() {
        if (!droids.isEmpty()) return;
        AssaultDroid a = new AssaultDroid("A-Rescue");
        a.equip(new org.example.weapons.Laser(12, 0.9, 1));
        SniperDroid s = new SniperDroid("S-Longshot");
        s.equip(new org.example.weapons.Railgun(26, 0.85, 2));
        MedicDroid m = new MedicDroid("M-Healix");
        m.equip(new org.example.weapons.MedKit(0, 1.0, 0, 15));
        droids.add(a); droids.add(s); droids.add(m);
    }

    private static void createDroid() {
        System.out.println("Оберіть тип дроїда:");
        System.out.println("1 - Assault");
        System.out.println("2 - Sniper");
        System.out.println("3 - Medic");
        System.out.println("4 - Tank");
        System.out.print("Ваш вибір: ");
        String t = scanner.nextLine().trim();
        System.out.print("Ведіть ім'я: ");
        String name = scanner.nextLine().trim();
        Droid d;
        switch (t) {
            case "1": d = new AssaultDroid(name); break;
            case "2": d = new SniperDroid(name); break;
            case "3": d = new MedicDroid(name); break;
            case "4": d = new org.example.droids.TankDroid(name); break;
            default:
                System.out.println("Невідомий тип, створюю Assault за замовчуванням.");
                d = new AssaultDroid(name); break;
        }

        if (d.getEquipped() == null) {
            if (d instanceof SniperDroid) d.equip(new org.example.weapons.Railgun(26,0.85,2));
            else if (d instanceof MedicDroid) d.equip(new org.example.weapons.MedKit(0,1.0,0,12));
            else d.equip(new org.example.weapons.Laser(14,0.9,1));
        }
        droids.add(d);
        System.out.println("\u001B[32mДодано дроїда: " + d.shortInfo() + "\u001B[0m");
    }

    private static void listDroids() {
        if (droids.isEmpty()) { System.out.println("Немає створених дроїдів."); return; }
        System.out.println("Список дроїдів:");
        for (int i=0;i<droids.size();i++) {
            System.out.printf("%d) %s%n", i+1, droids.get(i).detailedInfo());
        }
    }

    private static Droid chooseDroid(String prompt) {
        listDroids();
        if (droids.isEmpty()) return null;
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        try {
            int idx = Integer.parseInt(s)-1;
            if (idx<0 || idx>=droids.size()) { System.out.println("Невірний індекс"); return null; }
            return droids.get(idx).clone();
        } catch (Exception e) { System.out.println("Невірний ввід"); return null; }
    }

    private static void battleOneOnOne() {
        System.out.println("--- Бій 1 на 1 ---");
        Droid a = chooseDroid("Виберіть першого дроїда (номер): "); if (a==null) return;
        Droid b = chooseDroid("Виберіть другого дроїда (номер): "); if (b==null) return;
        Arena arena = Arena.chooseInteractive();
        Strategy strategy = Strategy.chooseInteractive();
        BattleLogger logger = new BattleLogger();
        BattleEngine engine = new BattleEngine(arena, logger, strategy);
        BattleResult res = engine.duel(a,b);
        System.out.println("Результат: " + res.summary());
        logger.setResult(res);
        logger.flushToConsole();
        System.out.print("Зберегти лог бою у файл? (y/n): ");
        String save = scanner.nextLine().trim();
        if (save.equalsIgnoreCase("y")) {
            String filename = "battle_" + LocalDateTime.now().format(dtf) + ".log";
            try { logger.saveToFile(filename); System.out.println("Збережено у " + filename); } catch (IOException e) { System.out.println("Помилка запису: " + e.getMessage()); }
        }
        for (Droid d : droids) d.gainXP(10);
        System.out.println("Усі ваші дроїди отримали +10 XP.");
    }

    private static void battleTeamOnTeam() {
        System.out.println("--- Бій команда на команду ---");
        System.out.println("Створіть команду 1. Виберіть індекси дроїдів через пробіл (наприклад: 1 2 3):");
        listDroids(); if (droids.isEmpty()) return;
        System.out.print("Команда 1: ");
        Team t1 = readTeam();
        System.out.print("Команда 2: ");
        Team t2 = readTeam();
        if (t1==null || t2==null) return;
        Arena arena = Arena.chooseInteractive();
        Strategy strategy = Strategy.chooseInteractive();
        BattleLogger logger = new BattleLogger();
        BattleEngine engine = new BattleEngine(arena, logger, strategy);
        BattleResult res = engine.teamBattle(t1,t2);
        System.out.println("Результат: " + res.summary());
        logger.setResult(res);
        logger.flushToConsole();
        System.out.print("Зберегти лог бою у файл? (y/n): ");
        String save = scanner.nextLine().trim();
        if (save.equalsIgnoreCase("y")) {
            String filename = "battle_" + LocalDateTime.now().format(dtf) + ".log";
            try { logger.saveToFile(filename); System.out.println("Збережено у " + filename); } catch (IOException e) { System.out.println("Помилка запису: " + e.getMessage()); }
        }
        for (Droid d : droids) d.gainXP(10);
        System.out.println("Усі ваші дроїди отримали +10 XP.");
    }

    private static Team readTeam() {
        String line = scanner.nextLine().trim();
        String[] parts = line.split("\\s+");
        Team t = new Team("Team");
        for (String p : parts) {
            if (p.isEmpty()) continue;
            try {
                int idx = Integer.parseInt(p)-1;
                if (idx<0 || idx>=droids.size()) { System.out.println("Індекс " + (idx+1) + " пропущено (неіснує)"); continue; }
                t.add(droids.get(idx).clone());
            } catch (Exception e) { System.out.println("Не вдалося прочитати індекс: " + p); }
        }
        if (t.size()==0) { System.out.println("Команда порожня"); return null; }
        return t;
    }

    private static void saveDroids() {
        boolean ok = DroidStorage.save("droids.dat", droids);
        if (ok) System.out.println("Дроїди збережено у droids.dat");
        else System.out.println("Помилка збереження.");
    }

    private static void loadDroidsInteractive() {
        List<Droid> loaded = DroidStorage.load("droids.dat");
        if (loaded == null) System.out.println("Немає файлу або помилка.");
        else { droids.clear(); droids.addAll(loaded); System.out.println("Завантажено " + droids.size() + " дроїдів."); }
    }

    private static void replayFromFile() {
        System.out.print("Введіть ім'я файлу лога для відтворення: ");
        String fn = scanner.nextLine().trim();
        java.io.File f = new java.io.File(fn);
        if (!f.exists()) { System.out.println("Файл не знайдено"); return; }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f))) {
            System.out.println("--- Відтворення бою з файлу: " + fn + " ---");
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            }
            System.out.println("--- Відтворення завершено ---");
        } catch (java.io.IOException e) { System.out.println("Помилка: " + e.getMessage()); }
    }

    private static void exportDroids() {
        System.out.print("Введіть ім'я текстового файлу (наприклад my_droids.txt): ");
        String fn = scanner.nextLine().trim();
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(fn))) {
            for (Droid d : droids) pw.println(d.detailedInfo());
            System.out.println("Експортовано до " + fn);
        } catch (java.io.IOException e) { System.out.println("Помилка експорту: " + e.getMessage()); }
    }

    private static void deleteDroid() {
        if (droids.isEmpty()) {
            System.out.println("Немає дроїдів для видалення.");
            return;
        }

        System.out.println("Список дроїдів:");
        for (int i = 0; i < droids.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, droids.get(i).detailedInfo());
        }

        System.out.print("Введіть номер дроїда, якого хочете видалити: ");
        String input = scanner.nextLine().trim();

        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= droids.size()) {
                System.out.println("Невірний індекс.");
                return;
            }

            Droid removed = droids.remove(idx);
            System.out.println("Видалено: " + removed.shortInfo());

            // автоматично перезаписуємо файл
            boolean ok = DroidStorage.save("droids.dat", droids);
            if (ok) System.out.println("Файл droids.dat оновлено.");
            else System.out.println("Помилка збереження.");

        } catch (NumberFormatException e) {
            System.out.println("Невірний ввід.");
        }
    }

}
