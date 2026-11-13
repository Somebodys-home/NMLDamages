package io.github.NoOne.nMLDamages.enums;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public enum DamageSource {
    PLAYER,
    MOB,
    ENVIRONMENT;

    // returns if the living entity's damage source class is of PLAYER or MOB
    public static DamageSource toDamageSource(LivingEntity livingEntity) {
        if (livingEntity instanceof Player) {
            return PLAYER;
        } else {
            return MOB;
        }
    }
}
