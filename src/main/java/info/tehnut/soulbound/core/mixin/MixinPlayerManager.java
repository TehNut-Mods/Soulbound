package info.tehnut.soulbound.core.mixin;

import info.tehnut.soulbound.api.SlottedItem;
import info.tehnut.soulbound.Soulbound;
import info.tehnut.soulbound.api.SoulboundContainer;
import info.tehnut.soulbound.core.SoulboundPersistentState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setMainArm(Lnet/minecraft/util/Arm;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void soulbound$respawnPlayer(ServerPlayerEntity oldPlayer, boolean dimensionChange, CallbackInfoReturnable<ServerPlayerEntity> callback, BlockPos blockPos, float hello, boolean forcedSpawn, ServerWorld oldWorld, Optional optional, ServerPlayerInteractionManager interactionManager, ServerWorld newWorld, ServerPlayerEntity newPlayer) {
        if (dimensionChange)
            return;

        SoulboundPersistentState persistentState = server.getOverworld().getPersistentStateManager().getOrCreate(SoulboundPersistentState::new, "soulbound_persisted_items");

        List<SlottedItem> savedItems = persistentState.restorePlayer(oldPlayer);
        if (savedItems == null)
            return;

        SoulboundContainer.CONTAINERS.forEach((id, container) -> {
            savedItems.stream().filter(item -> item.getContainerId().equals(id)).forEach(item -> {
                if (newPlayer.getRandom().nextFloat() <= Soulbound.CONFIG.get().getSoulboundRemovalChance()) {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(item.getStack());
                    enchantments.remove(Soulbound.ENCHANT_SOULBOUND);
                    EnchantmentHelper.set(enchantments, item.getStack());
                }

                container.replaceItem(newPlayer, item);
            });
        });

        savedItems.clear();
    }
}
