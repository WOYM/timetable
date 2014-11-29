package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.Schoolclass;
import org.woym.objects.Subject;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-29T15:26:17")
@StaticMetamodel(AcademicYear.class)
public class AcademicYear_ { 

    public static volatile SingularAttribute<AcademicYear, Long> id;
    public static volatile MapAttribute<AcademicYear, Subject, Integer> subjectDemands;
    public static volatile ListAttribute<AcademicYear, Schoolclass> schoolclasses;
    public static volatile SingularAttribute<AcademicYear, Integer> academicYear;

}