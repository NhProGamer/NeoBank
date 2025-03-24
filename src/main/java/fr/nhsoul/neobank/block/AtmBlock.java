package fr.nhsoul.neobank.block;

import fr.nhsoul.neobank.Bank;
import fr.nhsoul.neobank.init.NeobankModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import io.netty.buffer.Unpooled;

import fr.nhsoul.neobank.world.inventory.AtmMenuMenu;

public class AtmBlock extends Block {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public AtmBlock() {
		super(Properties.of().sound(SoundType.GRAVEL).strength(1f, 10f));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		if (stack.getItem() == NeobankModItems.BILL_5.get() ||
				stack.getItem() == NeobankModItems.BILL_1.get() ||
				stack.getItem() == NeobankModItems.BILL_10.get() ||
				stack.getItem() == NeobankModItems.BILL_20.get() ||
				stack.getItem() == NeobankModItems.BILL_50.get() ||
				stack.getItem() == NeobankModItems.BILL_100.get() ||
				stack.getItem() == NeobankModItems.BILL_200.get() ||
				stack.getItem() == NeobankModItems.BILL_500.get()) {
			if(!level.isClientSide()) {
				int amount = stack.getCount();
				int value = Bank.getMoneyValue(stack);
				stack.shrink(amount);
				Bank.addMoney(player, value*amount);
			}
		} else {
			openAtmGui(pos, player);
		}
		return ItemInteractionResult.SUCCESS;
	}

	private void openAtmGui(BlockPos pos, Player player) {
		player.openMenu(new MenuProvider() {
			@Override
			public Component getDisplayName() {
				return Component.literal("Atm");
			}

			@Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
				return new AtmMenuMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
			}
		}, pos);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockstate, Level world, BlockPos pos, Player entity, BlockHitResult hit) {
		super.useWithoutItem(blockstate, world, pos, entity, hit);
		if (entity instanceof ServerPlayer player) {
			openAtmGui(pos, player);
		}
		return InteractionResult.SUCCESS;
	}
}
