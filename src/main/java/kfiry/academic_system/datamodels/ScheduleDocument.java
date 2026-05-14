package kfiry.academic_system.datamodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "Schedule")
public class ScheduleDocument {

    @Id
    private String id;
    private Map<String, String> courseToSlot;

    public ScheduleDocument() {
        
    }

    public ScheduleDocument(Map<String, String> courseToSlot) {
        this.courseToSlot = courseToSlot;
    }

    public Map<String, String> getCourseToSlot() {
        return courseToSlot;
    }

    public void setCourseToSlot(Map<String, String> courseToSlot) {
        this.courseToSlot = courseToSlot;
    }
}