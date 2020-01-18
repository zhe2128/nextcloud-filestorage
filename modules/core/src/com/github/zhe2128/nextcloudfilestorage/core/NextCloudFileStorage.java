package com.github.zhe2128.nextcloudfilestorage.core;


import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * File storage implementation based on NextCloud
 *
 * @author zhe2128
 */
public class NextCloudFileStorage implements FileStorageAPI {

    @Inject
    protected NextCloudConfig nextCloudConfig;

    @Override
    public long saveStream(FileDescriptor fileDescr, InputStream inputStream) throws FileStorageException {
        Preconditions.checkNotNullArgument(fileDescr.getSize());
        Sardine connection = getWebDavConnection();
        String uploadUrl = getNextCloudUrl() + resolveFileName(fileDescr);
        try {
            if (connection.exists(uploadUrl)) {
                throw new FileStorageException(FileStorageException.Type.MORE_THAN_ONE_FILE, fileDescr.getId().toString());
            }
            createDirectory(fileDescr, connection);
            connection.put(uploadUrl, inputStream);
            connection.shutdown();
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
        return fileDescr.getSize();
    }

    @Override
    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        Preconditions.checkNotNullArgument(fileDescr.getSize());
        saveStream(fileDescr, new ByteArrayInputStream(data));
    }

    @Override
    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        Sardine connection = getWebDavConnection();
        try {
            connection.delete(getNextCloudUrl() + resolveFileName(fileDescr));
            connection.shutdown();
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    @Override
    public InputStream openStream(FileDescriptor fileDescr) throws FileStorageException {
        Sardine connection = getWebDavConnection();
        String downloadUrl = getNextCloudUrl() + resolveFileName(fileDescr);
        try {
            if (!connection.exists(downloadUrl)) {
                throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND,
                        "File not found" + getFileName(fileDescr));
            }
            return connection.get(downloadUrl);

        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    @Override
    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        try (InputStream inputStream = openStream(fileDescr)) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    @Override
    public boolean fileExists(FileDescriptor fileDescr) throws FileStorageException {
        Sardine connection = getWebDavConnection();
        boolean isExists;
        try {
            isExists = connection.exists(getNextCloudUrl() + resolveFileName(fileDescr));
            connection.shutdown();
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
        return isExists;
    }

    /**
     * Create subfolders
     *
     * @param fileDescr file descriptor
     * @throws FileStorageException
     */
    protected void createDirectory(FileDescriptor fileDescr, Sardine connection) throws FileStorageException {
        String[] dirsArray = getStorageDir(fileDescr.getCreateDate()).substring(1).split("/");
        StringBuilder createdDir = new StringBuilder();
        try {
            for (String dir : dirsArray) {
                createdDir.append("/");
                createdDir.append(dir);
                String directoryUrl = getNextCloudUrl() + "/" + createdDir.toString();
                if (!connection.exists(directoryUrl)) {
                    connection.createDirectory(directoryUrl);
                }
            }
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    /**
     * Get NextCloud API url
     *
     * @return API url
     */
    protected String getNextCloudUrl() {
        String url = nextCloudConfig.getUrl();
        Preconditions.checkNotEmptyString(url, "Url for NextCloud is required");
        return (url.lastIndexOf(url.length() - 1) != '/' ? url : url.substring(url.length() - 1))
                + "/remote.php/dav/files/" + nextCloudConfig.getUsername();
    }

    /**
     * Get file name
     *
     * @param fileDescriptor file descriptor
     * @return file name
     */
    protected String getFileName(FileDescriptor fileDescriptor) {
        if (StringUtils.isNotBlank(fileDescriptor.getExtension())) {
            return fileDescriptor.getId().toString() + "." + fileDescriptor.getExtension();
        } else {
            return fileDescriptor.getId().toString();
        }
    }

    /**
     * @return WebDav connection wrapper
     */
    protected Sardine getWebDavConnection() {
        Preconditions.checkNotEmptyString(nextCloudConfig.getUsername(), "Username for NextCloud is required");
        Preconditions.checkNotEmptyString(nextCloudConfig.getPassword(), "Password for NextCloud is required");
        return SardineFactory.begin(nextCloudConfig.getUsername(), nextCloudConfig.getPassword());
    }


    /**
     * Generate path directory
     *
     * @param createDate date
     * @return path directory
     */
    protected String getStorageDir(Date createDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return "/" + nextCloudConfig.getAppName() + "/" + String.format("%d/%s/%s",
                year, StringUtils.leftPad(String.valueOf(month), 2, '0'), StringUtils.leftPad(String.valueOf(day), 2, '0'));
    }

    /**
     * Get the path to file
     *
     * @param fileDescr file descriptor
     * @return path to file
     */
    protected String resolveFileName(FileDescriptor fileDescr) {
        return getStorageDir(fileDescr.getCreateDate()) + "/" + getFileName(fileDescr);
    }
}