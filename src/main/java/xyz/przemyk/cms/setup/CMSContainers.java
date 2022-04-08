package xyz.przemyk.cms.setup;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.containers.ControlStationContainer;

public class CMSContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CMSMod.MODID);

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

    public static final RegistryObject<MenuType<ControlStationContainer>> CONTROL_STATION = CONTAINERS.register("control_station", () -> new MenuType<>(ControlStationContainer::new));

}
