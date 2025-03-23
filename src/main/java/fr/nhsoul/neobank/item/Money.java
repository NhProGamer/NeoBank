
package fr.nhsoul.neobank.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class Money extends Item {
	public Money() {
		super(new Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
