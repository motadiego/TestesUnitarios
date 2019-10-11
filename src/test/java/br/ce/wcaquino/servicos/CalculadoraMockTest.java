package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		
	}
	
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
	
	@Test
	public void devoMostraDiferencaEntreMockSpy() {
		Mockito.when(calcMock.somar(1, 2)).thenReturn(5);
		
		// imprime o que esta dento do metodo somar()
		Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
		
		 // nao imprime o que está dentro do metodo somar. Imprime apenas se os argumentos passados 
		 // na linha System.out.println("Spy: " +calcSpy.somar(1, 2)); for diferente dos argumentos passados aqui
		Mockito.doReturn(5).when(calcSpy).somar(1, 2);
		
		// Evitar de chamar o metodo imprime()
		Mockito.doNothing().when(calcSpy).imprime(); 
		
		
		System.out.println("Mock: " + calcMock.somar(1, 2));
		System.out.println("Spy: " +calcSpy.somar(1, 2));
		
		System.out.println("Mock");
		calcMock.imprime();  // por padrao o Mock nao chama o metodo
		
		System.out.println("Spy");
		calcSpy.imprime();	
	}
	
}
