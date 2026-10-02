package dev.rdh.sarcio.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class NettyCompat {
    private NettyCompat() {
    }

    public static ByteBuf readHeapCopy(ByteBuf from, int length) {
        ByteBuf copy = Unpooled.buffer(length);
        from.readBytes(copy, length);
        return copy;
    }
}
