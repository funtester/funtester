package org.funtester.common.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.EventListener;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Announcer is a utility class to ease the use of the Publish/Subscribe design
 * pattern (as known as Observer).
 * 
 * @author Nat Pryce
 * @author Steve Freeman
 * @author Thiago Delgado Pinto
 * 		(make it thread-safe, fix "add", make "addAll", "getAll", "clear" and "size" methods)
 * 
 * 
 * This file is under the Apache Licence.
 * @see http://www.growing-object-oriented-software.com/code.html
 *
 * @param <T> Listener class.
 */

public class Announcer<T extends EventListener> {
	
//	private Logger logger = LoggerFactory.getLogger( Announcer.class );

	private final T proxy;
	private final ConcurrentLinkedDeque< T > listeners = new ConcurrentLinkedDeque< T >();
		
	public Announcer(Class<? extends T> listenerType) {
		
		proxy = listenerType.cast( Proxy.newProxyInstance(
			listenerType.getClassLoader(), 
			new Class<?>[]{listenerType}, 
			new InvocationHandler() {
				public Object invoke(Object aProxy, Method method, Object[] args) throws Throwable {
					announce(method, args);	
					return null;
				}
			}));
	}
	
	public int size() {
		return listeners.size();
	}
	
	public void clear() {
		listeners.clear();		
	}
	
	public void addAll(Collection< T > listeners) {
		for ( T listener : listeners ) {
			addListener( listener );
		}
	}	
	
	public ConcurrentLinkedDeque< T > getAll() {
		return listeners;
	}
	
	public boolean addListener(T listener) {
		/*
		log( "addListener called..." );
		String className = listener.getClass().getName();
		// if ( listeners.contains( listener ) ) return false;
		T listenerToBeRemoved = null;
		int i = 0;
		for ( T l : listeners ) {
			String currentClassName = l.getClass().getName();
			log( ++i + ") Comparing to existing class: " + currentClassName );
			if ( currentClassName.equalsIgnoreCase( className ) ) {
				log( "Same class!" );
				listenerToBeRemoved = l;
				break;
			}
		}
		if ( listenerToBeRemoved != null ) {
			removeListener( listenerToBeRemoved );
		}
		log( "Adding listener from class " + className );
		*/
		return listeners.add(listener);
	}
	
	public boolean removeListener(T listener) {
		return listeners.remove(listener);
	}
	
	public T announce() {
		return proxy;
	}
	
	private void announce(Method m, Object[] args) {
		try {
			synchronized ( listeners ) {
				for (T listener : listeners) {
					m.invoke( listener, args );
				}
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("could not invoke listener", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException)cause;
			} else if (cause instanceof Error) {
				throw (Error)cause;
			} else {
				throw new UnsupportedOperationException("listener threw exception", cause);
			}
		}
	}
	
	public static <T extends EventListener> Announcer<T> to(Class<? extends T> listenerType) {
		return new Announcer<T>(listenerType);
	}

	/*
	protected synchronized void log(String message) {
		logger.debug( message );
	}*/
}

