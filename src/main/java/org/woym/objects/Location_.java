package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.Room;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-02T17:15:59")
@StaticMetamodel(Location.class)
public class Location_ { 

    public static volatile SingularAttribute<Location, Long> id;
    public static volatile SingularAttribute<Location, String> name;
    public static volatile ListAttribute<Location, Room> rooms;

}