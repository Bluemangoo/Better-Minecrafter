package net.bluemangoo.betterMinecrafter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Items.class)
public abstract class ItemsMixin {
    @Shadow
    public static Item registerBlock(Block block, Item.Properties properties) {
        return null;
    }

    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Items;registerBlock(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/item/Item;"
            )
    )
    private static Item registerBlock(
            Block block, Operation<Item> original
    ) {
        if (block == Blocks.MINE_CRAFTER) {
            return registerBlock(block, new Item.Properties().fireResistant());
        }
        return original.call(block);
    }
}
