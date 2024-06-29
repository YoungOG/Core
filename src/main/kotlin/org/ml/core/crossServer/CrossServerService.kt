package org.ml.core.crossServer

import io.netty.buffer.ByteBuf
import java.util.UUID
import javax.inject.Singleton
import org.ml.core.CorePlugin

// TODO: Rename Network?
// TODO: Make a protocol design/wiki document listing all protocol ids
@Singleton
class CrossServerService {

    private lateinit var corePlugin: CorePlugin

    fun init(corePlugin: CorePlugin): CrossServerService {
        this.corePlugin = corePlugin
        return this
    }

    fun <T : Message> broadcast(message: T) {
        this.send(Channel.ALL, message)
    }

    fun <T : Message> send(channel: Channel, message: T) {}
}

enum class Channel {
    ALL,
    CONFIG,
    MINECRAFT,
    PROXY,
}

abstract class Message(val messageId: Byte) {
    abstract fun encode(buf: ByteBuf)
}

abstract class AutoMessage(messageId: Byte) : Message(messageId) {
    override fun encode(buf: ByteBuf) {
        val fields = this::class.java.declaredFields
        for (field in fields) {

        }
    }
}

class ReloadEpicItemConfigMessage : Message(0) {
    override fun encode(buf: ByteBuf) {}
}

// TODO: This is just an example of manual message, the actual message will be different
class SetPlayerMoneyMessage(val playerId: UUID, val amount: Int) : Message(1) {
    override fun encode(buf: ByteBuf) {
        buf.writeLong(this.playerId.leastSignificantBits)
        buf.writeLong(this.playerId.mostSignificantBits)
        buf.writeInt(amount)
    }
}

// TODO: This is just an example of auto message, the actual message will be different
class PrivateMessagePlayerMessage(val playerId: UUID, val message: String) : AutoMessage(2) {
}
