package io.github.NoOne.nMLDamages;

import org.bukkit.entity.LivingEntity;

public class DamageHelper {
//    public static HashMap<DamageType, Double> convertItemStatsToDamageTypes(HashMap<ItemStat, Double> statMap) {
//        return new HashMap<>(){{
//            for (Map.Entry<ItemStat, Double> entry : statMap.entrySet()) {
//                DamageType damageType = entry.getKey().toDamageType();
//
//                if (damageType != null) {
//                    put(damageType, entry.getValue());
//                }
//            }
//        }};
//    }
//
//    public static HashMap<ItemStat, Double> convertDamagesToItemStats(HashMap<DamageType, Double> statMap) {
//        HashMap<ItemStat, Double> damageMap = new HashMap<>();
//
//        for (Map.Entry<DamageType, Double> entry : statMap.entrySet()) {
//            damageMap.put(convertDamageType2Stat(entry.getKey()), entry.getValue());
//        }
//
//        return new HashMap<>(){{
//            for (Map.Entry<>)
//        }};
//    }
//
//    public static HashMap<DamageType, Double> convertPlayerStats2Damage(Stats stats) {
//        HashMap<DamageType, Double> damageMap = new HashMap<>();
//        HashMap<DamageType, Double> highestElementalDamage = new HashMap<>();
//
//        if (!stats.getHighestElementalDamage().isEmpty()) {
//            Map.Entry<String, Integer> finalEntry = stats.getHighestElementalDamage().entrySet().iterator().next();
//            highestElementalDamage = new HashMap<>(){{
//                put(convertString2DamageType(finalEntry.getKey()), Double.valueOf(finalEntry.getValue()) + stats.getElementalDamage());
//            }};
//        }
//
//        for (Map.Entry<String, Integer> statEntry : stats.getAllDamages().entrySet()) {
//            damageMap.put(convertString2DamageType(statEntry.getKey()), statEntry.getValue().doubleValue());
//        }
//
//        damageMap.putAll(highestElementalDamage);
//
//        return damageMap;
//    }
//
//    public static HashMap<DamageType, Double> convertPlayerStat2Damage(Stats stats, String stat) {
//        HashMap<DamageType, Double> damage = new HashMap<>();
//
//        for (Map.Entry<String, Integer> damageEntry : stats.getAllDamages().entrySet()) {
//            if (Objects.equals(damageEntry.getKey(), stat)) {
//                damage.put(convertString2DamageType(damageEntry.getKey()), damageEntry.getValue().doubleValue());
//                return damage;
//            }
//        }
//
//        return damage;
//    }
//
//    public static HashMap<DamageType, Double> multiplyDamageMap(HashMap<DamageType, Double> damageMap, double multiplier) {
//        HashMap<DamageType, Double> copy =  new HashMap<>(damageMap);
//
//        for (Map.Entry<DamageType, Double> damageEntry : copy.entrySet()) {
//            damageEntry.setValue(damageEntry.getValue() * multiplier);
//        }
//
//        return copy;
//    }
//
//    public static HashMap<DamageType, Double> convertStringMap2DamageTypes(HashMap<String, Double> statMap) {
//        HashMap<DamageType, Double> damageMap = new HashMap<>();
//
//        for (Map.Entry<String, Double> entry : statMap.entrySet()) {
//            damageMap.put(convertString2DamageType(entry.getKey()), entry.getValue());
//        }
//
//        return damageMap;
//    }
//
//    public static HashMap<DamageType, Double> convertStringIntMap2DamageTypes(HashMap<String, Integer> statMap) {
//        HashMap<DamageType, Double> damageMap = new HashMap<>();
//
//        for (Map.Entry<String, Integer> entry : statMap.entrySet()) {
//            damageMap.put(convertString2DamageType(entry.getKey()), entry.getValue().doubleValue());
//        }
//
//        return damageMap;
//    }

    public static boolean isMobDamageable(LivingEntity livingEntity) {
        return !livingEntity.hasMetadata("hologram") && !livingEntity.hasMetadata("garden_crop") &&
                !livingEntity.hasMetadata("invincible") && livingEntity.getNoDamageTicks() <= 0;
    }
}