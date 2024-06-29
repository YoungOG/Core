package org.ml.core.config

import cc.ekblad.toml.tomlMapper
import io.netty.buffer.ByteBufAllocator
import net.kyori.adventure.text.TextComponent
import org.ml.core.network.Channel
import org.ml.core.network.CrossServerService
import org.ml.core.network.Message
import javax.inject.Singleton

@Singleton
class ConfigService(network: CrossServerService) {
    val mapper = tomlMapper {  }


    init {
        network.registerHandler(1) {}
    }
}

