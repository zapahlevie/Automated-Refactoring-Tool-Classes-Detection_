package com.finalproject.automated.refactoring.tool.classes.detection.service;

import com.finalproject.automated.refactoring.tool.classes.detection.model.IndexModel;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface ClassAnalysis {
    Future analysis(@NonNull FileModel fileModel, @NonNull IndexModel indexModel,
                    @NonNull Map<String, List<ClassModel>> result);
}
