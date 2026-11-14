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
        return this.toItemStat().toChatColor();
    }

    public String toEmoji() {
        return this.toItemStat().toEmoji();
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

    public static DamageType fromString(String string) {
        return switch (string) {
            case "Physical" -> PHYSICAL;
            case "Fire" -> FIRE;
            case "Cold" -> COLD;
            case "Earth" -> EARTH;
            case "Lightning" -> LIGHTNING;
            case "Air" -> AIR;
            case "Radiant" -> RADIANT;
            case "Necrotic" -> NECROTIC;
            case "Pure" -> PURE;
            default -> null;
        };
    }
}
