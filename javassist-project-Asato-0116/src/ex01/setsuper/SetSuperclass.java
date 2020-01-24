package ex01.setsuper;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import target.Rectangle;


public class SetSuperclass
{
   static final String SEP = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + SEP + "output";

   public static void main(String[] args)
   {
      if (args.length != 3)
      {
    	System.out.print("Not enought arguments, input 3 arguments.\n"
    			+ "Closing program !\n");
      }
      else
      {
    	  int commonNum = 0;
    	  String superClassName = "";
    	  String subClassArray[] = new String[2];
    	  String commonClassArray[] = new String[3];
    	  if (args[0].startsWith("Common"))
    	  {
    		  commonClassArray[commonNum++] = args[0];
    	  }
    	  if (args[1].startsWith("Common"))
    	  {
    		  commonClassArray[commonNum++] = args[1];
    	  }
    	  if (args[2].startsWith("Common"))
    	  {
    		  commonClassArray[commonNum++] = args[2];
    	  }
    	  switch (commonNum)
    	  {
    	  	case 0:
    	  		superClassName = args[0];
    	  		subClassArray[0] = args[1];
    	  		subClassArray[1] = args[2];
    	  		break;
    	  	case 1:
    	  		int temp = 0;
    	  		superClassName = commonClassArray[0];
    	  		for (int i = 0; i <= 2; i++)
    	  		{
    	  			if (!args[i].contentEquals(superClassName))
    	  			{
    	  				subClassArray[temp++] = args[i];
    	  			}
    	  		}
    	  		break;
    	  	case 2:
    	  		if (commonClassArray[0].length() >= commonClassArray[1].length())
    	  		{
    	  			superClassName = commonClassArray[1];
    	  		}
    	  		else 
    	  		{
    	  			superClassName = commonClassArray[0];
    	  		}
    	  		temp = 0;
    	  		for (int i = 0; i <= 2; i++)
    	  		{
    	  			if (!args[i].contentEquals(superClassName))
    	  			{
    	  				subClassArray[temp++] = args[i];
    	  			}
    	  		}
    	  		break;
    	  	case 3: 
    	  		if (commonClassArray[0].length() >= commonClassArray[1].length()
    	  				&& commonClassArray[0].length() >= commonClassArray[2].length())
    	  		{
    	  			superClassName = commonClassArray[0];
    	  		}
    	  		else if (commonClassArray[1].length() >= commonClassArray[0].length()
    	  				&& commonClassArray[1].length() >= commonClassArray[2].length())
    	  		{
    	  			superClassName = commonClassArray[1];
    	  		}
    	  		else
    	  		{
    	  			superClassName = commonClassArray[2];
    	  		}
    	  		temp = 0;
    	  		for (int i = 0; i <= 2; i++)
    	  		{
    	  			if (!args[i].equals(superClassName))
    	  			{
    	  				subClassArray[temp++] = args[i];
    	  			}
    	  		}
    	  		break;
    	  }
	   try
	   {
         ClassPool pool = ClassPool.getDefault();

         boolean useRuntimeClass = false;
         if (useRuntimeClass) 
         {
            insertClassPathRunTimeClass(pool);
         } else 
         {
            insertClassPath(pool);
         }

         CtClass cc1 = pool.get("target." + subClassArray[0]);
         setSuperclass(cc1, "target." + superClassName, pool);

         cc1.writeFile(outputDir);
         System.out.println("[DBG] write output to: " + outputDir);
         
         CtClass cc2 = pool.get("target." + subClassArray[1]);
		 setSuperclass(cc2, "target." + superClassName, pool);

		 cc2.writeFile(outputDir);
		 System.out.println("[DBG] write output to: " + outputDir);
	  } 
	  catch (NotFoundException | CannotCompileException | IOException e) 
	  {
        e.printStackTrace();
      }
     }
   }
   static void insertClassPathRunTimeClass(ClassPool pool) throws NotFoundException 
   {
      ClassClassPath classPath = new ClassClassPath(new Rectangle().getClass());
      pool.insertClassPath(classPath);
      System.out.println("[DBG] insert classpath: " + classPath.toString());
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException 
   {
      String strClassPath = workDir + SEP + "bin"; // eclipse compile dir
      // String strClassPath = workDir + SEP + "classfiles"; // separate dir
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
