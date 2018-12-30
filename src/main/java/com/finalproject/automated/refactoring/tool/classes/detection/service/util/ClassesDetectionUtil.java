package com.finalproject.automated.refactoring.tool.classes.detection.service.util;

import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import lombok.NonNull;

public interface ClassesDetectionUtil {
    String getClassKey(@NonNull FileModel fileModel);
}
