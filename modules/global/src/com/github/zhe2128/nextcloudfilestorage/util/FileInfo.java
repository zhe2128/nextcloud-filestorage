package com.github.zhe2128.nextcloudfilestorage.util;

import java.util.UUID;

public class FileInfo {
    /**
     * FileDescriptor ID
     */
    private UUID id;
    /**
     * Direct file link in NextCloud
     */
    private String previewUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
