package com.kadioglumf.scheduler.model.generator;

import com.kadioglumf.scheduler.model.EIpType;
import com.kadioglumf.scheduler.utils.UserDeviceDetailsUtils;
import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

public class LoggedUserIpGenerator implements ValueGenerator<String> {
    @Override
    public String generateValue(Session session, Object o) {
        return UserDeviceDetailsUtils.getIpAddr(EIpType.CLIENT);
    }
}
