package io.github.NoOne.nMLDamages.event;

import io.github.NoOne.nMLDamages.CustomDamager;
import io.github.NoOne.nMLDamages.DamageHelper;
import io.github.NoOne.nMLDamages.enums.DamageSource;
import io.github.NoOne.nMLDamages.enums.DamageType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void doCustomDamage(CustomDamageEvent event) {
        LivingEntity damager = event.getDamager();
        LivingEntity damaged = event.getDamaged();
        DamageSource damagerSource = event.getDamagerSource();
        DamageSource damagedSource = event.getDamagedSource();
        HashMap<DamageType, Double> damage = event.getDamage();
        int noDamageTicks = event.getNoDamageTicks();

        if (!DamageHelper.isMobDamageable(damaged) || event.getDamage().isEmpty() || damaged.isDead()) return;

        switch (damagerSource) {
            case PLAYER -> {
                switch (damagedSource) {
                    case PLAYER -> CustomDamager.playerDamagePlayer((Player) damager, (Player) damaged, damage, noDamageTicks);
                    case MOB -> CustomDamager.playerDamageMob((Player) damager, damaged, damage, noDamageTicks);
                }
            }
            case MOB ->  {
                switch (damagedSource) {
                    case PLAYER -> CustomDamager.mobDamagePlayer(damager, (Player) damaged, damage, noDamageTicks);
                    case MOB -> CustomDamager.mobDamageMob(damager, damaged, damage, noDamageTicks);
                }
            }
            case ENVIRONMENT ->  {
                switch (damagedSource) {
                    case PLAYER, MOB -> CustomDamager.damageLivingEntity(damaged, damage, noDamageTicks);
                }
            }
        }
    }

    @EventHandler
    public void noFallDamage(EntityDamageEvent event) {
        if (event.getEntity().hasMetadata("no_fall_damage") && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void noFireworkDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework firework) {
            if (firework.hasMetadata("no_damage")) {
                event.setCancelled(true);
            }
        }
    }
}