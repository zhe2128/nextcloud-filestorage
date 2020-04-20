package com.github.zhe2128.nextcloudfilestorage.jmx;

import com.haulmont.cuba.core.sys.jmx.JmxRunAsync;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Interface Shared files to public on NextCloud
 */
@ManagedResource(description = "Shared files to public on NextCloud")
public interface SharedFilesMBean {
    /**
     * Shared file by id
     *
     * @return public link
     */
    @JmxRunAsync
    @ManagedOperation(description = "Shared file by id")
    @ManagedOperationParameters({@ManagedOperationParameter(name = "id", description = "file id")})
    String sharedFileById(String id);
}