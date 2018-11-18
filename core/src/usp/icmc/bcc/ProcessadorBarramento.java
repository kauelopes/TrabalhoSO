package usp.icmc.bcc;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ProcessadorBarramento implements Runnable{

	private Barramento barramento;
	private float chanceDeAcessoAMemória;
	volatile int estado;
	private boolean ok = true;
	
	
	public ProcessadorBarramento(Barramento barramento, float chanceDeAcessoAMemória) {
		this.barramento = barramento;
		this.chanceDeAcessoAMemória = chanceDeAcessoAMemória;		
		this.estado = 0;
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unused")
	public void cancel() {
		ok = false;

	}
	//estado = 0 - Calculando
	//estado = 1 - Espera
	//estado = 2 - lendo Barramento
	
	@Override
	public void run() {
		while(ok) {
			try {
				Thread.sleep(500);
				if(Math.random()<chanceDeAcessoAMemória) {					
					while(!barramento.colocaNoBarramento()) {
						estado = 1;					
					}
					estado = 2;
					Thread.sleep(500);
				}else {
					estado = 0;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
