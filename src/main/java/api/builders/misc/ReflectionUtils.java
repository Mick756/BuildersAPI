package api.builders.misc;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionUtils {
	
	public static final String BUKKIT_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	
	public static Class<?> getClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	}
	
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) throws NoSuchMethodException {
		return clazz.getConstructor(params);
	}
	
	public static Method getMethod(Class<?> clazz, String method, Class<?>... args) throws NoSuchMethodException {
		return clazz.getMethod(method, args);
	}
	
}
