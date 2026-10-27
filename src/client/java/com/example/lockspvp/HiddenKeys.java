package com.example.lockspvp;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

public class HiddenKeys {
    private static final Set<String> HIDDEN = new TreeSet<>();
    private static final Path FILE =
        FabricLoader.getInstance().getConfigDir().resolve("lockspvp-hidden.txt");

    // false = hidden keys are hidden, true = lock open, everything shown
    public static boolean showHidden = false;

    static {
        load();
    }

    public static boolean isHidden(String keyName) {
        return HIDDEN.contains(keyName);
    }

    public static void toggle(String keyName) {
        if (!HIDDEN.remove(keyName)) {
            HIDDEN.add(keyName);
        }
        save();
    }

    public static Component label(String keyName) {
        return Component.literal(isHidden(keyName) ? "Unhide" : "Hide");
    }

    private static void load() {
        try {
            if (Files.exists(FILE)) {
                for (String line : Files.readAllLines(FILE)) {
                    String s = line.trim();
                    if (!s.isEmpty()) HIDDEN.add(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save() {
        try {
            Files.write(FILE, HIDDEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
