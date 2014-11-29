package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-29T12:42:56")
@StaticMetamodel(AcademicYear.class)
public class AcademicYear_ { 

    public static volatile MapAttribute<AcademicYear, Subject, Integer> subjectDemands;
    public static volatile SingularAttribute<AcademicYear, Integer> academicYear;
    public static volatile ListAttribute<AcademicYear, Schoolclass> schoolclasses;
    public static volatile SingularAttribute<AcademicYear, Long> id;

}