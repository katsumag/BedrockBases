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
        sender.sendMessage("" +
                "/base claim - generates a bedrock base if the player doesnt own one already\n" +
                "/base home - teleports the player to their bedrock base\n" +
                "/base info - show info of the base the player is stood in\n" +
                "     - Base owners name,Claim date,Online status\n" +
                "\n" +
                "ADMIN COMMANDS\n" +
                "/base delete [player] - Admin can remove a players base. \n" +
                "/base list - List all claimed bases with details")
    }

    @SubCommand("claim")
    @Permission("bedrockbase.claim")
    fun claim(sender: Player) {

        if (BaseManager.hasBase(sender.uniqueId)) {
            sender.sendMessage("&4You've already claimed a base!".colour())
            return
        }

        bukkitRunnable {

            var location: Location

            do {
                location = Location(Bukkit.getWorld("BedrockBases"), rand.nextInt(256).toDouble(), 100.toDouble(), rand.nextInt(256).toDouble())
            } while (BaseManager.collidesWithBase(location))

            val base = Base(plugin, location, sender.uniqueId)

            bukkitRunnable {
                sender.teleport(base.getLocation())
                sender.sendMessage("&2You have been teleported to your base!".colour())
            }.runTaskLater(plugin, 100)

        }.runTaskAsynchronously(plugin)

        sender.sendMessage(("&2Base claimed successfully... teleporting soon").colour())

    }

    @SubCommand("home")
    @Permission("bedrockbase.home")
    fun home(sender: Player) {
        if (! BaseManager.hasBase(sender.uniqueId)) {
            sender.sendMessage("&4You haven't claimed a base!".colour())
            return
        }

        BaseManager.getBase(sender.uniqueId)?.getLocation()?.let { sender.teleport(it) }
        sender.sendMessage("&2You have been teleported to your base!".colour())

    }

    @SubCommand("info")
    @Permission("bedrockbase.info")
    fun info(sender: Player) {

        if (! BaseManager.hasBase(sender.uniqueId)) {
            sender.sendMessage("&4You haven't claimed a base!".colour())
            return
        }

        val base = BaseManager.getWithinRange(sender.location)

        sender.sendMessage("This base belongs to ${base.getPlayer().name} \nThis base was claimed on: ${base.getClaimDate()}")

    }

    @SubCommand("delete")
    @Permission("bedrockbase.admin")
    fun delete(sender: CommandSender, player: OfflinePlayer) {
        BaseManager.getBase(player.uniqueId)?.let {
            BaseManager.remove(it)
            sender.sendMessage("&2${player.name}'s base has been deleted!".colour())
        }
        sender.sendMessage("Deleting ${player.name}'s base")
    }

    @SubCommand("list")
    @Permission("bedrockbase.admin")
    fun list(sender: CommandSender) {
        BaseManager.BASE_LIST.forEach {
            sender.sendMessage("Player: ${it.getPlayer().name}\nUUID: ${it.getPlayerUUID()}\nLocation: ${it.getLocation().x}, ${it.getLocation().y}, ${it.getLocation().z}\nClaim Date: ${it.getClaimDate()}")
        }
    }
}