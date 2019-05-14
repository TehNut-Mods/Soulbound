package info.tehnut.soulbound.mixin;

import info.tehnut.soulbound.Soulbound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.Random;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setMainHand(Lnet/minecraft/util/AbsoluteHand;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void enchants$respawnPlayer(ServerPlayerEntity oldPlayer, DimensionType dimension, boolean dimensionChange, CallbackInfoReturnable<ServerPlayerEntity> callback, BlockPos pos, boolean forcedSpawn, ServerPlayerInteractionManager interactionManager, ServerPlayerEntity newPlayer) {
        if (dimensionChange)
            return;

        cloneSoulbound(oldPlayer.inventory.main, newPlayer.inventory.main, newPlayer.getRand());
        cloneSoulbound(oldPlayer.inventory.offHand, newPlayer.inventory.offHand, newPlayer.getRand());
        cloneSoulbound(oldPlayer.inventory.armor, newPlayer.inventory.armor, newPlayer.getRand());
    }

    private static void cloneSoulbound(DefaultedList<ItemStack> inventory, DefaultedList<ItemStack> newInventory, Random random) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.get(i);
            if (stack.isEmpty())
                continue;

            int soulboundLevel = EnchantmentHelper.getLevel(Soulbound.ENCHANT_SOULBOUND, stack);
            if (soulboundLevel == 0)
                continue;

            if (random.nextFloat() <= Soulbound.CONFIG.get().getSoulboundRemovalChance()) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                enchantments.remove(Soulbound.ENCHANT_SOULBOUND);
                EnchantmentHelper.set(enchantments, stack);
            }

            newInventory.set(i, stack);
            inventory.set(i, ItemStack.EMPTY);
        }
    }
}
