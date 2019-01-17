package com.finalproject.automated.refactoring.tool.classes.detection.service;

import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;

public interface ClassMethodAnalysis {

    void analysis(@NonNull FileModel fileModel, @NonNull ClassModel classModel);
}
