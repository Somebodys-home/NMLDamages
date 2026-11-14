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

        if (!DamageHelper.isMobDamageable(damaged) || event.getDamage().isEmpty() || damaged.isDead() || damager.isDead()) return;

        switch (damagerSource) {
            case PLAYER -> {
                switch (damagedSource) {
                    case PLAYER -> CustomDamager.playerDamagePlayer((Player) damager, (Player) damaged, damage, noDamageTicks);
                    case MOB -> CustomDamager.playerDamageMob((Player) damager, damaged, damage, noDamageTicks);
                    case ENVIRONMENT -> {
                        // pluh
                    }
                }
            }
        }
//        if (event.isMobDamager()) {
//            if (target instanceof Player player) {
//                Stats targetStats = profileManager.getPlayerProfile(player.getUniqueId()).getStats();
//                int evasion = targetStats.getEvasion();
//                int random = (int) (Math.random() * 100) + 1;
//
//                if (random <= evasion || evasion >= 100) { // evasion
//                    player.sendMessage("your evasion triggered! (" + evasion + "%)");
//                    event.setCancelled(true);
//                    return;
//                }
//
//                customDamager.doDamage(player, event.getDamager(), event.getDamage(), true, event.getNoDamageTicks());
//            }
//        } else { // player is damager
//            if (!target.equals(event.getDamager())) {
//                customDamager.doDamage(target, event.getDamager(), event.getDamage(), false, event.getNoDamageTicks());
//            }
//        }
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