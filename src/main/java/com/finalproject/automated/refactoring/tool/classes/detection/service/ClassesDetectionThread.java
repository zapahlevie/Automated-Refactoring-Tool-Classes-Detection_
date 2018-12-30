package com.finalproject.automated.refactoring.tool.classes.detection.service;

import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface ClassesDetectionThread {
    Future detect(@NonNull FileModel fileModel, @NonNull Map<String, List<ClassModel>> result);
}
