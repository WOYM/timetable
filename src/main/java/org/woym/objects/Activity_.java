package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-29T12:42:56")
@StaticMetamodel(Activity.class)
public class Activity_ { 

    public static volatile ListAttribute<Activity, Room> rooms;
    public static volatile ListAttribute<Activity, Schoolclass> schoolclasses;
    public static volatile SingularAttribute<Activity, Long> id;
    public static volatile SingularAttribute<Activity, TimePeriod> time;
    public static volatile SingularAttribute<Activity, ActivityType> type;
    public static volatile MapAttribute<Activity, Employee, EmployeeTimePeriods> employees;

}