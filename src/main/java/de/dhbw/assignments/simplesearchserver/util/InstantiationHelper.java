package de.dhbw.assignments.simplesearchserver.util;

/**
 * Hilfsklasse zum dynamischen Laden und Instanziieren von Klassen.
 * <p>
 * <b>Anmerkung:</b> Dieses File darf nicht editiert werden!
 *
 * @author Michael Schlegel
 *
 */
public class InstantiationHelper
{

	/**
	 * Privater Konstruktor. Verhindert ein Instaziieren dieser Klasse.
	 */
	private InstantiationHelper()
	{
	}

	/**
	 * Hilfsmethode zum dynamischen Laden und Instanziieren einer Klasse.
	 *
	 * @param <T>                         Typ der Klasse (siehe p_classToCast)
	 * @param p_className                 Vollst&auml;ndiger Name der Klasse
	 * @param p_constructorParameterTypes Klassentypen der Konstruktor-Parameter
	 * @param p_p_constructorParameters   Werte der Konstruktor-Parameter
	 * @param p_classToCast               CastType der instanziierten Klasse
	 *
	 * @return Instanz vom Typ p_classToCast
	 */
	public static <T> T loadImplementationClass(final String p_className, final Class<?>[] p_constructorParameterTypes,
	        final Object[] p_p_constructorParameters, final Class<T> p_classToCast)
	{
		try
		{
			Class<?> l_class = Thread.currentThread().getContextClassLoader().loadClass(p_className);

			Object l_instance = l_class.getDeclaredConstructor(p_constructorParameterTypes)
			        .newInstance(p_p_constructorParameters);

			return p_classToCast.cast(l_instance);
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Could not load Class " + p_className, ex);
		}
	}
}
