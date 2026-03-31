package kfiry.academic_system.services;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.CourseGraph;
import kfiry.academic_system.datamodels.Lecturer;
import kfiry.academic_system.datamodels.TimeSlot;

import kfiry.academic_system.repositories.CourseGraphRepository;
import kfiry.academic_system.repositories.CourseRepository;
import kfiry.academic_system.repositories.LecturerRepository;
import kfiry.academic_system.repositories.TimeSlotRepository;

@Service
public class MongoService {
    private CourseRepository courseRepository;
    private CourseGraphRepository courseGraphRepository;
    private LecturerRepository lecturerRepository;
    private TimeSlotRepository timeSlotRepository;

    public MongoService(CourseRepository courseRepository, CourseGraphRepository courseGraphRepository,
            LecturerRepository lecturerRepository, TimeSlotRepository timeSlotRepository) {
        this.courseRepository = courseRepository;
        this.courseGraphRepository = courseGraphRepository;
        this.lecturerRepository = lecturerRepository;
        this.timeSlotRepository = timeSlotRepository;

    }

    public void firstSetUp() {

        // ========== TimeSlot ==========
        TimeSlot time1 = new TimeSlot(DayOfWeek.SUNDAY, 12, 14);
        TimeSlot time2 = new TimeSlot(DayOfWeek.MONDAY, 10, 12);

        timeSlotRepository.insert(time1);
        timeSlotRepository.insert(time2);

        TimeSlot time3 = new TimeSlot(DayOfWeek.SUNDAY, 9, 18);
        TimeSlot time4 = new TimeSlot(DayOfWeek.WEDNESDAY, 11, 18);

        timeSlotRepository.insert(time3);
        timeSlotRepository.insert(time4);

        Set<TimeSlot> timesDavid = new HashSet<>();
        timesDavid.add(time1);
        timesDavid.add(time2);

        Set<TimeSlot> timesYotam = new HashSet<>();
        timesYotam.add(time3);
        timesYotam.add(time4);

        // ========== Lecturer ==========
        Lecturer lecturer1 = new Lecturer("David Gerber", "2327840329", timesDavid);
        Lecturer lecturer2 = new Lecturer("Yotam Eli", "2327840329", timesYotam);

        lecturerRepository.insert(lecturer1);
        lecturerRepository.insert(lecturer2);

        // ========== Course & CourseGraph ==========
        CourseGraph graph = new CourseGraph();

        Course c1 = new Course("Intro Computer Science", "C1CS1", 1, lecturer1, true);
        Course c2 = new Course("C++ Programming", "C1C++2", 2, lecturer2, true, new ArrayList<>(List.of(c1)));
        Course c3 = new Course("Mathematics I", "C1MATH1", 1, lecturer1, true);
        Course c4 = new Course("Algorithms", "C1ALG4", 2, lecturer2, true, new ArrayList<>(List.of(c2, c3)));
        Course c5 = new Course("Data Structures", "C1DS5", 2, lecturer1, true, new ArrayList<>(List.of(c2, c3)));
        Course c6 = new Course("Databases", "C1DB6", 3, lecturer1, true, new ArrayList<>(List.of(c2)));
        Course c7 = new Course("Software Engineering", "C1SE7", 3, lecturer2, true, new ArrayList<>(List.of(c4, c5)));
        Course c8 = new Course("Operating Systems", "C1OS8", 3, lecturer1, true, new ArrayList<>(List.of(c5)));
        Course c9 = new Course("Computer Networks", "C1CN9", 3, lecturer1, true, new ArrayList<>(List.of(c5, c6)));
        Course c10 = new Course("Preparation for the first semester", "C1LL10", 1, lecturer2, false);
        Course c11 = new Course("AI Basics", "C1AI11", 1, lecturer1, true, new ArrayList<>(List.of(c2)));
        Course c12 = new Course("Machine Learning", "C1ML12", 2, lecturer2, true, new ArrayList<>(List.of(c2)));
        Course c13 = new Course("Web Dev", "C1WEB13", 1, lecturer1, true, new ArrayList<>(List.of(c3)));
        Course c14 = new Course("Mobile Apps", "C1MOB14", 1, lecturer2, true, new ArrayList<>(List.of(c3)));
        Course c15 = new Course("Cybersecurity", "C1SEC15", 2, lecturer1, true, new ArrayList<>(List.of(c5)));
        Course c16 = new Course("Cloud Computing", "C1CLOUD16", 1, lecturer2, true, new ArrayList<>(List.of(c5)));
        Course c17 = new Course("Datamining", "C1DM17", 1, lecturer1, true, new ArrayList<>(List.of(c6)));
        Course c18 = new Course("Networks II", "C1NET18", 1, lecturer2, true, new ArrayList<>(List.of(c9)));
        Course c19 = new Course("Advanced DB", "C1ADB19", 1, lecturer1, true, new ArrayList<>(List.of(c6)));
        Course c20 = new Course("Capstone Mini", "C1CAP20", 2, lecturer2, true, new ArrayList<>(List.of(c7)));

        List<Course> Courses = List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18,
                c19, c20);

        courseRepository.insert(Courses);

        for (int i = 0; i < Courses.size(); i++) {
            graph.addCourse(Courses.get(i));
        }

        for (Course c : Courses) {
            for (Course prereq : c.getPrerequisites()) {
                if (Courses.contains(prereq)) {
                    graph.addPrerequisite(prereq, c);
                }
            }
        }

        courseGraphRepository.insert(graph);
    }

    public void initDataIfNeeded() {
        if (courseRepository.count() == 0) {
            firstSetUp();
        }
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public CourseGraph getGraph() {
        return courseGraphRepository.findAll().get(0); // בהנחה שיש אחד
    }
}
