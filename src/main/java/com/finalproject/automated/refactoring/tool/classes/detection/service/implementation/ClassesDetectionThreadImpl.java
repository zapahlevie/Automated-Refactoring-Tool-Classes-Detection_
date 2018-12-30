package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.model.IndexModel;
import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassAnalysis;
import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassesDetectionThread;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.utils.service.ThreadsWatcher;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClassesDetectionThreadImpl implements ClassesDetectionThread {

    @Autowired
    private ClassAnalysis classAnalysis;

    @Autowired
    private ThreadsWatcher threadsWatcher;

    @Value("${threads.waiting.time}")
    private Integer waitingTime;

    @Value("${classes.detection.regex}")
    private String classesRegex;

    @Async
    @Override
    public Future detect(@NonNull FileModel fileModel, @NonNull Map<String, List<ClassModel>> result) {
        List<IndexModel> indexOfClasses = getIndexOfClasses(fileModel.getContent());
        List<Future> threads = doAnalysisClasses(indexOfClasses, fileModel, result);

        threadsWatcher.waitAllThreadsDone(threads, waitingTime);

        return null;
    }

    private List<IndexModel> getIndexOfClasses(String content) {
        Pattern pattern = Pattern.compile(classesRegex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);

        return findIndex(matcher);
    }

    private List<IndexModel> findIndex(Matcher matcher) {
        List<IndexModel> indexModels = new ArrayList<>();

        while (matcher.find()) {
            saveIndex(matcher, indexModels);
        }

        return indexModels;
    }

    private void saveIndex(Matcher matcher, List<IndexModel> indexModels) {
        IndexModel indexModel = IndexModel.builder()
                .start(matcher.start())
                .end(matcher.end())
                .build();

        indexModels.add(indexModel);
    }

    private List<Future> doAnalysisClasses(List<IndexModel> indexOfClasses, FileModel fileModel, Map<String, List<ClassModel>> result) {
        return indexOfClasses.stream()
                .map(indexOfClass -> classAnalysis.analysis(fileModel, indexOfClass, result))
                .collect(Collectors.toList());
    }
}
