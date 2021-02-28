package xyz.przemyk.cms.setup;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.gui.ControlStationScreen;

@Mod.EventBusSubscriber(modid = CMSMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(CMSBlocks.SATELLITE_DISH_BLOCK.get(), RenderType.getCutout());
        ScreenManager.registerFactory(CMSContainers.CONTROL_STATION.get(), ControlStationScreen::new);
    }
}
