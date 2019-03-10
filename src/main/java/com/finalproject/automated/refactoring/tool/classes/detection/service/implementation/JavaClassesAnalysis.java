package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.model.IndexModel;
import com.finalproject.automated.refactoring.tool.classes.detection.service.*;
import com.finalproject.automated.refactoring.tool.classes.detection.service.util.ClassesDetectionUtil;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class JavaClassesAnalysis implements ClassAnalysis {

    @Autowired
    private ClassesDetectionUtil classesDetectionUtil;

    @Autowired
    private ClassMethodAnalysis classMethodAnalysis;

    @Autowired
    private ClassNameAnalysis classNameAnalysis;

    @Autowired
    private ClassPackageImportAnalysis classPackageImportAnalysis;

    @Autowired
    private ClassAttributesAnalysis classAttributesAnalysis;

    @Async
    @Override
    public Future analysis(FileModel fileModel, IndexModel indexModel,
                           Map<String, List<ClassModel>> result) {
        ClassModel classModel = ClassModel.builder().build();
        try {
            classPackageImportAnalysis.analysis(fileModel, classModel);
            classNameAnalysis.analysis(fileModel, classModel, indexModel);
            classMethodAnalysis.analysis(fileModel, classModel);
            classAttributesAnalysis.analysis(fileModel, classModel, indexModel);
            saveResult(fileModel, classModel, result);
        } catch (Exception e) {
            // Do nothing
            // Mark of non-class analysis
        }
        return null;
    }

    private void saveResult(FileModel fileModel, ClassModel classModel,
                            Map<String, List<ClassModel>> result) {
        String key = classesDetectionUtil.getClassKey(fileModel);

        if (result.containsKey(key))
            result.get(key).add(classModel);
        else {
            List<ClassModel> classModels = new ArrayList<>();
            classModels.add(classModel);

            result.put(key, classModels);
        }
    }
}
