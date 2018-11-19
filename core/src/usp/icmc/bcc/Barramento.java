package usp.icmc.bcc;

public class Barramento implements Runnable{
	private volatile Boolean livre = true;

	@Override
	public void run() {
		while(true) {
			if(!livre) {
				
				try {
					Thread.sleep(1000);
					livre = !livre;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public boolean ocupado() {
		return !livre;
	}
	
	public Boolean colocaNoBarramento() {
//		System.out.println("PEDIU PRA ENTRAR");
		if(livre) {
			livre = !livre;
//			System.out.println("Permitido");
			return true;
		}
//		System.out.println("NEGADO");
		return false;
		
	}

}
