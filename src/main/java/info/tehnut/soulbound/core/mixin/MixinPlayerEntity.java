package info.tehnut.soulbound.core.mixin;

import com.google.common.collect.Lists;
import info.tehnut.soulbound.core.SlottedItem;
import info.tehnut.soulbound.Soulbound;
import info.tehnut.soulbound.api.SoulboundContainer;
import info.tehnut.soulbound.core.ExtendedPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = PlayerEntity.class, priority = 6969)
public class MixinPlayerEntity implements ExtendedPlayer {

    private final List<SlottedItem> soulboundItems = Lists.newArrayList();

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void enchants$dropInventory$soulbound(CallbackInfo callbackInfo) {
        soulboundItems.clear();

        SoulboundContainer.CONTAINERS.forEach((id, container) -> {
            List<ItemStack> inventory = container.getContainerStacks((PlayerEntity) (Object) this);
            if (inventory == null)
                return;

            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.get(i);
                if (stack.isEmpty())
                    continue;

                int soulboundLevel = EnchantmentHelper.getLevel(Soulbound.ENCHANT_SOULBOUND, stack);
                if (soulboundLevel > 0) {
                    soulboundItems.add(new SlottedItem(id, stack, i));
                    inventory.set(i, ItemStack.EMPTY);
                }
            }
        });
    }

    @Override
    public List<SlottedItem> soulbound$getSoulboundItems() {
        return soulboundItems;
    }
}
