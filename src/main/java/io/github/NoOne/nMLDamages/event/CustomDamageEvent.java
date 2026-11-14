package io.github.NoOne.nMLDamages.event;

import io.github.NoOne.nMLDamages.DamageHelper;
import io.github.NoOne.nMLDamages.enums.DamageSource;
import io.github.NoOne.nMLDamages.enums.DamageType;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class CustomDamageEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private LivingEntity damaged;
    private LivingEntity damager;
    private DamageSource damagedSource; // the class that the damaged falls into
    private DamageSource damagerSource; // the class that the damager fals into
    private HashMap<DamageType, Double> damage;
    private boolean cancelled;
    private int noDamageTicks;

    public CustomDamageEvent(LivingEntity damaged, LivingEntity damager, HashMap<DamageType, Double> damage) {
        this.damaged = damaged;
        this.damager = damager;
        this.damage = damage;
        noDamageTicks = 0;
        cancelled = false;
        damagedSource = DamageSource.toDamageSource(damaged);
        damagerSource = DamageSource.toDamageSource(damager);
    }

    public CustomDamageEvent(LivingEntity damaged, LivingEntity damager, HashMap<DamageType, Double> damage, int noDamageTicks) {
        this.damaged = damaged;
        this.damager = damager;
        this.damage = damage;
        this.noDamageTicks = noDamageTicks;
        cancelled = false;
        damagedSource = DamageSource.toDamageSource(damaged);
        damagerSource = DamageSource.toDamageSource(damager);
    }

    public CustomDamageEvent(LivingEntity damaged, LivingEntity damager, Stats damagerStats) {
        this.damaged = damaged;
        this.damager = damager;
        this.damage = (HashMap<DamageType, Double>) DamageHelper.convertPlayerStatsToDamage(damagerStats.getAllDamages());
        noDamageTicks = 0;
        cancelled = false;
        damagedSource = DamageSource.toDamageSource(damaged);
        damagerSource = DamageSource.toDamageSource(damager);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public LivingEntity getDamaged() {
        return damaged;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public HashMap<DamageType, Double> getDamage() {
        return damage;
    }

    public int getNoDamageTicks() {
        return noDamageTicks;
    }

    public DamageSource getDamagedSource() {
        return damagedSource;
    }

    public DamageSource getDamagerSource() {
        return damagerSource;
    }
}