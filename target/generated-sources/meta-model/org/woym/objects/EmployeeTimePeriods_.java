package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.TimePeriod;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-29T15:26:17")
@StaticMetamodel(EmployeeTimePeriods.class)
public class EmployeeTimePeriods_ { 

    public static volatile SingularAttribute<EmployeeTimePeriods, Long> id;
    public static volatile ListAttribute<EmployeeTimePeriods, TimePeriod> timePeriods;

}