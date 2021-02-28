package xyz.przemyk.cms;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.przemyk.cms.setup.CMSBlocks;
import xyz.przemyk.cms.setup.CMSContainers;
import xyz.przemyk.cms.setup.CMSItems;

@Mod(CMSMod.MODID)
public class CMSMod {
    public static final String MODID = "cms";

    public CMSMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CMSBlocks.register(eventBus);
        CMSItems.register(eventBus);
        CMSContainers.register(eventBus);
    }
}
