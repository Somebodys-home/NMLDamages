package io.github.NoOne.nMLDamages;

import io.github.NoOne.nMLDamages.event.DamageListener;
import io.github.NoOne.nMLMobs.NMLMobs;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMLDamages extends JavaPlugin {
    private static NMLDamages instance;
    private ProfileManager profileManager;
    private MobStatsYMLManager mobStatsYMLManager;

    @Override
    public void onEnable() {
        instance = this;
        profileManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getProfileManager();
        mobStatsYMLManager = JavaPlugin.getPlugin(NMLMobs.class).getMobStatsYMLManager();

        DamageHologramGenerator.startDamageDisplayTask();

        getServer().getPluginManager().registerEvents(new DamageListener(), this);
    }

    public static NMLDamages getInstance() {
        return instance;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public MobStatsYMLManager getMobStatsYMLManager() {
        return mobStatsYMLManager;
    }
}
