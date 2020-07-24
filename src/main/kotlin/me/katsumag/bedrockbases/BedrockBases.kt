package me.katsumag.bedrockbases

import me.katsumag.bedrockbases.base.BaseManager
import me.katsumag.bedrockbases.commands.BaseCommand
import me.katsumag.bedrockbases.world.VoidWordGenerator
import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.TypeResult
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.WorldCreator
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BedrockBases : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        val commandManager = CommandManager(this)

        commandManager.parameterHandler.register(OfflinePlayer::class.java) {
            TypeResult(Bukkit.getOfflinePlayer(it.toString()), it)
        }

        commandManager.register(BaseCommand(this))

        server.getWorld("BedrockBases") ?: createWorld()

        BaseManager.loadEditSessions()


    }

    override fun onDisable() {
        // Plugin shutdown logic
        BaseManager.saveEditSessions()
    }

    private fun createWorld() {
        val creator = WorldCreator("BedrockBases")
        creator.generator(VoidWordGenerator())
        creator.createWorld()
    }

}