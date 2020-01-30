package ex05.javassistloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class Ex05
{
	private static final String SEP = File.separator;
	private static final String WORK_DIR = System.getProperty("user.dir");
	private static final String OUT_DIR = WORK_DIR + SEP + "output";
	private static final String PKG_NAME = "target.";

	public static void main(String[] args) 
	{
		while (true) 
		{
			String[] classArr = UtilMenu.getInputsClass();
			setInheritage(classInheritance(classArr));
			
			UtilMenu.showMenuOptionsAddRemove();
	        int option = UtilMenu.getOption();
	        switch (option) {
	        case 1:
	        	System.out.println("[DBG] Enter (1) the method to be modified and\n" //
	                  + "(2) a method call to be inserted (e.g., getVal: move(10, 20);):");
	            String[] arguments = UtilMenu.getArguments();
	            break;
	        case 2: 
	        	System.out.println("[DBG] Enter (1) the method to be modified and\n" //
		                  + "(2) a method call to be removed (e.g., getVal: move(10, 20);):");
		        arguments = UtilMenu.getArguments();
	         default:
	            break;
	         }
	      }
	   }
	/**
	 * Class to identify class inheritance base on ex 0116
	 * @param input classes set up their inheritance
	 * @return a list of the input classes where the first one is the superclass
	 */
	private static List<String> classInheritance(String[] input)
	{
		List<String> result = new ArrayList<String>();
		final String prefix = "Common";
		final int INPUT_NUMBER = 3;
		
		if (input.length != INPUT_NUMBER)
	    {
			System.out.printf("Not enought arguments, input %d arguments.\n"
	    			+ "Closing program !\n", INPUT_NUMBER);
			System.exit(0);
	    }
	    else
	    {
    	  int commonNum = 0;
    	  String superClassName = "";
    	  String commonClassArray[] = new String[INPUT_NUMBER];
    	  for (int i = 0; i < INPUT_NUMBER; i++)
    	  {
    		  if (input[i].startsWith(prefix))
    		  {
    			  commonClassArray[commonNum++] = input[i];
    		  }
    	  }
    	  switch (commonNum)
    	  {
    	  	case 0:
    	  		for (int i = 0; i < INPUT_NUMBER; i++)
    	  		{
    	  			result.add(input[i]);
    	  		}
    	  		break;
    	  	case 1:
    	  		int temp = 0;
    	  		result.add(commonClassArray[0]);
    	  		for (int i = 0; i < INPUT_NUMBER; i++)
    	  		{
    	  			if (!input[i].contentEquals(superClassName))
    	  			{
    	  				result.add(input[i]);
    	  			}
    	  		}
    	  		break;
    	  	case 2:
    	  		if (commonClassArray[0].length() >= commonClassArray[1].length())
    	  		{
    	  			result.add(commonClassArray[0]);
    	  		}
    	  		else 
    	  		{
    	  			result.add(commonClassArray[1]);
    	  		}
    	  		temp = 0;
    	  		for (int i = 0; i < INPUT_NUMBER; i++)
    	  		{
    	  			if (!input[i].contentEquals(superClassName))
    	  			{
    	  				result.add(input[i]);
    	  			}
    	  		}
    	  		break;
    	  	case 3: 
    	  		if (commonClassArray[0].length() >= commonClassArray[1].length()
    	  				&& commonClassArray[0].length() >= commonClassArray[2].length())
    	  		{
    	  			result.add(commonClassArray[0]);
    	  		}
    	  		else if (commonClassArray[1].length() >= commonClassArray[0].length()
    	  				&& commonClassArray[1].length() >= commonClassArray[2].length())
    	  		{
    	  			result.add(commonClassArray[1]);
    	  		}
    	  		else
    	  		{
    	  			result.add(commonClassArray[2]);
    	  		}
    	  		temp = 0;
    	  		for (int i = 0; i <INPUT_NUMBER; i++)
    	  		{
    	  			if (!input[i].equals(superClassName))
    	  			{
    	  				result.add(input[i]);
    	  			}
    	  		}
    	  		break;
    	  }
	    }
		return result;
	}
	private static void setInheritage(List<String> list)
	{
		try
		{
			ClassPool pool = ClassPool.getDefault();
			insertClassPath(pool);
			
			for (int i = 1; i < list.size(); i++)
			{
				CtClass cc = pool.get(PKG_NAME + list.get(i));
		        setSuperclass(cc, PKG_NAME + list.get(0), pool);

		        cc.writeFile(OUT_DIR);
			}
		}
		catch (NotFoundException | CannotCompileException | IOException e) 
		{
			e.printStackTrace();
	    }
	}
	/**
	 * Adds a ClassClassPath to the default ClassPool permanently.
	 */
	/*static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException 
	   {
	      ClassClassPath classPath = new ClassClassPath(new Rectangle().getClass());
	      pool.insertClassPath(classPath);
	      System.out.println("[DBG] insert classpath: " + classPath.toString());
	   }*/ //not use
	 /**
	  * Register the class path for the object.
	  */
	   static void insertClassPath(ClassPool pool) throws NotFoundException 
	   {
	      //String strClassPath = WORK_DIR + SEP + "bin"; // eclipse compile dir
	      String strClassPath = WORK_DIR + SEP + "classfiles"; // separate dir
	      pool.insertClassPath(strClassPath);
	      System.out.println("[DBG] insert classpath: " + strClassPath);
	   }

	   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException 
	   {
	      curClass.setSuperclass(pool.get(superClass));
	      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
	            ", subclass: " + curClass.getName());
	   }
}
