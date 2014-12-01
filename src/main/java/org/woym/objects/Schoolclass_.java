package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.LessonType;
import org.woym.objects.Room;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-12-01T15:01:12")
@StaticMetamodel(Schoolclass.class)
public class Schoolclass_ { 

    public static volatile SingularAttribute<Schoolclass, Long> id;
    public static volatile SingularAttribute<Schoolclass, Character> identifier;
    public static volatile MapAttribute<Schoolclass, LessonType, Integer> lessonDemands;
    public static volatile SingularAttribute<Schoolclass, Room> room;

}