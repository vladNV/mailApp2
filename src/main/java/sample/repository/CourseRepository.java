package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String name);

}
