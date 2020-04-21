package com.github.zhe2128.nextcloudfilestorage.jmx;

import com.github.zhe2128.nextcloudfilestorage.service.NextCloudService;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.app.Authenticated;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

@Component("nextcloudfilestorage_SharedFiles")
public class SharedFiles implements SharedFilesMBean {
    @Inject
    DataManager dataManager;

    @Inject
    NextCloudService nextCloudService;

    @Authenticated
    @Override
    public String sharedFileById(String id) {
        Preconditions.checkNotEmptyString(id, "Id is required");
        FileDescriptor fileDescriptors = dataManager.load(FileDescriptor.class)
                .query("select fd from sys$FileDescriptor fd where fd.id = :id")
                .parameter("id", UUID.fromString(id))
                .one();
        if (fileDescriptors != null) {
            return nextCloudService.getSharedLink(fileDescriptors);
        } else {
            return "File not found with id " + id;
        }
    }
}