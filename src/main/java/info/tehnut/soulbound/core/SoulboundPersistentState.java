package info.tehnut.soulbound.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import info.tehnut.soulbound.api.SlottedItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SoulboundPersistentState extends PersistentState {

    private final Map<UUID, List<SlottedItem>> persistedItems = Maps.newHashMap();

    public SoulboundPersistentState() {
        super("soulbound_persisted_items");
    }

    public void storePlayer(PlayerEntity player, List<SlottedItem> items) {
        this.persistedItems.put(player.getGameProfile().getId(), items);
        markDirty();
    }

    public List<SlottedItem> restorePlayer(PlayerEntity player) {
        List<SlottedItem> items = persistedItems.getOrDefault(player.getGameProfile().getId(), Collections.emptyList());
        persistedItems.remove(player.getGameProfile().getId());
        markDirty();
        return items;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        tag.getList("playerTags", 10).forEach(playerTag -> {
            CompoundTag playerCompound = (CompoundTag) playerTag;
            UUID uuid = playerCompound.getUuid("uuid");
            ListTag items = playerCompound.getList("items", 10);
            List<SlottedItem> slotted = Lists.newArrayList();
            items.forEach(itemTag -> {
                CompoundTag itemCompound = (CompoundTag) itemTag;
                Identifier id = new Identifier(itemCompound.getString("id"));
                ItemStack stack = ItemStack.fromTag(itemCompound.getCompound("stack"));
                int slot = itemCompound.getInt("slot");
                slotted.add(new SlottedItem(id, stack, slot));
            });
            persistedItems.put(uuid, slotted);
        });
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag playerTags = new ListTag();
        persistedItems.forEach((uuid, items) -> {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUuid("uuid", uuid);
            ListTag itemsList = new ListTag();
            items.forEach(slotted -> {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putString("id", slotted.getContainerId().toString());
                itemTag.put("stack", slotted.getStack().toTag(new CompoundTag()));
                itemTag.putInt("slot", slotted.getSlot());
                itemsList.add(itemTag);
            });
            playerTag.put("items", itemsList);

            playerTags.add(playerTag);
        });
        tag.put("playerTags", playerTags);
        return tag;
    }
}
