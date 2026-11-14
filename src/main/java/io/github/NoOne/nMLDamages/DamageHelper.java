package io.github.NoOne.nMLDamages;

import io.github.NoOne.nMLDamages.enums.DamageType;
import io.github.NoOne.nMLItems.enums.ItemStat;
import io.github.NoOne.nMLPlayerStats.statSystem.PlayerStat;
import org.bukkit.entity.LivingEntity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DamageHelper {
    // returns the sum of all the damages on a map
    public static double getTotalDamage(HashMap<DamageType, ? extends Number> damage) {
        double totalDamage = 0;

        for (Map.Entry<DamageType, ? extends Number> entry : damage.entrySet()) {
            totalDamage += entry.getValue().doubleValue();
        }

        return totalDamage;
    }

    // returns a converted hashmap of item stats to a map of damage types
    public static HashMap<DamageType, Double> convertItemStatsToDamages(HashMap<ItemStat, Double> statMap) {
        return new HashMap<>(){{
            for (Map.Entry<ItemStat, Double> entry : statMap.entrySet()) {
                DamageType damageType = entry.getKey().toDamageType();

                if (damageType != null) {
                    put(damageType, entry.getValue());
                }
            }
        }};
    }

    // returns a converted hashmap of damage types to a map of item stats
    public static HashMap<ItemStat, ? extends Number> convertDamagesToItemStats(HashMap<DamageType, ? extends Number> statMap) {
        return new HashMap<>(){{
            for (Map.Entry<DamageType, ? extends Number> entry : statMap.entrySet()) {
                put(entry.getKey().toItemStat(), entry.getValue());
            }
        }};
    }

    // returns a converted hashmap of player stats to damage types
    public static HashMap<DamageType, Double> convertPlayerStatsToDamage(HashMap<PlayerStat, ? extends Number> statMap) {
        return new HashMap<>(){{
            for (Map.Entry<PlayerStat, ? extends Number> entry : statMap.entrySet()) {
                put(entry.getKey().toItemStat().toDamageType(), entry.getValue().doubleValue());
            }
        }};
    }

    // returns a converted hashmap of strings to damage types
    public static HashMap<DamageType, ? extends Number> convertStringsToDamageTypes(HashMap<String, ? extends Number> statMap) {
        return new HashMap<>(){{
            for (Map.Entry<String, ? extends Number> entry : statMap.entrySet()) {
                put(DamageType.fromString(entry.getKey()), entry.getValue());
            }
        }};
    }

    // returns a map of all the values, multiplied by a given multiplier
    public static HashMap<DamageType, Double> multiplyDamageMap(HashMap<DamageType, ? extends Number> damageMap, double multiplier) {
        HashMap<DamageType, Double> result = new HashMap<>();

        damageMap.forEach((type, value) -> result.put(type, value.doubleValue() * multiplier));
        return result;
    }

    // returns a linked hashmap of the given hashmap, but with its damages sorted by highest to lowest
    public static LinkedHashMap<DamageType, Double> sortDamagesByHighest(HashMap<DamageType, Double> damage) {
        return damage.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    public static boolean isMobDamageable(LivingEntity livingEntity) {
        return !livingEntity.hasMetadata("hologram") && !livingEntity.hasMetadata("garden_crop") &&
                !livingEntity.hasMetadata("invincible") && livingEntity.getNoDamageTicks() <= 0;
    }
}