package org.woym.objects;

import java.math.BigDecimal;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-29T12:42:57")
@StaticMetamodel(Employee.class)
public abstract class Employee_ { 

    public static volatile ListAttribute<Employee, AcademicYear> guidedAcademicYears;
    public static volatile SingularAttribute<Employee, String> lastName;
    public static volatile SingularAttribute<Employee, String> symbol;
    public static volatile SingularAttribute<Employee, String> firstName;
    public static volatile ListAttribute<Employee, ChargeableCompensation> compensations;
    public static volatile SingularAttribute<Employee, BigDecimal> hoursPerWeek;
    public static volatile ListAttribute<Employee, TimePeriod> timeWishes;
    public static volatile SingularAttribute<Employee, BigDecimal> allocatedHours;
    public static volatile ListAttribute<Employee, Schoolclass> guidedSchoolclasses;
    public static volatile SingularAttribute<Employee, Long> id;
    public static volatile ListAttribute<Employee, ActivityType> possibleActivityTypes;

}