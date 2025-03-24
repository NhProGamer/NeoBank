package fr.nhsoul.neobank.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

import fr.nhsoul.neobank.world.inventory.AtmMenuMenu;
import fr.nhsoul.neobank.NeoBankMod;

public class NeobankModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, NeoBankMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<AtmMenuMenu>> ATM_MENU = REGISTRY.register("atm_menu", () -> IMenuTypeExtension.create(AtmMenuMenu::new));
}
