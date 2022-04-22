package wutdahack.actuallyunbreaking.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wutdahack.actuallyunbreaking.AUConfig;

import java.util.Random;

@Mixin(DigDurabilityEnchantment.class)
public abstract class UnbreakingEnchantmentMixin extends Enchantment {

    private UnbreakingEnchantmentMixin(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    // items can't have mending and unbreaking together if enabled in config
    @Override
    protected boolean checkCompatibility(Enchantment other) {
        if (AUConfig.instance.mendingIncompatibility) {
            return !(other instanceof MendingEnchantment) && super.checkCompatibility(other);
        } else {
            return super.checkCompatibility(other);
        }
    }

    // item won't take damage and the damage bar will
    // be removed depending on the level of the unbreaking enchantment
    @Inject(method = "shouldIgnoreDurabilityDrop", at = @At(value = "HEAD"), cancellable = true)
    private static void makeUnbreakable(ItemStack item, int level, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (AUConfig.instance.maxLevelOnly ? level >= Enchantments.UNBREAKING.getMaxLevel() : level > 0) {
            item.setDamageValue(0);
            cir.setReturnValue(true);
        }
    }

}
