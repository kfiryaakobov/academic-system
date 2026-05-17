package kfiry.academic_system.services;

import jakarta.annotation.PostConstruct;
import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.ScheduleDocument;
import kfiry.academic_system.datamodels.TimeSlot;
import kfiry.academic_system.repositories.CourseRepository;
import kfiry.academic_system.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;
    private CourseRepository courseRepository;
    private CoreService coreService;

    public ScheduleService(ScheduleRepository scheduleRepository, CourseRepository courseRepository,
            CoreService coreService) {
        this.scheduleRepository = scheduleRepository;
        this.courseRepository = courseRepository;
        this.coreService = coreService;
    }

    // ברגע שהאתר עולה ומתחיל לפעול הפעולה הזו מתחילה לעבוד כבר מאחורי הקלעים
    @PostConstruct
    public void initializeSchedule() {
        generateGlobalSchedule();
    }

    // שולפת את כל הקורסים, מריצה את אלגוריתם השיבוץ ומעדכנת את מערכת השעות הגלובלית
    // ממסד הנתונים
    public void generateGlobalSchedule() {
        List<Course> allCourses = courseRepository.findAll();
        Map<Course, TimeSlot> assignments = coreService.runGlobalAlgorithm(allCourses);
        Map<String, String> courseToSlot = new HashMap<>();
        for (Map.Entry<Course, TimeSlot> entry : assignments.entrySet()) {
            Course course = entry.getKey();
            TimeSlot slot = entry.getValue();
            String slotString = slot.getDay() + " " +
                    slot.getStartHour() + ":00-" +
                    slot.getEndHour() + ":00";

            courseToSlot.put(
                    course.getCourseID(),
                    slotString);
        }
        scheduleRepository.deleteAll();
        ScheduleDocument schedule = new ScheduleDocument(courseToSlot);
        scheduleRepository.save(schedule);
        System.out.println("Global schedule created.");
    }

    public ScheduleDocument getSchedule() {
        List<ScheduleDocument> schedules = scheduleRepository.findAll();
        if (schedules.isEmpty()) {
            return null;
        }
        return schedules.get(0);
    }
}