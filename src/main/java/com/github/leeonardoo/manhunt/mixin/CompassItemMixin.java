package com.github.leeonardoo.manhunt.mixin;

import com.github.leeonardoo.manhunt.Manhunt;
import com.github.leeonardoo.manhunt.ManhuntUtils;
import com.github.leeonardoo.manhunt.config.Behaviours;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(CompassItem.class)
public abstract class CompassItemMixin extends Item {

    private CompassItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking() &&
                Manhunt.Companion.getCONFIG().getCompassBehaviour().equals(Behaviours.Compass.USE) &&
                ManhuntUtils.INSTANCE.getHunters().contains(player.getUuid())) {

            if (!world.isClient) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack == null) {
                    // This should never execute as it happens when a player uses that stack (so not null)
                    return super.use(world, player, hand);
                }

                if (stack.getItem().equals(Items.COMPASS)) {
                    player.equip(8, ManhuntUtils.updateCompass(
                            stack, ManhuntUtils.fromServer(
                                    Objects.requireNonNull(player.getServer()), ManhuntUtils.INSTANCE.getSpeedrunner()
                            ))
                    );
                }
            }
            return TypedActionResult.success(player.getStackInHand(hand), world.isClient());
        }
        return super.use(world, player, hand);
    }
}