package me.katsumag.bedrockbases.commands

import me.katsumag.bedrockbases.*
import me.katsumag.bedrockbases.base.Base
import me.katsumag.bedrockbases.base.BaseManager
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@Command("base")
class BaseCommand(private val plugin: BedrockBases) : CommandBase() {

    private val rand = Random()

    @Default
    fun help(sender: CommandSender) {
        plugin.config.getString("help")?.let { sender.sendMessage(it.colour()) }
    }

    @SubCommand("claim")
    @Permission("bedrockbase.claim")
    fun claim(sender: Player) {

        if (BaseManager.hasBase(sender.uniqueId)) {
            plugin.config.getString("base-already-claimed")?.let { sender.sendMessage(it.colour()) }
            return
        }

        bukkitRunnable {

            var location: Location

            do {
                location = Location(Bukkit.getWorld("BedrockBases"), rand.nextInt(29000000).toDouble(), 100.toDouble(), rand.nextInt(29000000).toDouble())
            } while (BaseManager.collidesWithBase(location))

            val base = Base(location.serialize(), sender.uniqueId)

            bukkitRunnable {
                sender.teleport(base.serializedLocation.toLocation())
                plugin.config.getString("teleport")?.let { sender.sendMessage(it.colour()) }
            }.runTaskLater(plugin, 100)

        }.runTaskAsynchronously(plugin)

        plugin.config.getString("base-claimed")?.let { sender.sendMessage(it.colour()) }

    }

    @SubCommand("home")
    @Permission("bedrockbase.home")
    fun home(sender: Player) {
        if (! BaseManager.hasBase(sender.uniqueId)) {
            plugin.config.getString("no-base")?.let { sender.sendMessage(it.colour()) }
            return
        }

        BaseManager.getBase(sender.uniqueId)?.serializedLocation?.let { sender.teleport(it.toLocation()) }
        plugin.config.getString("teleport")?.let { sender.sendMessage(it.colour()) }

    }

    @SubCommand("info")
    @Permission("bedrockbase.info")
    fun info(sender: Player) {

        if (! BaseManager.hasBase(sender.uniqueId)) {
            plugin.config.getString("no-base")?.let { sender.sendMessage(it.colour()) }
            return
        }

        val base = BaseManager.getWithinRange(sender.location)

        plugin.config.getString("info")?.let { sender.sendMessage(it.colour().replace("%player_name%", base.getPlayer().name ?: base.getPlayerUUID().toString()).replace("%claim_date%", base.getClaimDate())) }

    }

    @SubCommand("delete")
    @Permission("bedrockbase.admin")
    fun delete(sender: CommandSender, player: OfflinePlayer) {
        BaseManager.getBase(player.uniqueId)?.let {
            BaseManager.remove(it)
            plugin.config.getString("deleted")?.let { sender.sendMessage(it.colour().replace("%player_name%", player.name ?: player.uniqueId.toString())) }
        }
        plugin.config.getString("deleting")?.let { sender.sendMessage(it.colour().replace("%player_name%", player.name ?: player.uniqueId.toString())) }
    }

    @SubCommand("list")
    @Permission("bedrockbase.admin")
    fun list(sender: CommandSender) {
        BaseManager.baseList.forEach {base ->
            plugin.config.getString("list")?.let {
                sender.sendMessage(it.colour()
                    .replace("%player_name%", base.getPlayer().name ?: "No name recorded")
                    .replace("%player_uuid%", base.getPlayerUUID().toString())
                        .replace("%location_x%", base.serializedLocation.toLocation().x.toString())
                        .replace("%location_y%", base.serializedLocation.toLocation().y.toString())
                        .replace("%location_z%", base.serializedLocation.toLocation().z.toString())
                        .replace("%claim_date%", base.getClaimDate())
            ) }
        }
    }

    @SubCommand("tp")
    @Permission("bedrockbase.admin")
    fun tp(sender: Player, player: OfflinePlayer) {
        if (! BaseManager.hasBase(player.uniqueId)) {
            plugin.config.getString("no-base-tp")?.let { sender.sendMessage(it.colour().replace("%player_name%", player.name ?: player.uniqueId.toString())) }
            return
        }

        plugin.config.getString("base-tp")?.let { sender.sendMessage(it.colour().replace("%player_name%", player.name ?: player.uniqueId.toString())) }
        sender.teleport(BaseManager.getBase(player.uniqueId)!!.serializedLocation.toLocation())

    }

}