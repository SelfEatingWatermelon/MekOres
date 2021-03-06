package com.selfeatingwatermelon.mekores.remap;

import com.selfeatingwatermelon.mekores.Log;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Remapper {
	private static final Marker MARKER = MarkerManager.getMarker("Remapper", Log.MOD_MARKER);

	/**
	 * A list of remapping functions that return {@code true} if they took an action for the {@link MissingMapping}.
	 */
	private static final List<Predicate<MissingMapping>> remappingFunctions = ImmutableList.of(Remapper::remapCustomName, Remapper::remapCamelCaseToUnderscores);

	/**
	 * Custom names to remap. Keys are the old names, values are the new names.
	 */
	private static final Map<String, String> customNames = ImmutableMap.<String, String>builder()
			.build();

	private Remapper() {
	}

	/**
	 * Remap this mod's missing mappings.
	 *
	 * @param missingMappings This mod's missing mappings
	 */
	public static void remap(List<MissingMapping> missingMappings) {
		for (MissingMapping missingMapping : missingMappings) { // For each missing mapping,
			Log.info(MARKER, "Trying to remap %s", missingMapping.resourceLocation);

			for (Predicate<MissingMapping> remappingFunction : remappingFunctions) { // For each remapping function
				if (remappingFunction.test(missingMapping)) { // If the function took an action,
					break; // Break from the inner loop
				}
			}

			if (missingMapping.getAction() == FMLMissingMappingsEvent.Action.DEFAULT) {
				Log.info(MARKER, "Couldn't remap %s", missingMapping.resourceLocation);
			}
		}
	}

	/**
	 * Try to remap {@code missingMapping} to the value of {@code registryName}.
	 *
	 * @param missingMapping The missing mapping
	 * @param registryName   The registry name to remap to
	 * @return True if the remapping was successful
	 */
	private static boolean tryRemap(MissingMapping missingMapping, ResourceLocation registryName) {
		switch (missingMapping.type) {
			case BLOCK:
				final IForgeRegistry<Block> blockRegistry = ForgeRegistries.BLOCKS;

				if (blockRegistry.containsKey(registryName)) {
					Log.info(MARKER, "Remapped block %s to %s", missingMapping.resourceLocation, registryName);
					missingMapping.remap(blockRegistry.getValue(registryName));
					return true;
				}

				break;
			case ITEM:
				final IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;

				if (itemRegistry.containsKey(registryName)) {
					Log.info(MARKER, "Remapped item %s to %s", missingMapping.resourceLocation, registryName);
					missingMapping.remap(itemRegistry.getValue(registryName));
					return true;
				}

				break;
		}

		return false;
	}

	private static final Pattern UPPER_CASE_LETTER = Pattern.compile("\\p{Lu}");

	/**
	 * Remap {@code camelCase} names to {@code lowercase_underscores} names.
	 *
	 * @param missingMapping The missing mapping
	 * @return True if the missing mapping was remapped
	 */
	private static boolean remapCamelCaseToUnderscores(MissingMapping missingMapping) {
		final String missingPath = missingMapping.resourceLocation.getResourcePath();

		// If the name is already lowercase, ignore it
		if (missingPath.toLowerCase().equals(missingPath)) return false;

		// Put an underscore in front of every uppercase letter, then convert it to lowercase.
		final String newPath = UPPER_CASE_LETTER.matcher(missingPath).replaceAll("_$0").toLowerCase();
		final ResourceLocation newRegistryName = new ResourceLocation(missingMapping.resourceLocation.getResourceDomain(), newPath);

		// Try to remap to the new registry name
		return tryRemap(missingMapping, newRegistryName);
	}


	/**
	 * Remap names to those specified in {@link #customNames}.
	 *
	 * @param missingMapping The missing mapping
	 * @return True if the missing mapping was remapped
	 */
	private static boolean remapCustomName(MissingMapping missingMapping) {
		final String missingPath = missingMapping.resourceLocation.getResourcePath();

		if (!customNames.containsKey(missingPath)) return false;

		final String newPath = customNames.get(missingPath);
		final ResourceLocation newRegistryName = new ResourceLocation(missingMapping.resourceLocation.getResourceDomain(), newPath);

		return tryRemap(missingMapping, newRegistryName);
	}
}
