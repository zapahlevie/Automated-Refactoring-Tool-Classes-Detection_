package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassesDetection;
import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassesDetectionThread;
import com.finalproject.automated.refactoring.tool.classes.detection.service.util.ClassesDetectionUtil;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.utils.service.ThreadsWatcher;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class ClassesDetectionImpl implements ClassesDetection {

    @Autowired
    private ClassesDetectionThread classesDetectionThread;

    @Autowired
    private ThreadsWatcher threadsWatcher;

    @Autowired
    private ClassesDetectionUtil classesDetectionUtil;

    @Value("${threads.waiting.time}")
    private Integer waitingTime;

    @Override
    public List<ClassModel> detect(@NonNull FileModel fileModel) {
        return detect(Collections.singletonList(fileModel))
                .get(classesDetectionUtil.getClassKey(fileModel));
    }

    @Override
    public Map<String, List<ClassModel>> detect(@NonNull List<FileModel> fileModels) {
        Map<String, List<ClassModel>> result = Collections.synchronizedMap(new HashMap<>());
        List<Future> threads = doClassesDetection(fileModels, result);

        threadsWatcher.waitAllThreadsDone(threads, waitingTime);

        return result;
    }

    private List<Future> doClassesDetection(List<FileModel> fileModels, Map<String, List<ClassModel>> result) {
        return fileModels.stream()
                .map(fileModel -> classesDetectionThread.detect(fileModel, result))
                .collect(Collectors.toList());
    }
}
