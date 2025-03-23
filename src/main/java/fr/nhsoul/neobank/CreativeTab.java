package fr.nhsoul.neobank;

import fr.nhsoul.neobank.init.NeobankModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NeoBankMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("neobank", () -> CreativeModeTab.builder()
            .title(Component.translatable("NeoBank"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> NeobankModItems.ATM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(NeobankModItems.ATM.get());
                output.accept(NeobankModItems.BILL_1.get());
                output.accept(NeobankModItems.BILL_5.get());
                output.accept(NeobankModItems.BILL_10.get());
                output.accept(NeobankModItems.BILL_20.get());
                output.accept(NeobankModItems.BILL_50.get());
                output.accept(NeobankModItems.BILL_100.get());
                output.accept(NeobankModItems.BILL_200.get());
                output.accept(NeobankModItems.BILL_500.get());
            }).build());
}
