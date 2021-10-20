package com.github.manafia.factioncreepers.commands;

import com.github.manafia.factioncreepers.Enable;
import com.github.manafia.factioncreepers.Storage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreeperEggCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("factioncreepers.give")) {
            sender.sendMessage(Enable.pl().color("&c(!) Insufficient Permissions!"));
            return true;
        }

        if (args.length == 4) {
            if (!args[0].equalsIgnoreCase("give")) {
                return true;
            }

            if (Enable.creeperTypes.contains(args[1].toLowerCase())) {
                sender.sendMessage(Enable.pl().color("&c(!) &7This is an invalid creeper type!"));
                return true;
            }

            if (Bukkit.getPlayer(args[3]) == null || !Bukkit.getPlayer(args[3]).isOnline()) {
                sender.sendMessage(Enable.pl().color("&c(!) &7This player is not online!"));
                return true;
            }
            try {
                int num = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Enable.pl().color("&c(!) &7The amount needs to be a number!"));
                return true;
            }

            String typeArg = args[1].toLowerCase();
            int amount = Integer.parseInt(args[2]);
            Player player = Bukkit.getPlayer(args[3]);

            ItemStack creeperEgg = new ItemStack(Material.MONSTER_EGG);
            if (typeArg.equalsIgnoreCase("dynamite")) {
                creeperEgg = createEgg(Storage.DynamiteConfiguration, amount);
            } else if (typeArg.equalsIgnoreCase("rocket")) {
                creeperEgg = createEgg(Storage.RocketConfiguration, amount);
            } else if (typeArg.equalsIgnoreCase("sandstorm")) {
                creeperEgg = createEgg(Storage.SandstormConfiguration, amount);
            } else if (typeArg.equalsIgnoreCase("sticky")) {
                creeperEgg = createEgg(Storage.StickyConfiguration, amount);
            }
            player.sendMessage(Enable.pl().color("&c(!) &7You have received a special creeper egg!"));
            sender.sendMessage(Enable.pl().color("&c(!) &7" + player.getName() + " has received a special creeper egg!"));
            player.getInventory().addItem(creeperEgg);
            return true;

        }


        sender.sendMessage(Enable.pl().color("&c(!) FactionCreepers"));
        sender.sendMessage(Enable.pl().color("&c(!) /creeperegg give <type> <amount> <player> - &7Give a player a creeper egg."));
        sender.sendMessage(Enable.pl().color("&cCreeper Types"));
        for (String type : Enable.creeperTypes) {
            sender.sendMessage(Enable.pl().color("&c" + type));
        }
        return true;
    }

    private ItemStack createEgg(ConfigurationSection configurationSection, int amount) {
        String itemName = configurationSection.getString("Item.Name");
        List<String> itemLore = configurationSection.getStringList("Item.Lore");
        return Enable.pl().createItem(Material.MONSTER_EGG, amount, (short) 50, itemName, itemLore);
    }

}
