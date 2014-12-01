package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.LessonType;
import org.woym.objects.Schoolclass;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-01T15:01:12")
@StaticMetamodel(AcademicYear.class)
public class AcademicYear_ { 

    public static volatile SingularAttribute<AcademicYear, Long> id;
    public static volatile ListAttribute<AcademicYear, Schoolclass> schoolclasses;
    public static volatile MapAttribute<AcademicYear, LessonType, Integer> lessonDemands;
    public static volatile SingularAttribute<AcademicYear, Integer> academicYear;

}