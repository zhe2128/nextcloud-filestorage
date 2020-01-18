package com.github.zhe2128.nextcloudfilestorage.core;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

/**
 * Settings for NextCloud file storage integration
 *
 * @author zhe2128
 */
@Source(type = SourceType.DATABASE)
public interface NextCloudConfig extends Config {
    /**
     * @return url NextCloud
     */
    @Property("NextCloud.url")
    String getUrl();

    /**
     * @return root directory for application
     */
    @Property("NextCloud.appName")
    @Default("App")
    String getAppName();

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
}
