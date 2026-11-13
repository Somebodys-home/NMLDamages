package io.github.NoOne.nMLDamages.enums;

import io.github.NoOne.nMLItems.enums.ItemStat;

public enum DamageType {
    PHYSICAL,
    FIRE,
    COLD,
    EARTH,
    LIGHTNING,
    AIR,
    RADIANT,
    NECROTIC,
    PURE;

    @Override
    public String toString() {
        return switch (this) {
            case PHYSICAL -> "Physical";
            case FIRE -> "Fire";
            case COLD -> "Cold";
            case EARTH -> "Earth";
            case LIGHTNING -> "Lightning";
            case AIR -> "Air";
            case RADIANT -> "Radiant";
            case NECROTIC -> "Necrotic";
            case PURE -> "Pure";
        };
    }

    public String toChatColor() {
        return switch (this) {
            case PHYSICAL -> "§4";
            case FIRE -> "§c";
            case COLD -> "§b";
            case EARTH -> "§2";
            case LIGHTNING -> "§e";
            case AIR -> "§7";
            case RADIANT, PURE -> "§f";
            case NECROTIC -> "§5";
        };
    }

    public String toEmoji() {
        return switch (this) {
            case PHYSICAL -> "⚔";
            case FIRE -> "\uD83D\uDD25";
            case COLD -> "❄";
            case EARTH -> "\uD83E\uDEA8";
            case LIGHTNING -> "\uD83D\uDDF2";
            case AIR -> "☁";
            case RADIANT -> "✦";
            case NECROTIC -> "\uD83C\uDF00";
            case PURE -> "\uD83D\uDCA2";
        };
    }

    public ItemStat toItemStat() {
        return switch (this) {
            case PHYSICAL -> ItemStat.PHYSICAL_DAMAGE;
            case FIRE -> ItemStat.FIRE_DAMAGE;
            case COLD -> ItemStat.COLD_DAMAGE;
            case EARTH -> ItemStat.EARTH_DAMAGE;
            case LIGHTNING -> ItemStat.LIGHTNING_DAMAGE;
            case AIR -> ItemStat.AIR_DAMAGE;
            case RADIANT -> ItemStat.RADIANT_DAMAGE;
            case NECROTIC -> ItemStat.NECROTIC_DAMAGE;
            case PURE -> ItemStat.PURE_DAMAGE;
        };
    }

    public static DamageType fromItemStat(ItemStat stat) {
        return switch (stat) {
            case PHYSICAL_DAMAGE -> PHYSICAL;
            case FIRE_DAMAGE -> FIRE;
            case COLD_DAMAGE -> COLD;
            case EARTH_DAMAGE -> EARTH;
            case LIGHTNING_DAMAGE -> LIGHTNING;
            case AIR_DAMAGE -> AIR;
            case RADIANT_DAMAGE -> RADIANT;
            case NECROTIC_DAMAGE -> NECROTIC;
            case PURE_DAMAGE -> PURE;
            default -> null;
        };
    }
}
