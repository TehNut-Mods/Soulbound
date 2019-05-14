package info.tehnut.soulbound.mixin;

import info.tehnut.soulbound.Soulbound;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerEntity.class, priority = 6969)
public class MixinPlayerEntity {

    @Shadow
    @Final
    public PlayerInventory inventory;

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"), cancellable = true)
    private void enchants$dropInventory$soulbound(CallbackInfo callbackInfo) {
        dropInventory(inventory.main, (PlayerEntity) (Object) this);
        dropInventory(inventory.offHand, (PlayerEntity) (Object) this);
        dropInventory(inventory.armor, (PlayerEntity) (Object) this);
        callbackInfo.cancel();
    }

    private static void dropInventory(DefaultedList<ItemStack> inventory, PlayerEntity player) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (stack.isEmpty())
                continue;

            int soulboundLevel = EnchantmentHelper.getLevel(Soulbound.ENCHANT_SOULBOUND, stack);
            if (soulboundLevel > 0)
                continue;

            player.dropItem(stack, true, false);
            inventory.set(i, ItemStack.EMPTY);
        }
    }
}
