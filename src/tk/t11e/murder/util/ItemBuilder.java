package tk.t11e.murder.util;
// Created by booky10 in knockIt (19:36 14.01.20)

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    private Material material;
    private int amount;
    private String name;
    private boolean unbreakable;
    private HashMap<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;

    public ItemBuilder(Material material, int amount, String name)  {
        this.amount=amount;
        this.material=material;
        this.name = name;
        unbreakable=false;
        enchantments=new HashMap<>();
        itemFlags=new ArrayList<>();
    }

    public ItemBuilder(Material material, int amount)  {
        this(material,amount,material.getKey().getKey().replace('_',' '));
    }

    public ItemBuilder(Material material)  {
        this(material,1);
    }

    public int getAmount() {
        return amount;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public ItemBuilder setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
        return this;
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.removeAll(Arrays.asList(itemFlags));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
        return this;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public ItemBuilder setEnchantments(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setUnbreakable(unbreakable);
        itemMeta.setDisplayName(name);
        for (ItemFlag itemFlag : itemFlags)
            itemMeta.addItemFlags(itemFlag);

        itemStack.setItemMeta(itemMeta);
        for (Enchantment enchantment : enchantments.keySet())
            itemStack.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
        return itemStack;
    }
}