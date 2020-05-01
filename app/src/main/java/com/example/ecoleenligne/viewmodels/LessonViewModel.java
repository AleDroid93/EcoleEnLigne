package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ecoleenligne.models.Lesson;

import java.util.ArrayList;

public class LessonViewModel extends ViewModel {

    MutableLiveData<ArrayList<Lesson>> lessonsLiveData;
    ArrayList<Lesson> lessons;

    public LessonViewModel() {
        this.lessonsLiveData = new MutableLiveData<>();
        this.lessons = new ArrayList<>();
    }

    public MutableLiveData<ArrayList<Lesson>> getLessonsLiveData() {
        return lessonsLiveData;
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
        lessonsLiveData.setValue(lessons);
    }

    public void addAll(ArrayList<Lesson> newLessons){
        lessons.addAll(newLessons);
        lessonsLiveData.setValue(lessons);
    }
}
