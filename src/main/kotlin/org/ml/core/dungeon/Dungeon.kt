package org.ml.core.dungeon

import com.google.inject.Inject
import org.ml.core.config.ConfigService
import java.util.UUID

data class Dungeon(val id: UUID, val name: String)  {}
data class DungeonInstance(val id: UUID, val name: String)  {}

class DungeonService @Inject constructor(
    private val configService: ConfigService
){

}