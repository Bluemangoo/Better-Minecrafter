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

import static net.minecraft.world.entity.player.Player.getXpNeededForLevel;

public record UpgradeC2SPacket() implements CustomPacketPayload {
    public static final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath(BetterMinecrafter.MOD_ID, "upgrade-minecrafter");
    public static final Type<UpgradeC2SPacket> TYPE = new Type<>(IDENTIFIER);
    public static final UpgradeC2SPacket INSTANCE = new UpgradeC2SPacket();
    public static final StreamCodec<Object, UpgradeC2SPacket> CODEC = StreamCodec.unit(INSTANCE);


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
            int requiredExp = MineCrafterBlockEntity.experienceRequiredForLevel(currentLevel);
            int playerExp = context.player().getTotalExperienceBasedOnLevels();

            if (saved_data.getLevel() > currentLevel) {
                level.addExperienceToMineCrafter(requiredExp - currentExp);
                if (saved_data.getLevel() == currentLevel + 1) {
                    level.addExperienceToMineCrafter(currentExp);
                }
                return;
            }
            if (playerExp < requiredExp - currentExp) {
                return;
            }

            int i = requiredExp - currentExp;
            level.addExperienceToMineCrafter(requiredExp - currentExp);

            while (i > 0) {
                int j = (int) (context.player().experienceProgress * (float) context.player().getXpNeededForNextLevel());
                if (j >= i) {
                    context.player().setExperiencePoints(j - i);
                    i = 0;
                } else {
                    i -= j;
                    context.player().setExperienceLevels(context.player().experienceLevel - 1);
                    context.player().setExperiencePoints(getXpNeededForLevel(context.player().experienceLevel));
                }
            }

        });
    }
}