package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.models.Chapter;

import java.util.ArrayList;

public class ChapterViewModel extends ViewModel {
    MutableLiveData<ArrayList<Chapter>> chaptersLiveData;
    ArrayList<Chapter> chapters;

    public ChapterViewModel() {
        this.chaptersLiveData = new MutableLiveData<>();
        this.chapters = new ArrayList<>();
    }

    public MutableLiveData<ArrayList<Chapter>> getChaptersLiveData() {
        return chaptersLiveData;
    }

    public void addChapter(Chapter chapter){
        chapters.add(chapter);
        chaptersLiveData.setValue(chapters);
    }
}
