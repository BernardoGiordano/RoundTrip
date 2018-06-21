package it.roundtrip.pojo;

import java.io.Serializable;

public class City implements Serializable {
    private String key;
    private String name;
    private String notes;
    private int cooldown;
    private int delay;
    private boolean repeat;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public City() { }

    public City(String key, String name, int cooldown) {
        this.key = key;
        this.name = name;
        this.cooldown = cooldown;
        this.notes = "";
        this.delay = 0;
        this.repeat = true;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getCooldown() {

        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
