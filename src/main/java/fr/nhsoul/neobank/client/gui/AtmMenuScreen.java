package fr.nhsoul.neobank.client.gui;

import fr.nhsoul.neobank.NeoBankMod;
import fr.nhsoul.neobank.network.NeoBits;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

import fr.nhsoul.neobank.world.inventory.AtmMenuMenu;

import com.mojang.blaze3d.systems.RenderSystem;
import net.neoforged.neoforge.network.PacketDistributor;

public class AtmMenuScreen extends AbstractContainerScreen<AtmMenuMenu> {
	private final static HashMap<String, Object> guistate = AtmMenuMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	EditBox amount;
	Button button_valider;

	public AtmMenuScreen(AtmMenuMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("neobank:textures/screens/atm_menu.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		amount.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (amount.isFocused())
			return amount.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String amountValue = amount.getValue();
		super.resize(minecraft, width, height);
		amount.setValue(amountValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		int textWidth = this.font.width(Component.translatable("gui.neobank.atm_menu.label"));
		int xPosition = (this.imageWidth - textWidth) / 2;

		guiGraphics.drawString(this.font,
				Component.translatable("gui.neobank.atm_menu.label"),
				xPosition,
				(this.imageHeight/2)-18-2,
				-12829636,
				false
		);

		guiGraphics.drawString(this.font,
				Component.translatable("block.neobank.atm_label", NeoBankMod.NeoBitsAmount),
				6,
				6,
				-12829636,
				false
		);
	}

	@Override
	public void init() {
		super.init();
		amount = new EditBox(this.font, (this.width/2)-(118/2), (this.height/2)-(18/2), 118, 18, Component.literal("")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion("0");
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion("0");
				else
					setSuggestion(null);
			}
		};
		amount.setMaxLength(12);
		amount.setFilter(this::filterInput);
		amount.setSuggestion("0");
		guistate.put("text:amount", amount);
		this.addWidget(this.amount);

		button_valider = Button.builder(Component.translatable("gui.neobank.atm_menu.confirm_button"), e -> {
			if (!amount.getValue().isEmpty()) {
				PacketDistributor.sendToServer(new NeoBits(Integer.parseInt(amount.getValue())));
			}
			this.minecraft.player.closeContainer();
		}).bounds(this.leftPos + 111, this.topPos + 142, 61, 20).build();
		guistate.put("button:button_valider", button_valider);

		this.addRenderableWidget(button_valider);
	}

	private boolean filterInput(String input) {
		if (input.isEmpty()) return true;

		//boolean hasSeparator = false;
		//int decimalCount = 0;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);

			// Autorise les chiffres
			if (Character.isDigit(c)) {
				//if (hasSeparator) decimalCount++; // Compte les décimales après le séparateur
				//if (decimalCount > 2) return false; // Bloque si plus de 2 décimales
				continue;
			}

			// Gère les séparateurs ('.' ou ',')
			/*if (c == '.' || c == ',') {
				if (hasSeparator || i == 0) return false; // Un seul séparateur, pas en premier
				hasSeparator = true;
				continue;
			}*/

			return false; // Caractère interdit
		}
		if (Integer.parseInt(input) > NeoBankMod.NeoBitsAmount) return false;
		return true;
	}
}
