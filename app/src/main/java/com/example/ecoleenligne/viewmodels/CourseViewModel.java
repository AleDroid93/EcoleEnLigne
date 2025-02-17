package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ecoleenligne.models.Course;

import java.util.ArrayList;

public class CourseViewModel extends ViewModel {
    MutableLiveData<ArrayList<Course>> coursesLiveData;
    ArrayList<Course> courses;

    public CourseViewModel() {
        this.coursesLiveData = new MutableLiveData<>();
        this.courses = new ArrayList<>();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public MutableLiveData<ArrayList<Course>> getCoursesLiveData() {
        return coursesLiveData;
    }

    public void addCourse(Course course){
        for(Course c : courses){
            if(c.getName().equals(course.getName())){
                return;
            }
        }
        courses.add(course);
        coursesLiveData.setValue(courses);
    }

    public boolean isEmpty(){
        return coursesLiveData.getValue().isEmpty();
    }

    public void clear(){
        courses.clear();
        coursesLiveData.setValue(courses);
    }

    public Course getCourse(String courseName){
        for(Course course : courses){
            if(course.getName().equals(courseName))
                return course;
        }
        return null;
    }

}
