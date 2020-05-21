package com.github.zhe2128.nextcloudfilestorage.service;

import com.github.zhe2128.nextcloudfilestorage.util.FileInfo;
import com.haulmont.cuba.core.entity.FileDescriptor;
import org.aarboard.nextcloud.api.ServerConfig;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface NextCloud service
 *
 * @author zhe2128
 */
public interface NextCloudService {
    String NAME = "nextcloudfilestorage_NextCloudService";

    /**
     * Checking if a file exists
     *
     * @param fileDescriptor
     * @return
     */
    boolean fileExists(FileDescriptor fileDescriptor);

    /**
     * Upload file from InputStream
     *
     * @param fileDescriptor file descriptor
     * @param inputStream
     * @return
     */
    void saveStream(FileDescriptor fileDescriptor, InputStream inputStream);

    /**
     * Download file
     *
     * @param fileDescriptor file descriptor
     * @return InputStream
     */
    InputStream openStream(FileDescriptor fileDescriptor) throws IOException;

    /**
     * Download file
     *
     * @param fileDescriptor file descriptor
     * @return byte[]
     */
    byte[] loadFile(FileDescriptor fileDescriptor) throws IOException;

    /**
     * Remove file
     *
     * @param fileDescriptor file descriptor
     */
    void removeFile(FileDescriptor fileDescriptor);

    /**
     * Get config NextCloud
     *
     * @return config
     */
    ServerConfig getConfig();

    /**
     * Get source file name
     *
     * @param fileDescriptor file descriptor
     * @return file name
     */
    String getSourceFileName(FileDescriptor fileDescriptor);

    /**
     * Shared file from file descriptor and return public link
     *
     * @param fileDescriptor file descriptor
     * @param width          width image
     * @param height         height image
     * @return public link
     */
    String getSharedLink(FileDescriptor fileDescriptor, Integer width, Integer height);

    /**
     * Shared file from file descriptor and return public link
     *
     * @param fileDescriptor file descriptor
     * @param width          width image
     * @return public link
     */
    String getSharedLinkW(FileDescriptor fileDescriptor, Integer width);

    /**
     * Shared file from file descriptor and return public link
     *
     * @param fileDescriptor file descriptor
     * @param height         height image
     * @return public link
     */
    String getSharedLinkH(FileDescriptor fileDescriptor, Integer height);

    /**
     * Upload file from string base64 encoded
     *
     * @param name          file name
     * @param extension     file extension
     * @param base64encoded byte base64 encoded
     * @param needShare     need share file
     * @param width         width image
     * @param height        height image
     * @return
     */
    FileInfo uploadFile(String name, String extension, String base64encoded, boolean needShare, Integer width, Integer height);
}