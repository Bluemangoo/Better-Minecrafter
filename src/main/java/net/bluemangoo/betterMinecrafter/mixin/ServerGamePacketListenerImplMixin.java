package net.bluemangoo.betterMinecrafter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bluemangoo.betterMinecrafter.data.MinecrafterLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @WrapOperation(method = "handlePlayerDonateExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;donateExperienceToMineCrafter()Z"))
    boolean playerDonateExperienceWrapper(ServerPlayer player, Operation<Boolean> original) {
        var level = player.serverLevel();
        var saved_data = level
                .getDataStorage()
                .computeIfAbsent(MinecrafterLevel.TYPE)
                .checked(level);
        int currentLevel = level.getMineCrafterLevel();
        int currentExp = level.getMineCrafterExp();

        if (saved_data.getLevel() > currentLevel) {
            level.addExperienceToMineCrafter(20);
            return true;
        } else if (saved_data.getLevel() == currentLevel && saved_data.getExp() - currentExp >= 20) {
            level.addExperienceToMineCrafter(20);
            return true;
        }

        return original.call(player);
    }
}
