package dev.rdh.sarcio.mixin.memory_management.world;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.property.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(targets = "net.minecraft.block.state.StateDefinition$BlockStateImpl")
abstract class BlockStateImplMixin {
	@Shadow @Final private ImmutableMap<Property, Comparable> values;
	@Unique private BlockState[] sarcio$transitions;

	/**
	 * @author rdh
	 * @reason more compact state transition table
	 */
	@Overwrite
	public void findNeighbors(Map<Map<Property, Comparable>, ?> states) {
		int size = 0;
		for (Property<?> property : this.values.keySet()) {
			size += property.values().size();
		}

		this.sarcio$transitions = new BlockState[size];
		Map<Property, Comparable> values = new HashMap<>(this.values);
		int index = 0;

		for (Property<? extends Comparable> property : this.values.keySet()) {
			Comparable current = this.values.get(property);
			for (Comparable value : property.values()) {
				values.put(property, value);
				BlockState state = (BlockState) states.get(values);
				if (state == null) {
					throw new IllegalStateException();
				}
				this.sarcio$transitions[index++] = state;
			}
			values.put(property, current);
		}
	}

	/**
	 * @author rdh
	 * @reason more compact state transition table
	 */
	@Overwrite
	public <T extends Comparable<T>, V extends T> BlockState set(Property<T> property, V value) {
		if (!this.values.containsKey(property)) {
			throw new IllegalArgumentException("Cannot get property " + property);
		}
		if (!property.values().contains(value)) {
			throw new IllegalArgumentException("Cannot set property " + property + " to " + value);
		}
		if (this.values.get(property) == value) {
			return (BlockState) this;
		}

		int index = 0;
		for (Property<?> candidate : this.values.keySet()) {
			for (Object allowed : candidate.values()) {
				if (candidate.equals(property) && allowed.equals(value)) {
					return this.sarcio$transitions[index];
				}
				index++;
			}
		}

		throw new IllegalStateException();
	}
}
