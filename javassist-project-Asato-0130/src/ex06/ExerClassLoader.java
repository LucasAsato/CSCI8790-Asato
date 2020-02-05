package ex06;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import util.UtilMenu;

public class ExerClassLoader extends ClassLoader
{
	static String WORK_DIR = System.getProperty("user.dir");
	static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	private static ClassPool pool;
	private String[] input = null;
	
	public static void main(String[] args) throws Throwable 
	{
		ExerClassLoader loader = new ExerClassLoader();
		System.out.println("Enter two inputs: (1) an application class name and (2) a field name\n"
				+ "E.g., ComponentApp,f1 or ServiceApp,f2");
		loader.input = UtilMenu.getInput();
		
		Class<?> c = loader.loadClass(loader.input[0]);
		c.getDeclaredMethod("main", new Class[] { String[].class }). //
				invoke(null, new Object[] { loader.input });
		
	}
	/* 
	 * Find a specified class, and modify the bytecode.
	 */
	protected Class<?> findClass(String name) throws ClassNotFoundException 
	{
		try 
		{
			CtClass cc = pool.get(name);
	        if (name.equals(input[0])) {
	            CtField f = new CtField(CtClass.doubleType, input[1], cc);
	            f.setModifiers(Modifier.PUBLIC);
	            cc.addField(f, CtField.Initializer.constant(0.0));
	         }	        
	         byte[] b = cc.toBytecode();
	         return defineClass( name, b, 0, b.length);
	      } catch (NotFoundException e) {
	         throw new ClassNotFoundException();
	      } catch (IOException e) {
	         throw new ClassNotFoundException();
	      } catch (CannotCompileException e) {
	         throw new ClassNotFoundException();
	      }
	   }
	public ExerClassLoader() throws NotFoundException 
	{
		pool = ClassPool.getDefault();
	    pool.insertClassPath(INPUT_DIR);
	}
}
