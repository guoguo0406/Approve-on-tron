package org.tron.core.capsule;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.tron.common.utils.ByteArray;
//import org.tron.protos.Protocol.Vote;
//import org.tron.protos.Protocol.Votes;
import org.tron.protos.Protocol.Approve;

import java.util.List;

@Slf4j(topic = "capsule")
public class
ApproveCapsule implements ProtoCapsule<Approve> {

  private Approve approve;

  public ApproveCapsule(final Approve approve) {
    this.approve = approve;
  }

  public ApproveCapsule(final byte[] data) {
    try {
      this.approve = Approve.parseFrom(data);
    } catch (InvalidProtocolBufferException e) {
      logger.debug(e.getMessage(), e);
    }
  }

  public ApproveCapsule(ByteString ownerAddress, ByteString approveAddress) {
    this.approve = Approve.newBuilder()
        .setOwnerAddress(ownerAddress)
        .setApproveAddress(approveAddress)
        .build();
  }

  public ByteString getAddress() {
    return this.approve.getOwnerAddress();
  }

  public void setAddress(ByteString address) {
    this.approve = this.approve.toBuilder().setApproveAddress(address).build();
  }

  public byte[] createDbKey() {
    return getAddress().toByteArray();
  }

  public String createReadableString() {
    return ByteArray.toHexString(getAddress().toByteArray());
  }

  @Override
  public byte[] getData() {
    return this.approve.toByteArray();
  }

  @Override
  public Approve getInstance() {
    return this.approve;
  }

}
