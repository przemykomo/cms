package xyz.przemyk.cms.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.przemyk.cms.CMSMod;

@SuppressWarnings("unused")
public class CMSItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CMSMod.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final CreativeModeTab CMS_ITEM_GROUP = new CreativeModeTab(CMSMod.MODID) {
        @Override
        public ItemStack makeIcon() {
            return CONTROL_STATION.get().getDefaultInstance();
        }
    };

    public static final RegistryObject<BlockItem> CONTROL_STATION = ITEMS.register("control_station", () -> new BlockItem(CMSBlocks.CONTROL_STATION_BLOCK.get(), new Item.Properties().tab(CMS_ITEM_GROUP)));
}
