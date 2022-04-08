package xyz.przemyk.cms.setup;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.blocks.ControlStationBlock;

@SuppressWarnings({"unused", "ConstantConditions"})
public class CMSBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CMSMod.MODID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final RegistryObject<ControlStationBlock> CONTROL_STATION_BLOCK = BLOCKS.register("control_station", () -> new ControlStationBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));
}
