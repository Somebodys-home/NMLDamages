package io.github.NoOne.nMLDamages;

import io.github.NoOne.nMLDamages.event.DamageListener;
import io.github.NoOne.nMLMobs.NMLMobs;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.StatConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMLDamages extends JavaPlugin {
    private static NMLDamages instance;
    private StatConfigManager statConfigManager;
    private MobStatsYMLManager mobStatsYMLManager;

    @Override
    public void onEnable() {
        instance = this;
        statConfigManager = JavaPlugin.getPlugin(NMLPlayerStats.class).getStatConfigManager();
        mobStatsYMLManager = JavaPlugin.getPlugin(NMLMobs.class).getMobStatsYMLManager();

        DamageHologramGenerator.startDamageDisplayTask();

        getServer().getPluginManager().registerEvents(new DamageListener(), this);
    }

    public static NMLDamages getInstance() {
        return instance;
    }

    public StatConfigManager getStatConfigManager() {
        return statConfigManager;
    }

    public MobStatsYMLManager getMobStatsYMLManager() {
        return mobStatsYMLManager;
    }
}
