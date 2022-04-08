package xyz.przemyk.cms.setup;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.CMSSkyRenderHandler;

@Mod.EventBusSubscriber(modid = CMSMod.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ClientLevel clientLevel && clientLevel.dimensionTypeRegistration().is(DimensionType.OVERWORLD_LOCATION)) {
            clientLevel.effects().setSkyRenderHandler(new CMSSkyRenderHandler());
        }
    }
}
