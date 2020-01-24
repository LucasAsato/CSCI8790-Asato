package question0121;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File; 
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

public class DefineNewClass 
{
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + File.separator + "output";
   static Scanner sc = new Scanner(System.in);
   
   public static void main(String[] args)
   {
	   int count = 0;
	   String superClassName = "";
 	   String subClassArray[] = new String[2];
 	   String commonClassArray[] = new String[3];
 	   while (true) 
 	   {
           System.out.println("=============================================");
           System.out.print("Enter three classes (\"q\" to terminate)\n");
           String[] inputs = getInputs();
           for (int i = 0; i < inputs.length; i++) 
           {
              System.out.println("[DBG] " + i + ": " + inputs[i]);
           }
           try 
           {
        	   ClassPool pool = ClassPool.getDefault();

               CtClass cc = makeClass(pool, subClassArray[0]);
               cc.writeFile(outputDir);
               System.out.println("[DBG] write output to: " + outputDir);

               CtClass ccInterface = makeInterface(pool, subClassArray[1]);
               ccInterface.writeFile(outputDir);
               System.out.println("[DBG] write output to: " + outputDir);
           } 
           catch (CannotCompileException | IOException e) 
           {
        	   e.printStackTrace();
           }
       }
       
   }
   public static String[] getInputs() 
   {
	   String input = sc.nextLine();
	   if (input.trim().equalsIgnoreCase("q")) 
	   {
		   System.err.println("Terminated.");
	       System.exit(0);
	   }
	   List<String> list = new ArrayList<String>();
	   String[] inputArr = input.split(",");
	   for (String iElem : inputArr) 
	   {
		   list.add(iElem.trim());
	   }
	   return list.toArray(new String[0]);
	}
   
   static CtClass makeClass(ClassPool pool, String newClassName) 
   {
	   CtClass cc = pool.makeClass(newClassName);
       System.out.println("[DBG] make class: " + cc.getName());
       return cc;
   }

   private static CtClass makeInterface(ClassPool pool, String newInterfaceName) 
   {
       CtClass cc = pool.makeInterface(newInterfaceName);
       System.out.println("[DBG] make interface: " + cc.getName());
       return cc;
   }
}
