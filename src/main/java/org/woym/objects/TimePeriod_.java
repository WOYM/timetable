package org.woym.objects;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.Week;
import org.woym.objects.Weekday;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-02T17:15:59")
@StaticMetamodel(TimePeriod.class)
public class TimePeriod_ { 

    public static volatile SingularAttribute<TimePeriod, Date> startTime;
    public static volatile SingularAttribute<TimePeriod, Integer> duration;
    public static volatile SingularAttribute<TimePeriod, Weekday> day;
    public static volatile SingularAttribute<TimePeriod, Date> endTime;
    public static volatile SingularAttribute<TimePeriod, Week> week;

}