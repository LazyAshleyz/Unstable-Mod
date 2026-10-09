package com.unstable.item;

import java.util.function.Function;

import com.unstable.Unstable;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {
	public static final Item NUKE_SHOT = register("nuke_shot", NukeShotItem::new);
	public static final Item STAB_SHOT = register("stab_shot", StabShotItem::new);
	public static final Item FLAMEWRD_ROD = register("flamewrd_rod", FlamewrdRodItem::new);

	private ModItems() {
	}

	public static void initialize() {
	}

	private static Item register(String path, Function<Item.Properties, Item> itemFactory) {
		Identifier id = Unstable.id(path);
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
		Item.Properties properties = new Item.Properties().setId(itemKey).durability(4);
		return Registry.register(BuiltInRegistries.ITEM, id, itemFactory.apply(properties));
	}
}
