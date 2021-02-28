package xyz.przemyk.cms.setup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.cms.CMSMod;
import xyz.przemyk.cms.blocks.ControlStationBlock;
import xyz.przemyk.cms.blocks.SatelliteDishBlock;
import xyz.przemyk.cms.blocks.tile.SatelliteDishTile;

@SuppressWarnings({"unused", "ConstantConditions"})
public class CMSBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CMSMod.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CMSMod.MODID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        TILE_ENTITIES.register(eventBus);
    }

    public static final RegistryObject<SatelliteDishBlock> SATELLITE_DISH_BLOCK = BLOCKS.register("satellite_dish", () -> new SatelliteDishBlock(AbstractBlock.Properties.from(Blocks.IRON_BLOCK)));
    public static final RegistryObject<TileEntityType<SatelliteDishTile>> SATELLITE_DISH_TILE = TILE_ENTITIES.register("satellite_dish", () -> TileEntityType.Builder.create(SatelliteDishTile::new, SATELLITE_DISH_BLOCK.get()).build(null));

    public static final RegistryObject<ControlStationBlock> CONTROL_STATION_BLOCK = BLOCKS.register("control_station", () -> new ControlStationBlock(AbstractBlock.Properties.from(Blocks.IRON_BLOCK)));
}
