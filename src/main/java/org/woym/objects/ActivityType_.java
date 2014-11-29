package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-29T12:42:56")
@StaticMetamodel(ActivityType.class)
public abstract class ActivityType_ { 

    public static volatile ListAttribute<ActivityType, Room> rooms;
    public static volatile SingularAttribute<ActivityType, Integer> typicalDuration;
    public static volatile SingularAttribute<ActivityType, String> name;
    public static volatile SingularAttribute<ActivityType, Long> id;

}