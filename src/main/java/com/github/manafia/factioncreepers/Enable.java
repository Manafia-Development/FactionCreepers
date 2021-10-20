package com.github.manafia.factioncreepers;

import com.github.manafia.factioncreepers.commands.CreeperEggCommand;
import com.github.manafia.factioncreepers.listeners.CreeperDeath;
import com.github.manafia.factioncreepers.listeners.ThrowableCegg;
import com.github.manafia.factioncreepers.listeners.dynamite.DynamiteCreeperSpawn;
import com.github.manafia.factioncreepers.listeners.dynamite.DynamiteExplode;
import com.github.manafia.factioncreepers.listeners.rocket.RocketCreeperSpawn;
import com.github.manafia.factioncreepers.listeners.sandstorm.SandstormCreeperSpawn;
import com.github.manafia.factioncreepers.listeners.sandstorm.SandstormExplode;
import com.github.manafia.factioncreepers.listeners.sticky.StickyCreeperSpawn;
import com.github.manafia.factioncreepers.listeners.sticky.StickyCreeperSuffocate;
import com.github.manafia.factioncreepers.utilities.particles.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Enable extends JavaPlugin {


    public static List<String> creeperTypes = Arrays.asList("dynamite, rocket,sandstorm,sticky");
    public static boolean mc18 = false;
    public static Enable instance;

    public static Enable pl() {
        return instance;
    }

    @Override
    public void onEnable() {
        getLogger().info("=== Enable Start ===");

        int version = Integer.parseInt(ReflectionUtils.PackageType.getServerVersion().split("_")[1]);
        if (version == 8) {
            getLogger().warning("Version 1.8 found, using alternate nametag method.");
            mc18 = true;
        }
        getLogger().info("Checking Configuration File...");
        if (!checkIfConfigExists()) {
            getLogger().info("Configuration file was not found!");
            getLogger().info("Creating config.yml...");
            saveResource("config.yml", true);
        } else {
            getLogger().info("Configuration File Found!");
        }


        getLogger().info("Loading Configuration into memory...");
        Storage.DynamiteConfiguration = getConfig().getConfigurationSection("Creepers.Dynamite");
        Storage.RocketConfiguration = getConfig().getConfigurationSection("Creepers.Rocket");
        Storage.ThrowableCreeperEggs = getConfig().getConfigurationSection("ThrowableCreeperEggs");
        Storage.SandstormConfiguration = getConfig().getConfigurationSection("Creepers.Sandstorm");
        Storage.StickyConfiguration = getConfig().getConfigurationSection("Creepers.Sticky");
        //register commands
        getLogger().info("Registering Commands...");
        getCommand("creeperegg").setExecutor(new CreeperEggCommand());

        //register listeners
        getLogger().info("Registering Listeners...");
        getServer().getPluginManager().registerEvents(new CreeperDeath(), this);
        getServer().getPluginManager().registerEvents(new DynamiteCreeperSpawn(), this);
        getServer().getPluginManager().registerEvents(new DynamiteExplode(), this);
        getServer().getPluginManager().registerEvents(new RocketCreeperSpawn(), this);
        getServer().getPluginManager().registerEvents(new ThrowableCegg(), this);
        getServer().getPluginManager().registerEvents(new SandstormCreeperSpawn(), this);
        getServer().getPluginManager().registerEvents(new SandstormExplode(), this);
        getServer().getPluginManager().registerEvents(new StickyCreeperSpawn(), this);
        getServer().getPluginManager().registerEvents(new StickyCreeperSuffocate(), this);

        instance = this;
        getLogger().info("All Done - Enjoy!");

    }

    @Override
    public void onDisable() {
        getLogger().info("=== Disabled ===");
    }

    private boolean checkIfConfigExists() {
        File configFile = new File(getDataFolder(), "config.yml");
        return configFile.exists();
    }

    public void applyNametag(Entity entity, String nametag, boolean visible) {
        nametag = color(nametag);
        if (mc18 && visible) {
            ArmorStand as = (ArmorStand) entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND); //Spawn the ArmorStand
            as.setVisible(false); // Makes the ArmorStand invisible
            as.setGravity(false); // Make sure it doesn't fall
            as.setCanPickupItems(false); // Disable Items
            as.setCustomName(nametag); // Set this to the text you want
            as.setCustomNameVisible(true); // This makes the text appear no matter if your looking at the entity or not
            entity.setPassenger(as);
            entity.setCustomName(nametag);
            entity.setCustomNameVisible(false);
        } else {
            entity.setCustomName(nametag);
            if (visible) {
                entity.setCustomNameVisible(true);
            }
        }
    }

    //colors a string
    public String color(String line) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        return line;
    }

    public String captializeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    //colors a string list
    public List<String> colorList(List<String> lore) {
        return lore.stream().map(this::color).collect(Collectors.toList());
    }

    public ItemStack createItem(Material material, int amount, short datavalue, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, datavalue);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore(colorList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void doStickyCreeperEffect(Entity entity) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Enable.pl(), () -> {


            if (entity.isDead()
                    || entity.getCustomName() == null
                    || (!Enable.pl().color(Storage.StickyConfiguration.getString("Nametag.Format")).equals(entity.getCustomName())
                    && !Enable.pl().color(Storage.StickyConfiguration.getString("Item.Name")).equals(entity.getCustomName()))) {
                return;
            }


            Location entityLocation = entity.getLocation().add(0, -1, 0);
            Entity armorStandEntity = entity.getWorld().spawnEntity(entityLocation, EntityType.ARMOR_STAND);
            ArmorStand armorStand = (ArmorStand) armorStandEntity;
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setPassenger(entity);
            //just a safe
            armorStandEntity.teleport(entityLocation);
            ConfigurationSection configuration = Storage.StickyConfiguration;
            Enable.pl().applyNametag(entity, configuration.getString("Nametag.Format"), configuration.getBoolean("Nametag.Visible"));


            Bukkit.getScheduler().scheduleSyncDelayedTask(Enable.pl(), () -> {

                if (entity.isDead()) {
                    return;
                }

                entity.getWorld().createExplosion(entityLocation.getX(), entityLocation.getY(), entityLocation.getZ(), Float.parseFloat(configuration.get("Info.Explosion-Power") + ""),
                        configuration.getBoolean("Info.Ignite-Blocks")
                        , configuration.getBoolean("Info.Break-Blocks"));
                List<Entity> entities = entity.getNearbyEntities(2, 2, 2);
                for (Entity nearbyEnt : entities) {
                    if (nearbyEnt.getType() == EntityType.ARMOR_STAND) {
                        nearbyEnt.remove();
                    }
                }


                armorStand.setCustomNameVisible(false);
                armorStand.remove();
                armorStandEntity.remove();
                entity.setCustomNameVisible(false);
                entity.remove();


            }, 10 * 20L);

        }, 5L);
    }


}
