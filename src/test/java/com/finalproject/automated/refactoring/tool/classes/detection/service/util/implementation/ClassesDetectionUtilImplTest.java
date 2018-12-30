package com.finalproject.automated.refactoring.tool.classes.detection.service.util.implementation;

import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassesDetectionUtilImplTest {

    private ClassesDetectionUtilImpl classesDetectionUtil;

    private FileModel fileModel;

    @Before
    public void setUp(){
        classesDetectionUtil = new ClassesDetectionUtilImpl();
        fileModel = FileModel.builder()
                .path("path")
                .filename("Filename.java")
                .content("content")
                .build();
    }

    @Test
    public void getClassKey_success() {
        String key = classesDetectionUtil.getClassKey(fileModel);
        String expectedKey = "path\\Filename.java";

        assertEquals(expectedKey, key);
    }

    @Test(expected = NullPointerException.class)
    public void getClassKey_failed_fileModelIsNull() {
        classesDetectionUtil.getClassKey(null);
    }
}
