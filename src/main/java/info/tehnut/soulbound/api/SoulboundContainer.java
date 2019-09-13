package info.tehnut.soulbound.api;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public interface SoulboundContainer {

    Map<Identifier, SoulboundContainer> CONTAINERS = Maps.newHashMap();

    /**
     * Used to expose an inventory attached to the player to Soulbound. Soulbound will gather valid items and restore them
     * into their original slot.
     *
     * This list must be mutable and must support {@link List#set(int, Object)}.
     *
     * @param player The player who has died
     * @return A mutable list of stacks attached to the player or null
     */
    List<ItemStack> getContainerStacks(PlayerEntity player);

    /**
     * Controls how items are inserted back into the player's inventory. These items are pre-filtered to match the proper
     * container.
     *
     * @param player The player who is respawning
     * @param item The item to be replaced
     */
    default void replaceItem(PlayerEntity player, SlottedItem item) {
        getContainerStacks(player).set(item.getSlot(), item.getStack());
    }

    /**
     * Controls how items in the inventory are removed from the drop list
     *
     * @param player The player who has died
     * @param slot The slot we have stored
     */
    default void removeStoredItem(PlayerEntity player, int slot) {
        getContainerStacks(player).set(slot, ItemStack.EMPTY);
    }
}
