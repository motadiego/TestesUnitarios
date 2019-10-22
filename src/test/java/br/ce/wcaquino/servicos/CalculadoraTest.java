package br.ce.wcaquino.servicos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.ce.wcaquino.exception.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParallelRunner;

@RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setUp(){
		calc = new Calculadora();
		System.out.println("iniciando ...");
	}
	
	@After
	public void tearDown() {
		System.out.println("finalizando ...");
	}
	
	
	@Test
	public void somarDoisValores() {
		// cenario
		int a = 5;
		int b = 3;
		
		// acao
		int resultado = calc.somar(a , b);
		
		//verificacao
		Assert.assertEquals(8, resultado);
	}
	
	

	@Test
	public void subtrairDoisValores() {
		// cenario
		int a = 8;
		int b = 5;
		
		// acao
		int resultado = calc.subtrair(a , b);
		
		//verificacao
		Assert.assertEquals(3, resultado);
	}
	
	
	@Test
	public void dividirDoisValores() throws NaoPodeDividirPorZeroException {
		// cenario
		int a = 6;
		int b = 3;
		
		// acao
		int resultado = calc.dividir(a , b);
		
		//verificacao
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected =  NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// cenario
		int a = 10;
		int b = 0;
		
		// acao
		calc.dividir(a , b);
	}
	
	@Test
	public void deveDividir() {
		String a = "6";
		String b = "3";
		
		int resultado = calc.dividir(a, b);
		
		Assert.assertEquals(2, resultado);
	}
	
}
