package ex06;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

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
	static String INPUT_DIR = WORK_DIR + File.separator + "bin";
	private ClassPool pool;
	
	public static void main(String[] args) throws NotFoundException
	{
		Scanner sc = new Scanner(System.in);
		ExerClassLoader loader = new ExerClassLoader();
		System.out.println("Enter two inputs: (1) an application class name and (2) a field name\n"
				+ "E.g., ComponentApp,f1 or ServiceApp,f2");
		String[] input = UtilMenu.getInput();
		try
		{
		    Class<?> c = loader.findClass(input[0],input[1]);
		    c.getDeclaredMethod("main", new Class[] { String[].class }). //
		            invoke(null, new Object[] { args });
		}
		catch (IllegalAccessException e) 
		{
	         e.printStackTrace();
	    }
		catch (NotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/* 
	 * Find a specified class, and modify the bytecode.
	 */
	protected Class<?> findClass(String name, String newField) throws NotFoundException, ClassNotFoundException 
	{
		try 
		{
			CtClass cc = pool.get(name);
	        CtField f = new CtField(CtClass.doubleType, newField, cc);
	        f.setModifiers(Modifier.PUBLIC);
	        cc.addField(f);
	        
	         byte[] b = cc.toBytecode();
	         return defineClass(name, b, 0, b.length);
	      } catch (NotFoundException e) {
	         throw new ClassNotFoundException();
	      } catch (IOException e) {
	         throw new ClassNotFoundException();
	      } catch (CannotCompileException e) {
	         throw new ClassNotFoundException();
	      }
	   }
	public ExerClassLoader() throws NotFoundException {
	      pool = new ClassPool();
	      pool.insertClassPath(INPUT_DIR);
	   }

}
