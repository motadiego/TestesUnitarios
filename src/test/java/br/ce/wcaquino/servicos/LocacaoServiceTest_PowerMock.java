package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTest_PowerMock {
	
	@InjectMocks
	private LocacaoService locacaoService;
	
	@Mock
	private SPCService spcService;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private EmailService emailService;
	
	private static int contador = 0;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		locacaoService = PowerMockito.spy(locacaoService);
		
		contador = contador + 1;
		System.out.println(contador);
	}
	
	@Before
	public void before() {
		System.out.println("Antes de cada teste");
	}
	
	@After
	public void tearDown() {
		System.out.println("Depos de cada teste");
	}
	
	
	@BeforeClass
	public static void setUpClass(){
		System.out.println("Before Class");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println("After Class");
	}
	
	/*
	 * // Usado para gerar uma classe de builder passada como parâmetro (Ex:
	 * LocacaoBuilder) public static void main(String[] args) { new
	 * BuilderMaster().gerarCodigoClasse(Locacao.class); }
	 */
	
	
	
	@Test
	public void deveAlugarFilme() throws Exception{
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5.0).agora());
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(14, 10, 2019)); 
		
		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		error.checkThat(locacao.getValor() , is(5.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(14, 10, 2019)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(15, 10, 2019)), is(true));
	}	

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		// cenario 
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(12, 10, 2019)); 
		
		// acao
		Locacao retorno = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}
	
	@Test
	public void deveAlugarFilme_SemCalcularValor() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		
		PowerMockito.doReturn(1.0).when(locacaoService , "calcularValorLocacao" , filmes);
		
		// acao 
		Locacao locacao =  locacaoService.alugarFilme(usuario, filmes);
		
		// verificacao
		Assert.assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(locacaoService).invoke("calcularValorLocacao" , filmes);
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//acao
		Double valor = Whitebox.invokeMethod(locacaoService, "calcularValorLocacao" , filmes);
		
		// verificacao
		Assert.assertThat(valor, is(4.0));
	}
}
