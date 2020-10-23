package com.modwiz.chatreadability;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Plugin(name="ChatReadability", version="0.0.1")
@Description("Gives users randomized name colors and alternates chat colors")
@Author("Iris Johnson <modwizcode@github>")
@ApiVersion(ApiVersion.Target.v1_15)
public class ChatReadability extends JavaPlugin {

    // Not configurable for now, uses bungee api colors since I think that's what we need to use TextComponent easily
    private List<ChatColor> messageColorList = List.of(ChatColor.GRAY);
    // Always have a default value in case it's not initialized properly (since we don't check for that)
    private List<ChatColor> nameColorList = List.of(ChatColor.DARK_AQUA);
    //

    @Override
    public void onEnable() {
        // This will copy the default config from the jar if no config exists already
        saveDefaultConfig();
        if (getConfig().getBoolean("enabled")) {
            // Only handle chat events and load remaining config if enabled
            List<String> nameColorStrings = getConfig().getStringList("name-colors");
            // Don't allow empty configs, just keep defaults in that case
            if (nameColorStrings.size() > 0) {
                // Now we convert to color codes, the ChatColor that bungee defines makes this super easy
                // I think this seems nice and clean? Possibly a waste.
                try {
                    nameColorList = nameColorStrings.parallelStream().map(ChatColor::of)
                            .collect(Collectors.toUnmodifiableList());
                } catch (IllegalArgumentException e) {
                    getLogger().log(Level.SEVERE, "Encountered an invalid color value in the name colors", e);
                }
            }
            // Handle "enabling" alternating message colors by adding the second color
            if (getConfig().getBoolean("alternating-colors")) {
                messageColorList = List.of(ChatColor.GRAY, ChatColor.DARK_GRAY);
            }
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }
    }

    public List<ChatColor> getMessageColors() {
        return messageColorList;
    }

    public List<ChatColor> getNameColors() {
        return nameColorList;
    }
}