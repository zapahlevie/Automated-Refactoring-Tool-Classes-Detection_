package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.model.IndexModel;
import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassAttributesAnalysis;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClassAttributesAnalysisImpl implements ClassAttributesAnalysis {

    private String methodRegex="(?:\\s)*(?:(\\w*)\\s*)?((?:\\()+(?:[@\\w\\[\\]<>\\(\\)=\".,\\s])*(?:\\)))+(?:[\\w,\\s])*(\\{)+(?:\\s)*$";

    @Override
    public void analysis(@NonNull FileModel fileModel, @NonNull ClassModel classModel, @NonNull IndexModel indexModel) {
        List<PropertyModel> attributes = new ArrayList<>();

        Integer startIndex = indexModel.getEnd(), endIndex = 0;

        Pattern methodPattern = Pattern.compile(methodRegex, Pattern.MULTILINE);
        Matcher methodMatcher = methodPattern.matcher(fileModel.getContent());
        while (methodMatcher.find()) {
            endIndex = methodMatcher.start();
            break;
        }

        String rawAttributes = fileModel.getContent().substring(startIndex, endIndex).replace("\n", "").replace("\r", "").trim().replaceAll(" +", " ");
        String[] splitAttributes = rawAttributes.split(";");
        List<String> splitted = new ArrayList<>();
        for(int i = 0; i < (splitAttributes.length - 1); i++){
            splitted.add(splitAttributes[i].trim());
        }
        classModel.setAttributes(splitted);
    }
}
