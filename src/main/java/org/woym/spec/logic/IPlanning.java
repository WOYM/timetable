package org.woym.spec.logic;

import java.util.List;

import org.woym.objects.Activity;
import org.woym.objects.TimePeriod;

/**
 * Modeliert die Funktionalität der Logikebene in der Plannungs-Seite
 * 
 * @author JurSch
 *
 */
public interface IPlanning<E> {

/**
 * Liefert alle Activitäten in den das object enthalten ist.
 * Sollten keine Activitäten zu diesem Object gefunden werden wird eine
 * leere Liste zurück gegeben.
 * 
 * @param object
 * 		Das Object, nachdem die Activitäten durgesucht werden
 * 
 * @return
 * 		Liste aller gefunden Activitäten, eine leere Liste ist ein
 * 		gültiger Wert
 */		
public List<Activity> getActivities(E object);

/**
 * Versucht eine Actiity hinzuzufügen. 
 * Teilt mittels {@link ISatus} den erfolgt oder misserfolg mit.
 * 
 * @param activity
 * 		Die zu hinzufügende Activity.
 * 
 * @return
 * 		{@link IStatus} mit Information über den Ablauf der Methode
 */
public IStatus addActivity(Activity activity);

/**
 * Überprüft ob das obejct zu dem gegebenen Zeitraumverfügbar ist.
 * Gibt ein {@link IStatus}, mit den Information, zurück.
 * 
 * @param object
 * 		Zu überprüfende Object
 * 
 * @param time
 * 		Zu überprüfender Zeitraum
 * 
 * @return
 * 		{@link IStatus} mit Informationen über den Vorgang.
 */
public IStatus isAvailable(E object, TimePeriod time);

/**
 * Versucht ein update der gegebenen Activity auszufühen.
 * Gibt {@link IStatus} mit Informationen zurück.
 * 
 * @param activity
 * 		Die zu updatende Activity
 * 		
 * @return
 * 		{@link IStatus} mit Informationen zumVorgang
 */
public IStatus updateActivity(Activity activity);

/**
 * Versucht die gegebenen Activity zu löschen.
 * Gibt {@link IStatus} mit Informationen zurück.
 * 
 * @param activity
 * 		Die zu löschende Activity
 * 		
 * @return
 * 		{@link IStatus} mit Informationen zumVorgang
 */
public IStatus deleteActivity(Activity activity);


}