package com.ctrip.platform.dal.dao.datasource;

import com.ctrip.platform.dal.dao.datasource.cluster.ConnectionValidator;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.Validator;

public interface ValidatorProxy extends Validator {
    void setPoolProperties(PoolProperties poolProperties);
    void setClusterConnValidator(ConnectionValidator clusterConnValidator);
}