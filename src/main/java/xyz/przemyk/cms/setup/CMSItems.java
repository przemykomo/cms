package xyz.przemyk.cms.setup;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.cms.CMSMod;

public class CMSItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CMSMod.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final ItemGroup CMS_ITEM_GROUP = new ItemGroup(CMSMod.MODID) {
        @Override
        public ItemStack createIcon() {
            return SATELLITE_DISH.get().getDefaultInstance();
        }
    };

    public static final RegistryObject<BlockItem> SATELLITE_DISH = ITEMS.register("satellite_dish", () -> new BlockItem(CMSBlocks.SATELLITE_DISH_BLOCK.get(), new Item.Properties().group(CMS_ITEM_GROUP)));
}
