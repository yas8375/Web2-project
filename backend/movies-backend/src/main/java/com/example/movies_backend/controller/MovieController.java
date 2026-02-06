package com.example.movies_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping; // 👈 استيراد جديد
import org.springframework.web.bind.annotation.RestController; // 👈 استيراد جديد

@RestController // 1. هذا السطر يجعل الملف "كنترولر" وليس ملف عادي
@RequestMapping("/api/v1/movies") // 2. هذا السطر هو الذي يحدد رابط الصفحة
public class MovieController {

    @GetMapping
    public String getAllMovies() {
        System.out.println("🚨 تنبيه: هناك مستخدم طلب قائمة الأفلام الآن!");
        return "أهلاً بك! هنا ستظهر قائمة الأفلام قريباً 🎬";
    }
}