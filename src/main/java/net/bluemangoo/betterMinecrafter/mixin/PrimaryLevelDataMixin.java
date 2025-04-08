package net.bluemangoo.betterMinecrafter.mixin;

import net.minecraft.world.level.block.entity.MineCrafterBlockEntity;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin {
    @Shadow
    private int mineCrafterExp;

    @Shadow
    private int mineCrafterLevel;


    @Inject(method = "addExperienceToMineCrafter", at = @At("TAIL"))
    public void addExperienceToMineCrafter(int i, CallbackInfo ci) {
        for (; this.mineCrafterExp < 0 && this.mineCrafterLevel > 0; --this.mineCrafterLevel) {
            this.mineCrafterExp += MineCrafterBlockEntity.experienceRequiredForLevel(this.mineCrafterLevel - 1);
        }
        if (this.mineCrafterExp < 0) {
            this.mineCrafterExp = 0;
        }
    }
}
