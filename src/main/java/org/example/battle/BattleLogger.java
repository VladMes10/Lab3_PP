package org.example.battle;

import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BattleLogger implements Serializable {
    private List<String> events = new ArrayList<>();
    private BattleResult result;
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void log(String s) { events.add(String.format("[%s] %s", LocalDateTime.now().format(df), s)); }
    public void setResult(BattleResult r) { this.result = r; }
    public void flushToConsole() { for (String e : events) System.out.println(e); if (result != null) System.out.println("--> " + result.summary()); }
    public void saveToFile(String filename) throws java.io.IOException {
        try (PrintWriter pw = new PrintWriter(new java.io.FileWriter(filename))) {
            for (String e : events) pw.println(e);
            if (result != null) pw.println("RESULT: " + result.summary());
        }
    }
}
