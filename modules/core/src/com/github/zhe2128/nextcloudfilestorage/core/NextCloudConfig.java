package com.github.zhe2128.nextcloudfilestorage.core;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;

/**
 * Settings for NextCloud file storage integration
 *
 * @author zhe2128
 */
@Source(type = SourceType.DATABASE)
public interface NextCloudConfig extends Config {
    /**
     * @return server name NextCloud
     */
    @Property("NextCloud.serverName")
    String getServerName();

    /**
     * @return port NextCloud
     */
    @Property("NextCloud.port")
    int getPort();

    /**
     * @return username NextCloud
     */
    @Property("NextCloud.username")
    String getUsername();

    /**
     * @return password NextCloud
     */
    @Property("NextCloud.password")
    String getPassword();

    @Property("NextCloud.useHTTPS")
    @DefaultBoolean(false)
    boolean getUseHTTPS();
}
