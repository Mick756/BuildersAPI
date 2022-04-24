package api.builders.inventory;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {
	
	private final Inventory inventory;
	
	public InventoryBuilder(InventoryType type) {
		this.inventory = Bukkit.createInventory(null, type);
	}
	
	public InventoryBuilder(int slots) {
		this.inventory = Bukkit.createInventory(null, slots);
	}
	
	public InventoryBuilder(InventoryHolder owner, int slots, String title) {
		this.inventory = Bukkit.createInventory(owner, slots, title);
	}
	
	public InventoryBuilder(InventoryType type, String title) {
		this.inventory = Bukkit.createInventory(null, type, title);
	}
	
	public InventoryBuilder(InventoryHolder owner, InventoryType type, String title) {
		this.inventory = Bukkit.createInventory(owner, type, title);
	}
	
	public InventoryBuilder addItem(ItemStack... stacks) {
		this.inventory.addItem(stacks);
		return this;
	}
	
	public InventoryBuilder setItem(int slot, ItemStack stack) {
		if (slot >= this.inventory.getSize()) throw new IndexOutOfBoundsException(slot);
		
		this.inventory.setItem(slot, stack);
		return this;
	}
	
	public InventoryBuilder clear() {
		this.inventory.clear();
		return this;
	}
	
	public Inventory build() {
		return this.inventory;
	}
	
	
}
