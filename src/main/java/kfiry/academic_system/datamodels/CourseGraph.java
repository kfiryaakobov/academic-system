package kfiry.academic_system.datamodels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "courseGraph")
public class CourseGraph {
    private Map<Course, List<Course>> adjList = new LinkedHashMap<>();// רשימת שכוניות - כלומר גרף

    // add course to the graph
    public void addCourse(Course course) {
        adjList.putIfAbsent(course, new ArrayList<>());
    }

    // add Edge before -> after (if before not exist -> NULL)
    public void addPrerequisite(Course before, Course after) {
        adjList.get(before).add(after);
    }

    public Map<Course, List<Course>> getGraph() {
        return adjList;
    }

    public void printGraph() {
        for (Course course : adjList.keySet()) {
            List<String> prereqs = new ArrayList<>();

            for (Map.Entry<Course, List<Course>> entry : adjList.entrySet()) {
                if (entry.getValue().contains(course)) {
                    prereqs.add(entry.getKey().getName());
                }
            }

            if (prereqs.isEmpty()) {
                System.out.println("Course \"" + course.getName() + "\" has no prerequisites.");
            } else {
                System.out.print("Course \"" + course.getName() + "\" needs ");
                for (int i = 0; i < prereqs.size(); i++) {
                    System.out.print("\"" + prereqs.get(i) + "\"");
                    if (i != prereqs.size() - 1)
                        System.out.print(" and ");
                }
                System.out.println();
            }
        }
    }

}

