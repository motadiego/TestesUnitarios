package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


public class CalculadoraMockTest {
	
	@Test  
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
		
		Assert.assertEquals(5,calc.somar(1, 8));
	}
	
	@Test  
	public void teste2() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class); 
		Mockito.when(calc.somar(argCaptor.capture(), argCaptor.capture())).thenReturn(5);
		
		Assert.assertEquals(5,calc.somar(1, 10000000));
		System.out.println(argCaptor.getAllValues());
	}
	
	
	
}
