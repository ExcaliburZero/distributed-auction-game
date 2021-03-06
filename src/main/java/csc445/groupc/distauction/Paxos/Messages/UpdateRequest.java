package csc445.groupc.distauction.Paxos.Messages;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * Created by chris on 5/2/18.
 */
public class UpdateRequest extends PaxosMessage {
    private final int entryId;

    public UpdateRequest(final int entryId, final Optional<Integer> receiver, final byte receiverRole) {
        super(receiver, receiverRole, NO_SPECIFIC_ROUND);
        this.entryId = entryId;
    }

    public int getEntryId() {
        return entryId;
    }

    @Override
    public String toString() {
        return "UpdateRequest(entryId = " + entryId + super.toString() + ")";
    }

    @Override
    public byte[] toByteArray() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES * 3 + Byte.BYTES);

        byteBuffer.putInt(UPDATE_REQUEST_OP);
        byteBuffer.putInt(entryId);

        if (receiver.isPresent()) {
            byteBuffer.putInt(receiver.get());
        } else {
            byteBuffer.putInt(EVERYONE_RECEIVES);
        }

        byteBuffer.put(receiverRole);

        return byteBuffer.array();
    }

    public static UpdateRequest fromByteArray(final byte[] bytes) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);

        byteBuffer.put(bytes);
        byteBuffer.rewind();

        byteBuffer.getInt();     // Move past OP code

        final int entryId = byteBuffer.getInt();
        final int possibleReceiver = byteBuffer.getInt();
        final byte receiverRole = byteBuffer.get();

        final Optional<Integer> receiver = (possibleReceiver != EVERYONE_RECEIVES) ? Optional.of(possibleReceiver) : Optional.empty();

        return new UpdateRequest(entryId, receiver, receiverRole);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UpdateRequest that = (UpdateRequest) o;

        return entryId == that.entryId;
    }

    @Override
    public int hashCode() {
        return entryId;
    }
}
