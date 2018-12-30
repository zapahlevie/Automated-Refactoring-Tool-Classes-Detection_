package com.finalproject.automated.refactoring.tool.classes.detection.service;

import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public interface ClassesDetection {
    List<ClassModel> detect(@NonNull FileModel fileModel);

    Map<String, List<ClassModel>> detect(@NonNull List<FileModel> fileModels);
}
