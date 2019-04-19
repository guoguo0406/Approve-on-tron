package org.tron.core.db;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tron.core.capsule.ApproveCapsule;

@Component
public class ApproveStore extends TronStoreWithRevoking<ApproveCapsule> {

  @Autowired
  public ApproveStore(@Value("approve") String dbName) {
    super(dbName);
  }

  @Override
  public ApproveCapsule get(byte[] key) {
    byte[] value = revokingDB.getUnchecked(key);
    return ArrayUtils.isEmpty(value) ? null : new ApproveCapsule(value);
  }
}