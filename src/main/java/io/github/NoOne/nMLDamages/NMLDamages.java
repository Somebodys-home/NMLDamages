package io.github.NoOne.nMLDamages;

import io.github.NoOne.nMLMobs.NMLMobs;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMLDamages extends JavaPlugin {
    private static NMLDamages instance;
    private ProfileManager profileManager;
    private MobStatsYMLManager mobStatsYMLManager;
    private CustomDamager customDamager;
    private DamageHelper damageHelper;

    @Override
    public void onEnable() {
        instance = this;
        profileManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getProfileManager();
        mobStatsYMLManager = JavaPlugin.getPlugin(NMLMobs.class).getMobStatsYMLManager();

        customDamager = new CustomDamager(this);
        damageHelper = new DamageHelper();
        DamageHologramGenerator.startDamageDisplayTask();

        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
    }

    public static NMLDamages getInstance() {
        return instance;
    }

    public CustomDamager getCustomDamager() {
        return customDamager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public DamageHelper getDamageManager() {
        return damageHelper;
    }

    public MobStatsYMLManager getMobStatsYMLManager() {
        return mobStatsYMLManager;
    }
}
