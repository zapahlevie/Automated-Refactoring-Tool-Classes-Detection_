package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.model.IndexModel;
import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassAnalysis;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.classes.detection.service.implementation.utils.TestUtil;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.utils.service.ThreadsWatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.*;
import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassesDetectionThreadImplTest {

    @Autowired
    private ClassesDetectionThreadImpl classesDetectionThread;

    @MockBean
    private ClassAnalysis classAnalysis;

    @MockBean
    private ThreadsWatcher threadsWatcher;

    @Value("${threads.waiting.time}")
    private Integer waitingTime;

    private static final Integer INVOKED_ONCE = 1;

    private FileModel fileModel;

    private List<IndexModel> indexModels;

    @Before
    public void setUp() {
        Future future = TestUtil.getFutureExpectation();

        fileModel = TestUtil.getFileModel();
        indexModels = createIndexModels();

        indexModels.forEach(indexModel ->
                stubClassAnalysis(indexModel, future));

        doNothing().when(threadsWatcher)
                .waitAllThreadsDone(eq(Collections.singletonList(future)), eq(waitingTime));
    }

    @Test
    public void detect_success() {
        Map<String, List<ClassModel>> result = Collections.synchronizedMap(new HashMap<>());
        classesDetectionThread.detect(fileModel, result);

        verifiesClassAnalysis();
        verifyThreadsWatcher();
    }

    @Test(expected = NullPointerException.class)
    public void detect_failed_fileModelIsNull() {
        Map<String, List<ClassModel>> result = Collections.synchronizedMap(new HashMap<>());
        classesDetectionThread.detect(null, result);
    }

    private void stubClassAnalysis(IndexModel indexModel, Future future) {
        when(classAnalysis.analysis(eq(fileModel), eq(indexModel),
                eq(Collections.synchronizedMap(new HashMap<>())))).thenReturn(future);
    }

    private List<IndexModel> createIndexModels() {
        List<IndexModel> indexModels = new ArrayList<>();

        indexModels.add(IndexModel.builder()
                .start(45)
                .end(92)
                .build());

        return indexModels;
    }

    private void verifiesClassAnalysis() {
        indexModels.forEach(this::verifyClassAnalysis);
        verifyNoMoreInteractions(classAnalysis);
    }

    private void verifyClassAnalysis(IndexModel indexModel) {
        verify(classAnalysis, times(INVOKED_ONCE))
                .analysis(eq(fileModel), eq(indexModel), eq(Collections.synchronizedMap(new HashMap<>())));
    }

    private void verifyThreadsWatcher() {
        verify(threadsWatcher, times(INVOKED_ONCE))
                .waitAllThreadsDone(anyList(), eq(waitingTime));
        verifyNoMoreInteractions(threadsWatcher);
    }
}
