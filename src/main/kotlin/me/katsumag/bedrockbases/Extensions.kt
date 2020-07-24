package me.katsumag.bedrockbases

import com.sk89q.worldedit.EditSession
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitWorld
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.ForwardExtentCopy
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.scheduler.BukkitRunnable
import java.io.File

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

fun Location.toWEVector(): BlockVector3 {
    return BlockVector3.at(x, y, z)
}

fun File.loadAndPasteSchematic(source: Location): EditSession {
    val format = ClipboardFormats.findByFile(this) ?: throw IllegalArgumentException("Invalid File Type")

    val clipboard = format.getReader(inputStream()).read()

    val world = source.world ?: throw IllegalArgumentException("No World")

    val session = WorldEdit.getInstance().editSessionFactory.getEditSession(BukkitWorld(world), -1)

    val copy = ForwardExtentCopy(clipboard, clipboard.region, clipboard.origin, session, source.toWEVector())
    Operations.completeLegacy(copy)
    session.flushSession()
    return session
}