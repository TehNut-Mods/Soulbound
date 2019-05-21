package info.tehnut.soulbound.core;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SlottedItem {

    private final Identifier containerId;
    private final ItemStack stack;
    private final int slot;

    public SlottedItem(Identifier containerId, ItemStack stack, int slot) {
        this.containerId = containerId;
        this.stack = stack;
        this.slot = slot;
    }

    public Identifier getContainerId() {
        return containerId;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getSlot() {
        return slot;
    }
}
