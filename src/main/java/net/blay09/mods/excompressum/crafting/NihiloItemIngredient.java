package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.items.ItemHandlerHelper;

public class NihiloItemIngredient implements IIngredientFactory {
	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		if(!json.has("value")) {
			throw new JsonSyntaxException("Missing key 'value', expected string");
		}
		String key = json.get("value").getAsString();
		ExNihiloProvider.NihiloItems nihiloItem = ExNihiloProvider.NihiloItems.valueOf(key);
		ItemStack itemStack = ExRegistro.getNihiloItem(nihiloItem);
		if(itemStack.isEmpty()) {
			return Ingredient.EMPTY;
		}
		int count = json.has("count") ? json.get("count").getAsInt() : 1;
		return Ingredient.fromStacks(ItemHandlerHelper.copyStackWithSize(itemStack, count));
	}
}
