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
}
