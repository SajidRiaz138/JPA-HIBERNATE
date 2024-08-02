package org.hibernate.relations.manytomany;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NaturalIdCache;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManyToManyRelationJointTableHasOwnColumns
{
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class StudentCourseId implements Serializable
    {
        // Composite Key

        @Column (name = "student_id")
        private Long studentId;
        @Column (name = "course_id")
        private Long courseId;
    }

    @Entity (name = "Enrollment")
    @Table (name = "enrollment")
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode (exclude = { "student", "course", "enrollmentDate", "grade" })
    public static class Enrollment implements Serializable
    {
        // Join Table

        @EmbeddedId
        private StudentCourseId id;

        @Column (name = "enrolled_on")
        private final LocalDate enrollmentDate = LocalDate.now();
        @Column (name = "student_grade")
        private String grade;

        @ManyToOne (fetch = FetchType.LAZY)
        @MapsId ("studentId")
        private Student student;

        @ManyToOne (fetch = FetchType.LAZY)
        @MapsId ("courseId")
        private Course course;

        public Enrollment(Student student, Course course)
        {
            this.student = student;
            this.course = course;
            this.id = new StudentCourseId(student.getId(), course.getId());
        }
    }

    @Entity (name = "Student")
    @Table (name = "student")
    @NoArgsConstructor
    @RequiredArgsConstructor
    @Getter
    @Setter
    public static class Student
    {
        // Assume User interacts with Student so it is controlling side

        @Id
        private Long id;
        @NonNull
        private String email;
        @NonNull
        private String name;

        @OneToMany (mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
        private final List<Enrollment> enrollments = new ArrayList<>();

        public void addCourse(Course course)
        {
            Enrollment enrollment = new Enrollment(this, course);
            enrollments.add(enrollment);
            course.getEnrollments().add(enrollment);
        }

        public void removeCourse(Course course)
        {
            enrollments.stream()
                    .filter(enrollment -> enrollment.getStudent().equals(this) &&
                            enrollment.getCourse().equals(course))
                    .findFirst()
                    .ifPresent(enrollment ->
                    {
                        enrollments.remove(enrollment);
                        enrollment.getCourse().getEnrollments().remove(enrollment);
                        enrollment.setStudent(null);
                        enrollment.setCourse(null);
                    });
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (!(o instanceof Student)) return false;
            return id != null && id.equals(((Student) o).getId());
        }

        @Override
        public int hashCode()
        {
            return getClass().hashCode();
        }
    }

    @Entity (name = "Course")
    @Table (name = "course")
    @NaturalIdCache
    @Cache (usage = CacheConcurrencyStrategy.READ_WRITE)
    @EqualsAndHashCode (exclude = { "id", "courseDescription", "credits", "enrollments" })
    @RequiredArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Course
    {
        // One Curse has May Enrollments. One to Many  Course ---> Enrollments
        // @NaturalId or Business key course name or course code used for cache and retrieval
        // The @NaturalIdCache directs Hibernate to cache the entity identifier associated with a given business key.
        // With these annotations in place, we can fetch the Course entity without needing to hit the database

        @Id
        @GeneratedValue
        private Long id;

        @NonNull
        @NaturalId
        private String courseName;
        @NonNull
        private String courseDescription;
        @NonNull
        private int credits;

        @OneToMany (mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
        private final List<Enrollment> enrollments = new ArrayList<>();
    }
}
