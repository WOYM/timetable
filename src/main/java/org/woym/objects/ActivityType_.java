package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.Room;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-01T15:01:12")
@StaticMetamodel(ActivityType.class)
public abstract class ActivityType_ { 

    public static volatile SingularAttribute<ActivityType, Long> id;
    public static volatile SingularAttribute<ActivityType, Integer> typicalDuration;
    public static volatile SingularAttribute<ActivityType, String> name;
    public static volatile ListAttribute<ActivityType, Room> rooms;

}