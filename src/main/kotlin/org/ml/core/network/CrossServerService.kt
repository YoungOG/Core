package org.ml.core.network

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import java.util.UUID
import javax.inject.Singleton
import org.ml.core.encoding.encodeUUID
import org.ml.core.encoding.encodeValue

// TODO: Rename Network?
// TODO: Make a protocol design/wiki document listing all protocol ids
@Singleton
class CrossServerService {

    fun <T : Message> broadcast(message: T) {
        this.send(Channel.ALL, message)
    }

    fun <T : Message> send(channel: Channel, message: T) {
        // TODO: Check so that ByteBuf it released properly
        val buf = ByteBufAllocator.DEFAULT.buffer()
        message.encode(buf)
        // TODO: Send over Redis/RabbitMQ/Other
    }
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
        encodeValue(buf, this)
    }
}

class ReloadEpicItemConfigMessage : Message(0) {
    override fun encode(buf: ByteBuf) {}
}

// TODO: This is just an example of manual message, the actual message will be different
class SetPlayerMoneyMessage(val playerId: UUID, val amount: Int) : Message(1) {
    override fun encode(buf: ByteBuf) {
        encodeUUID(buf, playerId)
        buf.writeIntLE(amount)
    }
}

// TODO: This is just an example of auto message, the actual message will be different
class PrivateMessagePlayerMessage(val playerId: UUID, val message: String) : AutoMessage(2) {
}
