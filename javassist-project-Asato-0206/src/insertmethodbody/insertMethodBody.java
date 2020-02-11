package insertmethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilFile;
import util.UtilMenu;

public class insertMethodBody 
{
	static String WORK_DIR = System.getProperty("user.dir");
	static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
	static String Target = "target.";
	public static void main(String[] args)
	{
		while(true)
		{
			UtilMenu.showMenuOptions();
			UtilMenu.getOption();
			System.out.print("Enter three inputs: (1) an application class name, "
					+ "(2) a method name, and (3) a method parameter index.\n"
					+ "(e.g. "
					+ "ComponentApp,foo,1 or ServiceApp,bar,2)");
			String[] inputs = UtilMenu.getArguments();
			if (inputs.length == 3)
			{
				process(inputs);
			}
			else
			{
				System.out.println("[WRN] Invalid number of inputs.");
			}
		}
	}
	public static void process(String[] inputs)
	{
		try
		{
			ClassPool pool = ClassPool.getDefault();
	        pool.insertClassPath(INPUT_DIR);
	        CtClass cc = pool.get(Target + inputs[0]);
	        CtMethod m = cc.getDeclaredMethod(inputs[1]);
	        String block1 = "{ " //
		               + "System.out.println(\"[Inserted] "+ Target + inputs[0] +"."+ inputs[1]
		               + "'s param " + inputs[2] + " : \" + $" + inputs[2] + "); }";
	         System.out.println(block1);
	         m.insertBefore(block1);
	         cc.writeFile(OUTPUT_DIR);
	         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	         System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
	         System.out.println("-------------------------------------------------------");
	         Loader cl = new Loader(pool);
	 	     Class<?> c = cl.loadClass(Target + inputs[0]);
	         c.newInstance();
	         c.getDeclaredMethod("main", new Class[] { String[].class }). //
				invoke(null, new Object[] {null});
		}
		catch(ClassNotFoundException | NotFoundException | CannotCompileException |
				IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
	         e.printStackTrace();
	    }
	}

}
