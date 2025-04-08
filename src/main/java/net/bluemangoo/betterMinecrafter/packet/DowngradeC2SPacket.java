package net.bluemangoo.betterMinecrafter.packet;

import net.bluemangoo.betterMinecrafter.BetterMinecrafter;
import net.bluemangoo.betterMinecrafter.data.MinecrafterLevel;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.MineCrafterBlockEntity;
import org.jetbrains.annotations.NotNull;

public record DowngradeC2SPacket() implements CustomPacketPayload {
    public static final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath(BetterMinecrafter.MOD_ID, "downgrade-minecrafter");
    public static final Type<DowngradeC2SPacket> TYPE = new Type<>(IDENTIFIER);
    public static final DowngradeC2SPacket INSTANCE = new DowngradeC2SPacket();
    public static final StreamCodec<Object, DowngradeC2SPacket> CODEC = StreamCodec.unit(INSTANCE);


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(TYPE, CODEC);
        ServerPlayNetworking.registerGlobalReceiver(TYPE, (packet, context) -> {
            var level = context.player().serverLevel();
            var saved_data = level
                    .getDataStorage()
                    .computeIfAbsent(MinecrafterLevel.TYPE)
                    .checked(level);
            int currentLevel = level.getMineCrafterLevel();
            int currentExp = level.getMineCrafterExp();
            if (currentLevel < 1) {
                return;
            }
            int requiredExp = MineCrafterBlockEntity.experienceRequiredForLevel(currentLevel - 1);
            if (saved_data.getLevel() <= currentLevel) {
                saved_data.setLevel(currentLevel);
                if (saved_data.getExp() < currentExp) {
                    saved_data.setExp(currentExp);
                }
            }

            level.addExperienceToMineCrafter(-requiredExp - currentExp);

        });
    }
}