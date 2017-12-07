package com.ktanx.jdbc.test.persist;

import com.ktanx.jdbc.persist.KeyGenerator;
import com.ktanx.jdbc.persist.KeyGeneratorFactory;
import com.ktanx.jdbc.persist.UUIDKeyGenerator;
import com.ktanx.jdbc.test.model.UidUser;

/**
 * Created by liyd on 17/6/7.
 */
public class UUIDKeyGeneratorFactory implements KeyGeneratorFactory {

    public Class<?>[] getSupportType() {
        return new Class<?>[]{UidUser.class};
    }

    public KeyGenerator getKeyGenerator() {
        return new UUIDKeyGenerator();
    }
}
