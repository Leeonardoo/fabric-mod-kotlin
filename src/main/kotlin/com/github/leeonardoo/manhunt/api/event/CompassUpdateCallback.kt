package com.github.leeonardoo.manhunt.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.item.ItemStack

/**
 * <p>
 * Callback for updating the hunters' compass.
 * Called before the compass is updated.
 * </p>
 *
 * <p>
 * Upon return, returning the {@code oldStack} parameter cancels the event,
 * and returning anything else continues to further processing by other listeners.
 * </p>
 *
 * <p>
 * The final return value is the updated hunter compass.
 * </p>
 *
 * @author YTG1234
 */
@FunctionalInterface
interface CompassUpdateCallback {

    /**
     * Called when a hunter's compass updates.
     */
    //TODO should probably get rid of those '!!'
    companion object {
        val EVENT: Event<CompassUpdateCallback>
            get() = EventFactory.createArrayBacked(CompassUpdateCallback::class.java) { listeners ->
                object : CompassUpdateCallback {
                    override fun onCompassUpdate(oldStack: ItemStack?, newStack: ItemStack?): ItemStack {
                        var currentStack = newStack?.copy()
                        for (listener in listeners) {
                            if (currentStack != null) {
                                currentStack = listener.onCompassUpdate(oldStack, currentStack.copy())
                            }
                            if (currentStack == oldStack) return oldStack!!
                        }
                        return currentStack!!
                    }
                }
            }
    }

    /**
     * Receives the old, non-updated or non-compass item stack, the hunter and the updated stack.
     *
     *
     *
     * Can apply some changes to the old or the updated [ItemStack], thus modifying the compass update process.
     * Returning the old stack effectively "cancelling" the update.
     * Returning a stack different than the `newStack` parameter changes the event behaviour.
     *
     *
     * @param oldStack The old, non-updated compass or another [ItemStack].
     * @param newStack The new, updated compass [ItemStack]. This stack is updated by the Manhunt mod or by another listener.
     *
     * @return The new, updated compass [ItemStack].
     */
    fun onCompassUpdate(oldStack: ItemStack?, newStack: ItemStack?): ItemStack
}