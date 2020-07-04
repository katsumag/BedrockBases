package me.katsumag.bedrockbases

import me.katsumag.bedrockbases.commands.BaseCommand
import me.katsumag.bedrockbases.world.VoidWordGenerator
import me.mattstudios.mf.base.CommandManager
import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin

class BedrockBases : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        val commandManager = CommandManager(this)
        commandManager.register(BaseCommand(this))
        server.getWorld("BedrockBases") ?: createWorld()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun createWorld() {
        val creator = WorldCreator("BedrockBases")
        creator.generator(VoidWordGenerator())
        creator.createWorld()
    }

}