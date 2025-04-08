package net.bluemangoo.betterMinecrafter.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class MinecrafterLevel extends SavedData {
    public static final Codec<MinecrafterLevel> CODEC = RecordCodecBuilder.create((instance) -> instance.group(Codec.INT.fieldOf("level").forGetter((minecrafterLevel) -> minecrafterLevel.level),
            Codec.INT.fieldOf("exp").forGetter((minecrafterLevel) -> minecrafterLevel.exp)).apply(instance, MinecrafterLevel::new));
    public static final SavedDataType<MinecrafterLevel> TYPE;
    int level = -1;
    int exp = 0;

    public MinecrafterLevel() {
        this.setDirty();
    }

    public MinecrafterLevel(int level, int exp) {
        this.level = level;
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public void setLevel(int level) {
        this.level = level;
        this.setDirty();
    }

    public void setExp(int exp) {
        this.exp = exp;
        this.setDirty();
    }

    public MinecrafterLevel checked(ServerLevel level) {
        if (this.level < 0) {
            this.setLevel(level.getMineCrafterLevel());
            this.setExp(level.getMineCrafterExp());
        }
        return this;
    }

    static {
        TYPE = new SavedDataType<>("minecrafter_level", MinecrafterLevel::new, CODEC, DataFixTypes.SAVED_DATA_MINE_PROGRESS);
    }
}
