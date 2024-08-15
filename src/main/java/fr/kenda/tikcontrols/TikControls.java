package fr.kenda.tikcontrols;

import fr.kenda.tikcontrols.managers.Managers;
import fr.kenda.tikcontrols.utils.PluginVersionning;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class TikControls extends JavaPlugin {

    public static final String PREFIX = "&f[&cTikControls&f]&r ";
    private static TikControls instance;

    public static TikControls getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        new Managers();
        try {
            PluginVersionning versionning = new PluginVersionning();
            if (!versionning.isLatestVersion())
                versionning.sendNewVersionAlert();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {

    }
}
