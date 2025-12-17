package org.example.save;


import org.example.droids.Droid;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DroidStorage {
    @SuppressWarnings("unchecked")
    public static List<Droid> load(String filename) {
        File f = new File(filename);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof List) return (List<Droid>) obj;
        } catch (Exception e) {
            System.out.println("Помилка під час завантаження: " + e.getMessage());
        }
        return null;
    }

    public static boolean save(String filename, List<Droid> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
            return true;
        } catch (IOException e) {
            System.out.println("Помилка під час збереження: " + e.getMessage());
            return false;
        }
    }
}
