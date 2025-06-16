package me.fearlessdude.createbinary.block.custom;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlock;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity;
import me.fearlessdude.createbinary.Utils;
import me.fearlessdude.createbinary.block.entity.ComputerizedRedstoneLinkBlockEntity;
import me.fearlessdude.createbinary.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ComputerizedRedstoneLinkBlock extends BaseEntityBlock implements IWrenchable {
    public static final VoxelShape SHAPE_FLAT = Block.box(2, 0, 2, 14, 3, 14);
    public static final VoxelShape SHAPE_VERTICAL = Block.box(3, -1, 1, 13, 3, 15);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty RECEIVING = BooleanProperty.create("receiving");
    public static final BooleanProperty TRANSMITTING = BooleanProperty.create("transmitting");

    public ComputerizedRedstoneLinkBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.FACING, Direction.UP)
                .setValue(RECEIVING, false)
                .setValue(TRANSMITTING, false));
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            Direction blockFacing = (Direction) state.getValue(FACING);
            if (fromPos.equals(pos.relative(blockFacing.getOpposite())) && !this.canSurvive(state, level, pos)) {
                level.destroyBlock(pos, true);
            } else {
                if (!level.getBlockTicks().willTickThisTick(pos, this)) {
                    level.scheduleTick(pos, this, 1);
                }

            }
        }
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos neighbourPos = pos.relative(((Direction)state.getValue(FACING)).getOpposite());
        BlockState neighbour = worldIn.getBlockState(neighbourPos);
        return !neighbour.canBeReplaced();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, RECEIVING, TRANSMITTING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace())
                .setValue(RECEIVING, false)
                .setValue(TRANSMITTING, false);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        VoxelShape SHAPE = facing.getAxis().isVertical() ? SHAPE_FLAT : SHAPE_VERTICAL;
//        VoxelShape shape = Utils.rotate(Direction.UP, facing, SHAPE);
        VoxelShape shape = Utils.rotateShapeToFacing(facing, SHAPE);
        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerizedRedstoneLinkBlockEntity(pos, state);
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.FAIL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.COMPUTERIZED_REDSTONE_LINK_BE.get() ?
                (level1, pos, state1, be) -> ((ComputerizedRedstoneLinkBlockEntity) be)
                        .tick(level1, pos, state1, (ComputerizedRedstoneLinkBlockEntity) be) : null;
    }
}
