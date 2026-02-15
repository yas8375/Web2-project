package com.example.movies_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity // 1. تخبر سبرنق أن هذا الكلاس يمثل جدولاً في الداتا بيس
@Table(name = "movies") // 2. اسم الجدول بالضبط كما هو في بوستجرس
public class Movie {

    @Id // 3. المفتاح الأساسي للجدول
    @Column(name = "id") // 4. ربط المتغير بعمود id
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "year")
    private Integer year;

    @Column(name = "director")
    private String director;

    // --- (Constructors) ---
    // سبرنق يحتاج لكونستركتور فارغ دائماً
    public Movie() {
    }

    public Movie(String id, String title, Integer year, String director) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
    }

    // --- (Getters and Setters) ---
    // هذه الدوال تسمح للسبرنق بقراءة وكتابة البيانات في المتغيرات

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}