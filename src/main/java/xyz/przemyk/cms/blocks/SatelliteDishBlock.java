package xyz.przemyk.cms.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import xyz.przemyk.cms.blocks.tile.SatelliteDishTile;

import javax.annotation.Nullable;

public class SatelliteDishBlock extends Block {

    public static final VoxelShape RENDER_SHAPE = VoxelShapes.create(0.1, 0, 0.1, 0.9, 0.9, 0.9);

    public SatelliteDishBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SatelliteDishTile();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return RENDER_SHAPE;
    }
}
