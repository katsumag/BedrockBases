package me.katsumag.bedrockbases

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

fun String.colour() : String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

fun String.debug() = Bukkit.broadcastMessage("&c(!) [DEBUG] (!)\n&b$this".colour())

fun bukkitRunnable(function: BukkitRunnable.() -> Unit): BukkitRunnable {
    return object: BukkitRunnable() {
        override fun run() {
            function()
        }
    }
}