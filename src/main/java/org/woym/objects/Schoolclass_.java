package org.woym.objects;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.woym.objects.Room;
import org.woym.objects.Subject;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-29T16:05:39")
@StaticMetamodel(Schoolclass.class)
public class Schoolclass_ { 

    public static volatile SingularAttribute<Schoolclass, Long> id;
    public static volatile MapAttribute<Schoolclass, Subject, Integer> subjectDemands;
    public static volatile SingularAttribute<Schoolclass, Character> identifier;
    public static volatile SingularAttribute<Schoolclass, Room> room;

}