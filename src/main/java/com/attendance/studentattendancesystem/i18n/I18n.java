package com.attendance.studentattendancesystem.i18n;

import java.util.HashMap;
import java.util.Map;

/** Lightweight in-memory translations (EN / UR / ZH). */
public final class I18n {
    private static Language language = Language.ENGLISH;
    private static final Map<String, String[]> TEXTS = new HashMap<>();

    static {
        put("app.title", "Student Attendance System", "طلبہ حاضری سسٹم", "学生考勤系统");
        put("app.subtitle", "Manage Students  •  Track Attendance", "طلبہ کا انتظام  •  حاضری ریکارڈ", "管理学生  •  跟踪考勤");
        put("welcome", "Welcome\nAdministrator", "خوش آمدید\nایڈمنسٹریٹر", "欢迎\n管理员");
        put("nav.dashboard", "Dashboard", "ڈیش بورڈ", "仪表盘");
        put("nav.students", "Students", "طلبہ", "学生");
        put("nav.attendance", "Attendance", "حاضری", "考勤");
        put("nav.classes", "Classes", "کلاسز", "课程");
        put("nav.refresh", "Refresh", "ریفریش", "刷新");
        put("side.brand", "Attendance\nSystem", "حاضری\nسسٹم", "考勤\n系统");
        put("side.quote", "Better Attendance\nBrighter Future", "بہتر حاضری\n روشن مستقبل", "更好的出勤\n更光明的未来");
        put("dashboard.title", "Dashboard", "ڈیش بورڈ", "仪表盘");
        put("dashboard.subtitle", "Overview of students, courses, classes, and attendance",
                "طلبہ، کورسز، کلاسز اور حاضری کا جائزہ", "学生、课程、课时与考勤概览");
        put("dashboard.overview", "Overview", "جائزہ", "概览");
        put("stat.students", "TOTAL STUDENTS", "کل طلبہ", "学生总数");
        put("stat.courses", "TOTAL COURSES", "کل کورسز", "课程总数");
        put("stat.classes", "TOTAL CLASSES", "کل کلاسز", "课时总数");
        put("stat.rate", "ATTENDANCE RATE", "حاضری کی شرح", "出勤率");
        put("stat.students.sub", "Registered students", "رجسٹرڈ طلبہ", "已注册学生");
        put("stat.courses.sub", "Active courses", "فعال کورسز", "活动课程");
        put("stat.classes.sub", "Scheduled classes", "طے شدہ کلاسز", "已安排课时");
        put("stat.rate.sub", "Overall attendance", "مجموعی حاضری", "总体出勤");
        put("dashboard.info.title", "Attendance Overview", "حاضری کا جائزہ", "考勤概览");
        put("dashboard.info",
                "Use the navigation to manage students, courses, classes, and attendance records. The statistics on this dashboard are calculated directly from your system data.",
                "نیویگیشن سے طلبہ، کورسز، کلاسز اور حاضری کا انتظام کریں۔ اعداد و شمار براہ راست آپ کے ڈیٹا سے نکالے جاتے ہیں۔",
                "通过导航管理学生、课程、课时与考勤记录。仪表盘统计直接来自系统数据。");
        put("lang.label", "Language", "زبان", "语言");
        put("period", "Period", "پیریڈ", "节次");
        put("attendance.percent", "Attendance %", "حاضری %", "出勤率 %");
        put("student.id", "Student ID", "طالب علم آئی ڈی", "学号");
        put("student.name", "Student Name", "طالب علم کا نام", "姓名");
    }

    private I18n() {
    }

    private static void put(String key, String en, String ur, String zh) {
        TEXTS.put(key, new String[]{en, ur, zh});
    }

    public static void setLanguage(Language lang) {
        language = lang == null ? Language.ENGLISH : lang;
    }

    public static Language getLanguage() {
        return language;
    }

    public static String t(String key) {
        String[] values = TEXTS.get(key);
        if (values == null) {
            return key;
        }
        return switch (language) {
            case URDU -> values[1];
            case CHINESE -> values[2];
            default -> values[0];
        };
    }
}
