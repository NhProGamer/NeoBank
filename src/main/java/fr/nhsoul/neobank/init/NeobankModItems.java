package fr.nhsoul.neobank.init;

import fr.nhsoul.neobank.NeoBankMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import fr.nhsoul.neobank.item.Money;

public class NeobankModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(NeoBankMod.MODID);
	public static final DeferredItem<Item> ATM = block(NeobankModBlocks.ATM);
	public static final DeferredItem<Item> BILL_1 = REGISTRY.register("bill_1", Money::new);
	public static final DeferredItem<Item> BILL_5 = REGISTRY.register("bill_5", Money::new);
	public static final DeferredItem<Item> BILL_10 = REGISTRY.register("bill_10", Money::new);
	public static final DeferredItem<Item> BILL_20 = REGISTRY.register("bill_20", Money::new);
	public static final DeferredItem<Item> BILL_50 = REGISTRY.register("bill_50", Money::new);
	public static final DeferredItem<Item> BILL_100 = REGISTRY.register("bill_100", Money::new);
	public static final DeferredItem<Item> BILL_200 = REGISTRY.register("bill_200", Money::new);
	public static final DeferredItem<Item> BILL_500 = REGISTRY.register("bill_500", Money::new);


	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
