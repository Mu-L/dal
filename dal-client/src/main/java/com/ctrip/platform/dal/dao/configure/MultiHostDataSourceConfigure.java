package com.ctrip.platform.dal.dao.configure;

import com.ctrip.platform.dal.dao.datasource.cluster.HostSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author c7ch23en
 */
public class MultiHostDataSourceConfigure extends DataSourceConfigure implements MultiHostConnectionStringConfigure {

    private final List<HostSpec> hosts = new ArrayList<>();
    private final String dbName;
    private String zonesPriority;
    private Long failoverTimeMS;
    private Long blacklistTimeoutMS;
    private Long fixedValidatePeriodMS;

    public MultiHostDataSourceConfigure(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public List<HostSpec> getHosts() {
        return hosts;
    }

    @Override
    public String getDbName() {
        return dbName;
    }

    @Override
    public String getZonesPriority() {
        return zonesPriority;
    }

    @Override
    public Long getFailoverTimeMS() {
        return failoverTimeMS;
    }

    @Override
    public Long getBlacklistTimeoutMS() {
        return blacklistTimeoutMS;
    }

    @Override
    public Long getFixedValidatePeriodMS() {
        return fixedValidatePeriodMS;
    }

    public void addHost(HostSpec host) {
        hosts.add(host);
    }

    public void setZonesPriority(String zonesPriority) {
        this.zonesPriority = zonesPriority;
    }

    public void setFailoverTimeMS(Long failoverTimeMS) {
        this.failoverTimeMS = failoverTimeMS;
    }

    public void setBlacklistTimeoutMS(Long blacklistTimeoutMS) {
        this.blacklistTimeoutMS = blacklistTimeoutMS;
    }

    public void setFixedValidatePeriodMS(Long fixedValidatePeriodMS) {
        this.fixedValidatePeriodMS = fixedValidatePeriodMS;
    }

}
