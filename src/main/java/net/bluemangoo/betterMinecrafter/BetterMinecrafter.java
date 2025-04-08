package net.bluemangoo.betterMinecrafter;

import net.bluemangoo.betterMinecrafter.packet.DowngradeC2SPacket;
import net.bluemangoo.betterMinecrafter.packet.UpgradeC2SPacket;
import net.fabricmc.api.ModInitializer;

public class BetterMinecrafter implements ModInitializer {
    public static final String MOD_ID = "better-minecrafter";

    @Override
    public void onInitialize() {
        UpgradeC2SPacket.register();
        DowngradeC2SPacket.register();
    }
}
