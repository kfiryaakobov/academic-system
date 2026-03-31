package kfiry.academic_system.utilities;
import java.util.Set;
import java.util.Stack;

import kfiry.academic_system.datamodels.Course;
import kfiry.academic_system.datamodels.CourseGraph;

public class TopologicalHelper {

    public void DFS(CourseGraph graph, Stack<Course> stack, Course current, Set<Course> visited){
        visited.add(current);// mark the current course
        for (Course neighbor : graph.getGraph().get(current)) { // loop on neighbors. graph.getGraph().get(current) -> return list of neighbors 
            if(!visited.contains(neighbor)) // check if we checkes this course
                DFS(graph,stack,neighbor,visited);// recursive
        }
        stack.push(current);
    }
}
