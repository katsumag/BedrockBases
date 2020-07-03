package me.katsumag.bedrockbases

import me.katsumag.bedrockbases.world.VoidWordGenerator
import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin

class BedrockBases : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic

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