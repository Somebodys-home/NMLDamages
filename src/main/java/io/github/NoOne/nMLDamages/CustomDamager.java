package io.github.NoOne.nMLDamages;

import io.github.NoOne.nMLDamages.enums.DamageType;
import io.github.NoOne.nMLMobs.NMLMobSystem;
import io.github.NoOne.nMLMobs.mobs.NMLMob;
import io.github.NoOne.nMLMobs.mobstats.MobStats;
import io.github.NoOne.nMLMobs.mobstats.MobStatsYMLManager;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.PlayerStat;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class CustomDamager {
    private static ProfileManager profileManager = NMLDamages.getInstance().getProfileManager();
    private static MobStatsYMLManager mobStatsYMLManager = NMLDamages.getInstance().getMobStatsYMLManager();

    public static void playerDamagePlayer(Player damager, Player damaged, HashMap<DamageType, Double> damage, int noDamageTicks) {
        Stats damagerStats = profileManager.getStats(damager.getUniqueId());
        Stats damagedStats = profileManager.getStats(damaged.getUniqueId());

        if (!passesEvasionCheck(damagedStats.getIntStatValue(PlayerStat.EVASION))) {
            return;
        }

        applyElementalDamage(damage, damagerStats);
        applyElementalResistances(damage, damagedStats);
        criticalHitCheck(damage, damagerStats);

        damaged.damage(DamageHelper.getTotalDamage(damage));
        damaged.setNoDamageTicks(noDamageTicks);
    }

    public static void playerDamageMob(Player damager, LivingEntity mob, HashMap<DamageType, Double> damage, int noDamageTicks) {
        Stats damagerStats = profileManager.getStats(damager.getUniqueId());

        applyElementalDamage(damage, damagerStats);

        if (NMLMobSystem.isNMLMob(mob)) {
            NMLMob nmlMob = NMLMobSystem.getNmlMob(mob);
            MobStats mobStats = mobStatsYMLManager.getMobStatsFromYml(mob.getName());

            if (!passesEvasionCheck(mobStats.getIntStatValue(PlayerStat.EVASION))) {
                return;
            }

            applyElementalResistances(damage, mobStats);
            criticalHitCheck(damage, damagerStats);

            mob.damage(DamageHelper.getTotalDamage(damage));
            mob.setNoDamageTicks(0);
            nmlMob.addAttacker(damager, noDamageTicks);
        } else {
            criticalHitCheck(damage, damagerStats);
            mob.damage(DamageHelper.getTotalDamage(damage));
            mob.setNoDamageTicks(noDamageTicks);
        }

    }

    // applies the stats' elemental damage to the highest damage on the damage map
    private static void applyElementalDamage(HashMap<DamageType, Double> damage, Stats stats) {
        DamageType highestDamage = DamageHelper.sortDamagesByHighest(damage).keySet().stream().toList().getFirst();

        if (highestDamage != DamageType.PHYSICAL) {
            damage.replace(highestDamage, damage.get(highestDamage) + stats.getStatValue(PlayerStat.ELEMENTAL_DAMAGE));
        }
    }

    // applies the stats' elemental resistances to the given damage map
    private static void applyElementalResistances(HashMap<DamageType, Double> damage, Stats stats) {
        double defense = stats.getStatValue(PlayerStat.DEFENSE);
        
        for (Map.Entry<DamageType, Double> entry : damage.entrySet()) {
            double resistanceMultiplier = 100.0;
            double elementalResist = switch (entry.getKey()) {
                case PHYSICAL -> stats.getStatValue(PlayerStat.PHYSICAL_RESIST);
                case FIRE -> stats.getStatValue(PlayerStat.FIRE_RESIST);
                case COLD -> stats.getStatValue(PlayerStat.COLD_RESIST);
                case EARTH -> stats.getStatValue(PlayerStat.EARTH_RESIST);
                case LIGHTNING -> stats.getStatValue(PlayerStat.LIGHTNING_RESIST);
                case AIR -> stats.getStatValue(PlayerStat.AIR_RESIST);
                case RADIANT -> stats.getStatValue(PlayerStat.RADIANT_RESIST);
                case NECROTIC -> stats.getStatValue(PlayerStat.NECROTIC_RESIST);
                case PURE -> 0;
            };

            resistanceMultiplier -= (defense / 2) + elementalResist;
            damage.replace(entry.getKey(), entry.getValue() * resistanceMultiplier / 100);
        }
    }

    // runs a check on if the attack critically hit, and applies the multiplied damage to the map if it did
    private static void criticalHitCheck(HashMap<DamageType, Double> damage, Stats stats) {
        int critChance = stats.getIntStatValue(PlayerStat.CRIT_CHANCE);

        if (passesCritCheck(critChance)) {
            double critMultiplier = 1 + (stats.getStatValue(PlayerStat.CRIT_DAMAGE) / 100);

            for (Map.Entry<DamageType, Double> damageEntry : damage.entrySet()) {
                damageEntry.setValue(damageEntry.getValue() * critMultiplier);
            }
        }
    }

    // returns true/false if a random int between 1 - 100 is less than or equal to the given evasion stat
    private static boolean passesEvasionCheck(int evasionStat) {
        return ThreadLocalRandom.current().nextInt(1, 100) <= evasionStat;
    }

    // returns true/false if a random int between 1 - 100 is less than or equal to the given crit chance stat
    private static boolean passesCritCheck(int critChanceStat) {
        return ThreadLocalRandom.current().nextInt(1, 100) <= critChanceStat;
    }
}