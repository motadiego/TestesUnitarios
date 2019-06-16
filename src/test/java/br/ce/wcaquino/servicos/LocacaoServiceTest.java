package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	private LocacaoService locacaoService = new LocacaoService();
	
	private static int contador = 0;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp(){
		System.out.println("Before");
		// cenario
		locacaoService = new LocacaoService();
		contador = contador + 1;
		System.out.println(contador);
	
	}
	
	@After
	public void tearDown() {
		System.out.println("After");
	}
	
	
	@BeforeClass
	public static void setUpClass(){
		System.out.println("Before Class");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println("After Class");
	}
	
	
	
	
	@Test
	public void teste() throws Exception{
		Usuario usuario = new Usuario("Pedro junior");
		Filme filme = new Filme("Filme 1",2 , 5.0);
		
		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);
		
			//verificacao
		error.checkThat(locacao.getValor() , is(5.0));
		error.checkThat(locacao.getValor() , not(6.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is((true)));
	}
	
	/***
	 * Forma elegante
	 * @throws Exception
	 */
	@Test(expected=FilmeSemEstoqueException.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = new Usuario("Pedro junior");
		Filme filme = new Filme("Filme 1",0 , 5.0);
				
		// acao
		locacaoService.alugarFilme(usuario, filme);
	}
	
	
	@Test
	public void testeLocacao_UsuarioVazio() throws FilmeSemEstoqueException{
		// cenario
		Usuario usuario =null;//new Usuario("Pedro junior");
		Filme filme = new Filme("Filme 1",2 , 5.0);
	
		// acao
		try {
			locacaoService.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lancado uma exceção");
		}catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
		
		System.out.println("Forma Robusta");
	}
	
	
	
	
	@Test
	public void testeLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException{
		// cenario
		Usuario usuario = new Usuario("Pedro junior");
		Filme filme = null;//new Filme("Filme 1",2 , 5.0);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		locacaoService.alugarFilme(usuario, filme);
		
		System.out.println("Forma nova");
	}
	
	
	
}
