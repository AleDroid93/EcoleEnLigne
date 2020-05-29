package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.models.Statistics;

import java.util.ArrayList;

public class StatsViewModel extends ViewModel {
    MutableLiveData<ArrayList<Statistics>> statsLiveData;
    ArrayList<Statistics> statistics;

    public StatsViewModel() {
        this.statsLiveData = new MutableLiveData<>();
        this.statistics = new ArrayList<>();
    }

    public MutableLiveData<ArrayList<Statistics>> getStatsLiveData() {
        return statsLiveData;
    }

    public void clear(){
        this.statistics.clear();
        statsLiveData.setValue(statistics);
    }


    public void addStats(Statistics stats){
        int isPresent = -1;
        for(Statistics st : statistics){
            if(st.getCourseName().equalsIgnoreCase(stats.getCourseName())){
                isPresent = statistics.indexOf(st);
                break;
            }
        }
        if(isPresent >= 0){
            statistics.set(isPresent, stats);
        }else{
            statistics.add(stats);
        }
        statsLiveData.setValue(statistics);
    }
}
