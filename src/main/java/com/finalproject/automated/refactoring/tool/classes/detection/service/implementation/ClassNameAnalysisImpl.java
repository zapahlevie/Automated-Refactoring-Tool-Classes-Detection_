package com.finalproject.automated.refactoring.tool.classes.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.model.IndexModel;
import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassNameAnalysis;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ClassNameAnalysisImpl implements ClassNameAnalysis {


    @Override
    public void analysis(@NonNull FileModel fileModel, @NonNull ClassModel classModel, @NonNull IndexModel indexModel) {
        String className = fileModel.getContent().substring(indexModel.getStart(), indexModel.getEnd()).trim();
        String[] list = className.split(" ");
        for(int i = 0; i < list.length; i++){
            if(list[i].equals("class")){
                classModel.setName(list[i+1].replace("{",""));
            }
            else if(list[i].equals("extends")){
                classModel.setExtend(list[i+1].replace("{",""));
            }
            else if(list[i].equals("implements")){
                classModel.setImplement(list[i+1].replace("{",""));
            }
        }
        Integer indexclass = className.indexOf("class");
        List<String> keywords = Arrays.asList(className.substring(0, indexclass - 1).trim().split(" "));
        classModel.setKeywords(keywords);
    }
}
