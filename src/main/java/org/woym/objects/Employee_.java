package org.woym.objects;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.AcademicYear;
import org.woym.objects.ActivityType;
import org.woym.objects.ChargeableCompensation;
import org.woym.objects.Schoolclass;
import org.woym.objects.TimePeriod;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-01T15:01:12")
@StaticMetamodel(Employee.class)
public abstract class Employee_ { 

    public static volatile SingularAttribute<Employee, Long> id;
    public static volatile ListAttribute<Employee, Schoolclass> guidedSchoolclasses;
    public static volatile SingularAttribute<Employee, String> lastName;
    public static volatile ListAttribute<Employee, TimePeriod> timeWishes;
    public static volatile SingularAttribute<Employee, BigDecimal> allocatedHours;
    public static volatile SingularAttribute<Employee, BigDecimal> hoursPerWeek;
    public static volatile SingularAttribute<Employee, String> symbol;
    public static volatile ListAttribute<Employee, AcademicYear> guidedAcademicYears;
    public static volatile ListAttribute<Employee, ActivityType> possibleActivityTypes;
    public static volatile SingularAttribute<Employee, String> firstName;
    public static volatile ListAttribute<Employee, ChargeableCompensation> compensations;

}