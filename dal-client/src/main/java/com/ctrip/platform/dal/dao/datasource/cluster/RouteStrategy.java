package com.ctrip.platform.dal.dao.datasource.cluster;

import com.ctrip.framework.dal.cluster.client.util.CaseInsensitiveProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author c7ch23en
 */
public interface RouteStrategy {

    void initialize(Set<HostSpec> configuredHosts, ConnectionFactory connFactory, CaseInsensitiveProperties strategyProperties);

    Connection pickConnection(RequestContext request) throws SQLException;

    default ConnectionValidator getConnectionValidator(){
        return new NullConnectionValidator();
    }

    void destroy();

}
