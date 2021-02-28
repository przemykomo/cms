package xyz.przemyk.cms.setup;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.containers.ControlStationContainer;

public class CMSContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CMSMod.MODID);

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

    public static final RegistryObject<ContainerType<ControlStationContainer>> CONTROL_STATION = CONTAINERS.register("control_station", () -> new ContainerType<ControlStationContainer>(ControlStationContainer::new));

}
