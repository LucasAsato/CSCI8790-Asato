public class ServiceApp {
	public static void main(String[] args) throws Exception {
		System.out.println("Run...");
		ServiceApp localMyApp = new ServiceApp();
		localMyApp.runService();

		System.out.println(Class.forName(args[0]).getField(args[1]).getName());
		Object obj = Class.forName(args[0]).getField(args[1]).get(Class.forName(args[0]).newInstance());
		System.out.println("[DBG] value: " + obj);
		System.out.println("Done.");
	}

	public void runService() {
		System.out.println("Called runComponent.");
	}
}