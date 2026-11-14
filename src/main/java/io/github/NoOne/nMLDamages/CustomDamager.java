package io.github.NoOne.nMLDamages;

import io.github.NoOne.nMLDamages.enums.DamageType;
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
        MobStats mobStats = mobStatsYMLManager.getMobStatsFromYml(mob.getName());

        if (!passesEvasionCheck(mobStats.getIntStatValue(PlayerStat.EVASION))) {
            return;
        }

        applyElementalDamage(damage, damagerStats);
        applyElementalResistances(damage, mobStats);
        criticalHitCheck(damage, damagerStats);

        mob.damage(DamageHelper.getTotalDamage(damage));
        mob.setNoDamageTicks(noDamageTicks);
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

//    public static void doDamage(LivingEntity target, LivingEntity attacker, Map<DamageType, Double> damageSplits, boolean isMobDamager, int noDamageTicks) {
//        Stats targetStats = profileManager.getStats(target.getUniqueId());
//        Stats attackerStats = profileManager.getStats(target.getUniqueId());
//
//        // sorting the damage map from highest to lowest
//        damageSplits = damageSplits.entrySet().stream()
//                .sorted(Map.Entry.<DamageType, Double>comparingByValue().reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                )
//        );
//
//        if (attackerStats != null) { // if attacker has stats, apply raw elemental damage to damage map
//            int elementalDamage = attackerStats.getElementalDamage();
//            Map.Entry<DamageType, Double> highestDamage = damageSplits.entrySet().iterator().next();
//
//            // elemental damage doesn't apply to physical or pure damage
//            if (highestDamage.getKey() != DamageType.PHYSICAL && highestDamage.getKey() != DamageType.PURE) {
//                highestDamage.setValue(highestDamage.getValue() + elementalDamage);
//            }
//
//            damageSplits.put(highestDamage.getKey(), highestDamage.getValue());
//        }
//
//        if (targetProfile == null) { // if target doesn't have stats, just apply damage now
//            applyDamage(target, attacker, damageSplits, noDamageTicks);
//            return;
//        }
//
//        HashMap<ItemStat, Integer> resistedTypes = new HashMap<>();
//
//        // elemental resistances
//        if (targetStats.getDefense() != 0) resistedTypes.put(DEFENSE, targetStats.getDefense());
//        if (targetStats.getPhysicalResist() != 0) resistedTypes.put(PHYSICALRESIST, targetStats.getPhysicalResist());
//        if (targetStats.getFireResist() != 0) resistedTypes.put(FIRERESIST, targetStats.getFireResist());
//        if (targetStats.getColdResist() != 0) resistedTypes.put(COLDRESIST, targetStats.getColdResist());
//        if (targetStats.getEarthResist() != 0) resistedTypes.put(EARTHRESIST, targetStats.getEarthResist());
//        if (targetStats.getLightningResist() != 0) resistedTypes.put(LIGHTNINGRESIST, targetStats.getLightningResist());
//        if (targetStats.getAirResist() != 0) resistedTypes.put(AIRRESIST, targetStats.getAirResist());
//        if (targetStats.getRadiantResist() != 0) resistedTypes.put(RADIANTRESIST, targetStats.getRadiantResist());
//        if (targetStats.getNecroticResist() != 0) resistedTypes.put(NECROTICRESIST, targetStats.getNecroticResist());
//
//        if (!resistedTypes.isEmpty()) { // if target has any resistances
//            for (Map.Entry<DamageType, Double> entry : damageSplits.entrySet()) { // for every damage type
//                double value = entry.getValue();
//                double damageReductionPercent = 0;
//
//                if (entry.getKey() != DamageType.PURE) { // apply defense reduction on everything except pure damage
//                    damageReductionPercent += resistedTypes.getOrDefault(DEFENSE, 0) * .5; // .5% : 1
//                }
//
//                switch (entry.getKey()) { // then go into elemental resistances (1% : 1)
//                    case PHYSICAL -> damageReductionPercent += resistedTypes.getOrDefault(PHYSICALRESIST, 0);
//                    case FIRE -> damageReductionPercent += resistedTypes.getOrDefault(FIRERESIST, 0);
//                    case COLD -> damageReductionPercent += resistedTypes.getOrDefault(COLDRESIST, 0);
//                    case EARTH -> damageReductionPercent += resistedTypes.getOrDefault(EARTHRESIST, 0);
//                    case LIGHTNING -> damageReductionPercent += resistedTypes.getOrDefault(LIGHTNINGRESIST, 0);
//                    case AIR -> damageReductionPercent += resistedTypes.getOrDefault(AIRRESIST, 0);
//                    case RADIANT -> damageReductionPercent += resistedTypes.getOrDefault(RADIANTRESIST, 0);
//                    case NECROTIC -> damageReductionPercent += resistedTypes.getOrDefault(NECROTICRESIST, 0);
//                }
//
//                value = Math.max(0, Math.round(value * (1 - damageReductionPercent)));
//                entry.setValue(value);
//            }
//        }
//
//        // if the damager is a mob
//        if (isMobDamager) {
//            MobStats mobStats = mobStatsYMLManager.getMobStatsFromYml(attacker.getName());
//            applyDamageFromMob(target, attacker, mobStats, damageSplits);
//        } else {
//            applyDamage(target, attacker, damageSplits, noDamageTicks);
//        }
//    }
//
//    private static void applyDamage(LivingEntity target, LivingEntity attacker, Map<DamageType, Double> damageSplits, int noDamageTicks) {
//        Map<DamageType, Double> effectiveDamageSplits = new EnumMap<>(damageSplits); // shallow copy for enum keys
//        Stats damagerStats = profileManager.getPlayerProfile(attacker.getUniqueId()).getStats();
//        double totalDamage = 0;
//        boolean critHit = false;
//
//        // crit chance
//        if (damagerStats != null) critHit = ThreadLocalRandom.current().nextDouble(1, 100) <= damagerStats.getCritChance();
//        if (critHit) {
//            double critMultiplier = damagerStats.getCritDamage() / 10stats.getStatValue(PlayerStat.COLD_RESIST);
//
//            effectiveDamageSplits.replaceAll((_, v) -> v * critMultiplier);
//            attacker.getWorld().playSound(target, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2f, 1f);
//            attacker.getWorld().playSound(target, Sound.ENTITY_PLAYER_ATTACK_CRIT, 2f, 1f);
//        }
//
//        for (Map.Entry<DamageType, Double> entry : effectiveDamageSplits.entrySet()) {
//           totalDamage += entry.getValue();
//        }
//
//        target.damage(totalDamage, attacker);
//        target.setNoDamageTicks(noDamageTicks);
//        DamageHologramGenerator.createDamageHologram(attacker, target, effectiveDamageSplits, critHit);
//    }
//
//    private static void applyDamageFromMob(LivingEntity target, LivingEntity attacker, MobStats mobStats, Map<DamageType, Double> damageSplits) {
//        Map<DamageType, Double> effectiveDamageSplits = new EnumMap<>(damageSplits); // shallow copy for enum keys
//        double totalDamage = 0;
//        boolean critHit = false;
//
//        // critical hit case
//        if (mobStats != null) critHit = ThreadLocalRandom.current().nextDouble(1, 100) <= mobStats.getCritChance();
//        if (critHit) {
//            double critMultiplier = mobStats.getCritDamage() / 10stats.getStatValue(PlayerStat.COLD_RESIST);
//
//            effectiveDamageSplits.replaceAll((k, v) -> v * critMultiplier);
//        }
//
//        for (double damage : effectiveDamageSplits.values()) {
//            totalDamage += damage;
//        }
//
//        // knockback
//        Vector knockbackDirection = target.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize();
//        Vector knockback = knockbackDirection.multiply(.4).setY(.35);
//
//        target.damage(totalDamage);
//        target.setVelocity(knockback);
//    }
}