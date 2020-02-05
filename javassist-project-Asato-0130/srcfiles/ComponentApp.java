public class ComponentApp {
	public static void main(String[] args) throws Exception {
		System.out.println("Run...");
		ComponentApp localMyApp = new ComponentApp();
		localMyApp.runComponent();

		System.out.println(Class.forName(args[0]).getField(args[1]).getName());
		Object obj = Class.forName(args[0]).getField(args[1]).get(Class.forName(args[0]).newInstance());
		System.out.println("[DBG] value: " + obj);
		System.out.println("Done.");
	}

	public void runComponent() {
		System.out.println("Called runComponent.");
	}
}