package com.github.leeonardoo.manhunt.mixin;

import com.github.leeonardoo.manhunt.Manhunt;
import com.github.leeonardoo.manhunt.ManhuntUtils;
import com.github.leeonardoo.manhunt.config.Behaviours;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource damageSource);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "TAIL"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
        if (!world.isClient()) {
            if (source.getAttacker() != null) {
                if (source.getAttacker() instanceof PlayerEntity &&
                        ManhuntUtils.INSTANCE.getHunters().contains(source.getAttacker().getUuid()) &&
                        Manhunt.Companion.getCONFIG().getDamageBehaviour().equals(Behaviours.Damage.DAMAGE) &&
                        !isInvulnerableTo(source)) {

                    callback.setReturnValue(super.damage(source, Float.MAX_VALUE));
                }
            }
        }
    }
}