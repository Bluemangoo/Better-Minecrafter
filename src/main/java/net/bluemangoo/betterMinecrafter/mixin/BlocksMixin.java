package net.bluemangoo.betterMinecrafter.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Inject(
            method = "register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;",
            at = @At("HEAD")
    )
    private static void register(
            String string, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties, CallbackInfoReturnable<Block> cir
    ) {
        if ("mine_crafter".equals(string)) {
            properties.strength(-1.0F, 3600000.0F);
        }
    }
}
