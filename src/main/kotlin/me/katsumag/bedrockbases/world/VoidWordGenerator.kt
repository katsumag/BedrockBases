package me.katsumag.bedrockbases.world

import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import java.util.*
import javax.annotation.Nonnull

class VoidWordGenerator : ChunkGenerator() {

    @Nonnull
    override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData {
        return createChunkData(world)
    }

}