package com.github.zhe2128.nextcloudfilestorage.core;


import com.github.zhe2128.nextcloudfilestorage.service.NextCloudService;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * File storage implementation based on NextCloud
 *
 * @author zhe2128
 */
public class NextCloudFileStorage implements FileStorageAPI {
    @Inject
    private NextCloudService nextCloudService;

    @Override
    public long saveStream(FileDescriptor fileDescr, InputStream inputStream) throws FileStorageException {
        Preconditions.checkNotNullArgument(fileDescr.getSize());
        try {
            nextCloudService.saveStream(fileDescr, inputStream);
            return fileDescr.getSize();
        } catch (Exception e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    @Override
    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        try {
            InputStream inputStream = new ByteArrayInputStream(data);
            saveStream(fileDescr, inputStream);
        } catch (Exception e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    @Override
    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        try {
            nextCloudService.removeFile(fileDescr);
        } catch (Exception e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }

    }

    @Override
    public InputStream openStream(FileDescriptor fileDescr) throws FileStorageException {
        try {
            return nextCloudService.openStream(fileDescr);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    @Override
    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        try {
            return nextCloudService.loadFile(fileDescr);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }

    @Override
    public boolean fileExists(FileDescriptor fileDescr) throws FileStorageException {
        try {
            return nextCloudService.fileExists(fileDescr);
        } catch (Exception e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        }
    }
}