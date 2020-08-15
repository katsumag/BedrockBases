package me.katsumag.bedrockbases

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.extension.input.ParserContext
import com.sk89q.worldedit.history.UndoContext
import com.sk89q.worldedit.math.BlockVector3
import org.bukkit.Location
import java.io.Serializable

/**
 * Create a new serializable block change.
 *
 * @param position the position as a serialized Location {@link Location#serialize()}
 * @param previous the previous BaseBlock as a String {@link BaseBlock#asString()}
 * @param current the current BaseBlock as a String {@link BaseBlock#asString()}
 */

class BlockChange(val serializedLocation: Map<String, Any>, val previous: String, val current: String) : Serializable {

    companion object {

        @JvmStatic
        val serialVersionUID: Long = 836299261983
    }

    fun undo(context: UndoContext) {
        val position = Location.deserialize(serializedLocation)
        checkNotNull(context.extent).setBlock(BlockVector3.at(position.x, position.y, position.z), WorldEdit.getInstance().blockFactory.parseFromInput(previous, ParserContext()))
    }

    fun redo(context: UndoContext) {
        val position = Location.deserialize(serializedLocation)
        checkNotNull(context.extent).setBlock(BlockVector3.at(position.x, position.y, position.z), WorldEdit.getInstance().blockFactory.parseFromInput(current, ParserContext()))
    }

}