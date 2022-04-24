package api.builders.misc;

import api.builders.exceptions.ChestNotFoundException;
import api.builders.exceptions.UnableToCompleteException;
import com.github.f4b6a3.util.random.Xorshift128PlusRandom;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestUtils {
	
	protected Chest chest;
	
	public ChestUtils(Block block) throws ChestNotFoundException {
		try {
			this.chest = (Chest) block;
		} catch (Exception ex) {
			throw new ChestNotFoundException("There was no chest found at this location");
		}
	}
	
	public ChestUtils(Location location) throws ChestNotFoundException {
		new ChestUtils(location.getBlock());
	}
	
	public ChestUtils(Chest chest) {
		this.chest = chest;
	}
	
	public Inventory getInventory() {
		
		if (this.chest == null) return null;
		
		return this.chest.getInventory();
	}
	
	public int availableSlotCount() {
		int available = 0;
		for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
			ItemStack stack = this.getInventory().getItem(slot);
			if (stack == null || stack.getType().equals(Material.AIR)) {
				available++;
			}
		}
		
		return available;
	}
	
	public int[] getAvailableSlots() {
		int[] slots = new int[this.getInventory().getSize()];
		
		int i = 0;
		for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
			ItemStack stack = this.getInventory().getItem(slot);
			if (stack == null || stack.getType().equals(Material.AIR)) {
				slots[i] = slot;
				i++;
			}
		}
		
		return slots;
	}
	
	public void clear() {
		this.getInventory().clear();
	}
	
	public boolean addItem(boolean random, ItemStack... stacks) {
		
		if (random) {
			Xorshift128PlusRandom r = new Xorshift128PlusRandom();
			
			int[] slots = this.getAvailableSlots();
			
			for (ItemStack stack : stacks) {
				
				boolean canPlace = false;
				while (!canPlace) {
					int slot = slots[r.nextInt(slots.length - 1)];
					
					if (this.getInventory().getItem(slot) == null) {
						canPlace = true;
						this.getInventory().setItem(slot, stack);
					}
				}
			}
			
			return true;
		}
		
		if (availableSlotCount() > stacks.length) {
			this.getInventory().addItem(stacks);
			return true;
		}
		return false;
	}
	
	@SneakyThrows
	public static ChestUtils placeChest(Location location, ChestContents contents, boolean placeRandom) {
		location.getBlock().setType(Material.CHEST);
		
		if (contents != null) {
			ChestUtils chest = new ChestUtils(location.getBlock());
			
			if (!chest.addItem(placeRandom, contents.getContents())) {
				throw new UnableToCompleteException("An item was unable to be placed in the chest.");
			}
			
			return chest;
		} else {
			return new ChestUtils(location.getBlock());
		}
	}
	
	public static class ChestContents {
		
		private final @Getter ItemStack[] contents;
		
		public ChestContents(ItemStack... itemStacks) {
			this.contents = itemStacks;
		}
		
	}
}
