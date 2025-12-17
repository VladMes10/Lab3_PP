package org.example.droids;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {
    private String name;
    private List<Droid> members = new ArrayList<>();

    public Team(String name) { this.name = name; }
    public void add(Droid d) { members.add(d); }
    public int size() { return members.size(); }
    public boolean hasAlive() { for (Droid d : members) if (d.isAlive()) return true; return false; }
    public List<Droid> getAlive() { List<Droid> r = new ArrayList<>(); for (Droid d : members) if (d.isAlive()) r.add(d); return r; }
    public List<Droid> getMembers() { return members; }
    public String getName() { return name; }
}
