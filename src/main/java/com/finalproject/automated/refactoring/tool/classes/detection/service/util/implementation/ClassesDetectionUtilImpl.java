package com.finalproject.automated.refactoring.tool.classes.detection.service.util.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.service.util.ClassesDetectionUtil;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ClassesDetectionUtilImpl implements ClassesDetectionUtil {

    private static final String KEY_DIVIDER = "\\";

    @Override
    public String getClassKey(@NonNull FileModel fileModel) {
        return fileModel.getPath() + KEY_DIVIDER + fileModel.getFilename();
    }
}
