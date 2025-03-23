
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package fr.nhsoul.neobank.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import fr.nhsoul.neobank.block.AtmBlock;
import fr.nhsoul.neobank.NeoBankMod;

public class NeobankModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(NeoBankMod.MODID);
	public static final DeferredBlock<Block> ATM = REGISTRY.register("atm", AtmBlock::new);
}
