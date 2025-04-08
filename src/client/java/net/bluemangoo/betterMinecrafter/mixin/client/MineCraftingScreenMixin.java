package net.bluemangoo.betterMinecrafter.mixin.client;

import net.bluemangoo.betterMinecrafter.packet.DowngradeC2SPacket;
import net.bluemangoo.betterMinecrafter.packet.UpgradeC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MineCraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MineCraftingMenu;
import net.minecraft.world.level.block.entity.MineCrafterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MineCraftingScreen.class)
public abstract class MineCraftingScreenMixin extends AbstractContainerScreen<MineCraftingMenu> {
    public MineCraftingScreenMixin(MineCraftingMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "donateExperienceToMineCrafter", at = @At("HEAD"), cancellable = true)
    private void donateExperienceToMineCrafter(Button button, CallbackInfo ci) {
        if (Screen.hasShiftDown()) {
            if (Screen.hasControlDown()) {
                ci.cancel();
                return;
            }
            var menu = this.menu;
            if (menu.getCurrentLevel() > 0) {
                ClientPlayNetworking.send(new DowngradeC2SPacket());
            }
            ci.cancel();
            return;
        }
        if (Screen.hasControlDown()) {
            var player = Objects.requireNonNull(this.minecraft).player;
            var menu = this.menu;
            int requiredExp = MineCrafterBlockEntity.experienceRequiredForLevel(this.menu.getCurrentLevel());
            int currentExp = menu.getCurrentExp();
            assert player != null;
            int playerExp = player.getTotalExperienceBasedOnLevels();
            if (playerExp < requiredExp - currentExp) {
                ci.cancel();
            }
            ClientPlayNetworking.send(new UpgradeC2SPacket());
            ci.cancel();
        }
    }
}
