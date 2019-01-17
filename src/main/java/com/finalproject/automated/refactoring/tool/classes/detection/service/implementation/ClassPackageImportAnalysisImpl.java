package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassPackageImportAnalysis;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClassPackageImportAnalysisImpl implements ClassPackageImportAnalysis {

    private String packageRegex=".*package\\s+(\\w+.)+$";
    private String importRegex=".*import\\s+(\\w+.)+$";

    @Override
    public void analysis(@NonNull FileModel fileModel, @NonNull ClassModel classModel) {
        Pattern packagePattern = Pattern.compile(packageRegex, Pattern.MULTILINE);
        Pattern importPattern = Pattern.compile(importRegex, Pattern.MULTILINE);
        Matcher packageMatcher = packagePattern.matcher(fileModel.getContent());
        Matcher importMatcher = importPattern.matcher(fileModel.getContent());
        List<String> imports = new ArrayList<>();
        while (packageMatcher.find()) {
            classModel.setPackageName(fileModel.getContent().substring(packageMatcher.start(), packageMatcher.end()).replace(";","").trim());
        }
        while (importMatcher.find()) {
            imports.add(fileModel.getContent().substring(importMatcher.start(), importMatcher.end()).replace(";","").trim());
        }
        classModel.setImports(imports);
    }
}
