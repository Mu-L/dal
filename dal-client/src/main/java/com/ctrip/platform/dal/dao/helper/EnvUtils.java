package com.ctrip.platform.dal.dao.helper;

/**
 * @author c7ch23en
 */
public interface EnvUtils {

    default String getEnv() {
        return null;
    }

    default String getSubEnv() {
        return null;
    }

    default String getZone() {
        return null;
    }

    default String getIdc() {
        return null;
    }

    default boolean isProd() {
        return false;
    }

    default boolean isLocal() {
        return false;
    }

    default boolean isDalLocal() {
        return false;
    }

}
