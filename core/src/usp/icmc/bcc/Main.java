package usp.icmc.bcc;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Main extends ApplicationAdapter {

	
	ShapeRenderer render;
	Barramento barramento;
	
	SpriteBatch batch;
	BitmapFont font;
	float taxaErro = 1.4f;
	int contador = 0;
	static int POS = 300;
	static int MUL = 98;
	
	int[] contadores = new int[7];
	int medicoes = 0;
	
	
	
	ArrayList<ProcessadorBarramento> p;
	ArrayList<Thread> t;
	
	@Override
	public void create () {
		render = new ShapeRenderer();
		barramento = new Barramento();
		new Thread(barramento).start();
		
		p = new ArrayList<ProcessadorBarramento>();
		t = new ArrayList<Thread>();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
	}
	
	private void criaProcessador() {
		p.add(new ProcessadorBarramento(barramento, taxaErro*0.1f));
		t.add(new Thread(p.get(p.size()-1)));
		t.get(t.size()-1).start();
	}
	
	private void retiraProcessador() {
		t.remove(t.size()-1);
		p.get(p.size()-1).cancel();
		p.remove(p.size()-1);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		render.begin(ShapeType.Filled);
		
		batch.begin();
		font.draw(batch, "taxa De Erro em Cache:" + (taxaErro*0.1f) , 410, 30);
		batch.end();
		
		medicoes++;
		
		for(int i =0;i<p.size();i++) {
			if(p.get(i).estado == 0) {
				render.setColor(Color.BLUE);
				batch.begin();
//				font.draw(batch, "running", MUL*i,POS+80);
				batch.end();
				render.rect(MUL*i,POS,50,50);
				render.setColor(Color.GRAY);
				render.rectLine(MUL*i + 25, POS, MUL*i + 25, POS - 60-5,10); // parte individual
				render.rectLine(MUL*i + 25-5, POS-60, 300+5, POS-60,10); // parte compartilhadan250,POS-200
				render.rectLine(300, POS-60, 300, POS-200,10); // parte compartilhadan250,POS-200



			}
		}
		
		for(int i =0;i<p.size();i++) {
			if(p.get(i).estado == 1) {
				contadores[i]++;
				batch.begin();
				font.draw(batch, "Waiting", MUL*i,POS+80);
				batch.end();
				render.setColor(Color.RED);
				render.rect(MUL*i,POS,50,50);
				render.rectLine(MUL*i + 25, POS, MUL*i + 25, POS - 60+5,10); // parte individual
				render.setColor(Color.GRAY);
				render.rectLine(MUL*i + 25, POS-60, 250, POS-60,10); // parte compartilhadan250,POS-200
				
			}
		}
		
		
		render.setColor(Color.GREEN);
		boolean usoumemoria = false;
		for(int i =0;i<p.size();i++) {
			if(p.get(i).estado == 2) {
				usoumemoria = true;
				batch.begin();
				font.draw(batch, "Reading", MUL*i,POS+80);
				batch.end();
				render.rect(MUL*i,POS,50,50);
				render.rectLine(MUL*i + 25, POS, MUL*i + 25, POS - 60-5,10); // parte individual
				render.rectLine(MUL*i + 25, POS-60, 300+5, POS-60,10); // parte compartilhadan250,POS-200
				render.rectLine(300, POS-60+5, 300, POS-200,10); // parte compartilhadan250,POS-200
			}
		}
		
		if(usoumemoria) {
			render.setColor(Color.GREEN);
			render.rect(250,POS-200,100,50);
		}else {
			render.setColor(Color.GRAY);
			render.rect(250,POS - 200,100,50);
		}
		batch.begin();
		font.draw(batch, "RAM", 250 + 35,POS-210);
		font.draw(batch, "a - Adiciona Processador ao BUS",10,35);
		font.draw(batch, "d - deleta Processador ao BUS",10,20);
		
		
		batch.end();
		
		render.end();
		
		
		
		if(Gdx.input.isKeyJustPressed(Keys.A)) {
			if(contador < 7) {
				contador++;
				criaProcessador();
			}
			for(int i=0;i<7;i++) {
				contadores[i] = 0;
			}
			medicoes = 0;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.T)) {
			float total = 0;
			for(int i=0;i<p.size();i++) {
				total += contadores[i]*1f/medicoes;
				System.out.println("proc " + i + ": " + contadores[i]*1f/medicoes);
			}
			total = total/p.size();
			System.out.println("media eh : " + total);
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.D)) {
			if(contador>0)
				contador--;
			retiraProcessador();
			for(int i=0;i<7;i++) {
				contadores[i] = 0;
			}
			medicoes = 0;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.UP)) {
			if(taxaErro<10) {
				taxaErro+=1;
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			if(taxaErro>0) {
				taxaErro-=1;
			}
		}
		
		
	}
	
	@Override
	public void dispose () {

	}
}
