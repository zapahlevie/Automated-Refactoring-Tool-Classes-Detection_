package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassesDetectionThread;
import com.finalproject.automated.refactoring.tool.classes.detection.service.util.ClassesDetectionUtil;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassesDetectionImplTest {

    @Autowired
    private ClassesDetectionImpl classesDetection;

    @MockBean
    private ClassesDetectionThread classesDetectionThread;

    @MockBean
    private ThreadsWatcher threadsWatcher;

    @MockBean
    private ClassesDetectionUtil classesDetectionUtil;

    @Value("${threads.waiting.time}")
    private Integer waitingTime;

    private static final Integer NUMBER_OF_PATH = 3;
    private static final Integer INVOKED_ONCE = 1;

    private FileModel fileModel;

    @Before
    public void setUp() {
        Future future = TestUtil.getFutureExpectation();

        fileModel = FileModel.builder()
                .path("path")
                .filename("Filename.java")
                .content("content")
                .build();

        when(classesDetectionThread.detect(eq(fileModel),
                eq(Collections.synchronizedMap(new HashMap<>())))).thenReturn(future);
        doNothing().when(threadsWatcher)
                .waitAllThreadsDone(eq(Collections.singletonList(future)), eq(waitingTime));
        when(classesDetectionUtil.getClassKey(eq(fileModel)))
                .thenReturn("");
    }

    @Test
    public void detect_singlePath_success() {
        List<ClassModel> result = classesDetection.detect(fileModel);
        assertNull(result);

        verifyClassesDetectionThread(INVOKED_ONCE);
        verifyThreadsWatcher();
        verifyClassesDetectionUtil();
    }

    @Test
    public void detect_multiPath_success() {
        Map<String, List<ClassModel>> result = classesDetection.detect(
                Collections.nCopies(NUMBER_OF_PATH, fileModel));
        assertNotNull(result);

        verifyClassesDetectionThread(NUMBER_OF_PATH);
        verifyThreadsWatcher();
    }

    @Test(expected = NullPointerException.class)
    public void detect_singlePath_failed_pathIsNull() {
        fileModel = null;
        classesDetection.detect(fileModel);
    }

    @Test(expected = NullPointerException.class)
    public void detect_multiPath_failed_listOfPathIsNull() {
        List<FileModel> fileModels = null;
        classesDetection.detect(fileModels);
    }

    private void verifyClassesDetectionThread(Integer invocationsTimes) {
        verify(classesDetectionThread, times(invocationsTimes))
                .detect(eq(fileModel), eq(Collections.synchronizedMap(new HashMap<>())));
        verifyNoMoreInteractions(classesDetectionThread);
    }

    private void verifyThreadsWatcher() {
        verify(threadsWatcher, times(INVOKED_ONCE))
                .waitAllThreadsDone(anyList(), eq(waitingTime));
        verifyNoMoreInteractions(threadsWatcher);
    }

    private void verifyClassesDetectionUtil() {
        verify(classesDetectionUtil, times(INVOKED_ONCE))
                .getClassKey(eq(fileModel));
        verifyNoMoreInteractions(classesDetectionUtil);
    }
}
