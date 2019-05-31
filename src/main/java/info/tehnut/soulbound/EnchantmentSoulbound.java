package info.tehnut.soulbound;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentSoulbound extends Enchantment {

    public EnchantmentSoulbound() {
        super(Weight.VERY_RARE, EnchantmentTarget.ALL, EquipmentSlot.values());
    }

    @Override
    public int getMaximumLevel() {
        return 1;
    }

    @Override
    public int getMinimumPower(int level) {
        return 15;
    }

    @Override
    protected boolean differs(Enchantment enchantment) {
        return super.differs(enchantment) && enchantment != Enchantments.VANISHING_CURSE;
    }

    @Override
    public boolean isAcceptableItem(ItemStack itemStack_1) {
        return true;
    }
}
