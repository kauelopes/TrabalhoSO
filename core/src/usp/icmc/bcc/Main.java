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
	int taxaErro = 1;
	
	
	
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
		
		for(int i =0;i<p.size();i++) {
			if(p.get(i).estado == 0) {
				render.setColor(Color.BLUE);
				render.rect(100*i,200,50,50);
				render.setColor(Color.GRAY);
				render.line(100*i + 25, 225, 300, 30);


			}
		}
		
		render.setColor(Color.RED);
		for(int i =0;i<p.size();i++) {
			if(p.get(i).estado == 1) {
				render.rect(100*i,200,50,50);
				render.line(100*i + 25, 225, 300, 30);
				
			}
		}
		
		
		render.setColor(Color.GREEN);
		boolean usoumemoria = false;
		for(int i =0;i<p.size();i++) {
			if(p.get(i).estado == 2) {
				usoumemoria = true;
				render.rect(100*i,200,50,50);
				render.line(100*i + 25, 225, 300, 30);
			}
		}
		
		if(usoumemoria) {
			render.setColor(Color.GREEN);
			render.rect(250,10,100,50);
		}else {
			render.setColor(Color.GRAY);
			render.rect(250,10,100,50);
		}
		
		render.end();
		
		
		
		if(Gdx.input.isKeyJustPressed(Keys.A)) {
			criaProcessador();
		}
		if(Gdx.input.isKeyJustPressed(Keys.D)) {
			retiraProcessador();
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
