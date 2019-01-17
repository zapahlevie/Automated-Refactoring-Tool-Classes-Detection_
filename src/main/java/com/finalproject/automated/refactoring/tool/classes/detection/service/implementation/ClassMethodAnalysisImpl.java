package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassMethodAnalysis;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.methods.detection.service.MethodsDetection;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassMethodAnalysisImpl implements ClassMethodAnalysis {

    @Autowired
    private MethodsDetection methodsDetection;

    @Override
    public void analysis(@NonNull FileModel fileModel, @NonNull ClassModel classModel) {
        List<MethodModel> methods = methodsDetection.detect(fileModel);
        classModel.setMethodModels(methods);
    }
}
