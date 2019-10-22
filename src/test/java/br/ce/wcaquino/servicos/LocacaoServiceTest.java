package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.runners.ParallelRunner;
import br.ce.wcaquino.utils.DataUtils;


@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {
	
	@InjectMocks @Spy
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
		System.out.println("iniciando 2 ...");
	}
	
	/*
	 * @Before public void before() { System.out.println("Antes de cada teste"); }
	 */
	
	@After
	public void tearDown() {
		System.out.println("finalizando 2 ...");
	}
	
	
	/*
	 * @BeforeClass public static void setUpClass(){
	 * System.out.println("Before Class"); }
	 * 
	 * @AfterClass public static void tearDownClass() {
	 * System.out.println("After Class"); }
	 */
	
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
		
		Mockito.doReturn(DataUtils.obterData(14, 10, 2019)).when(locacaoService).obterData();
		
		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		error.checkThat(locacao.getValor() , is(5.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(14, 10, 2019)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(15, 10, 2019)), is(true));
	}
	
	/***
	 * Forma elegante
	 * @throws Exception
	 */
	@Test(expected=FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora());
				
		// acao
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
		
		// cenario
		Usuario usuario =null;//new Usuario("Pedro junior");
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// acao
		try {
			locacaoService.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lancado uma exceção");
		}catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
		
		//System.out.println("Forma Robusta");
	}
	
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException{
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		locacaoService.alugarFilme(usuario, null);
		
		System.out.println("Forma nova");
	}
	
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		// cenario 
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		
		Mockito.doReturn(DataUtils.obterData(12, 10, 2019)).when(locacaoService).obterData();
		
		
		// acao
		Locacao retorno = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}
	
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception{
		
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// quando o metodo do service for chamado , retornar TRUE
		Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		//acao
		try {
			locacaoService.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
		}
		
		//verificacao
		Mockito.verify(spcService).possuiNegativacao(usuario);
	}
	
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();
		
		List<Locacao> locacoes = Arrays.asList(
			LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario).agora(),
			LocacaoBuilder.umLocacao().comUsuario(usuario2).agora(),
			LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora(),
			LocacaoBuilder.umLocacao().atrasada().comUsuario(usuario3).agora()
		);
		
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		// acao
		locacaoService.notificacaoAtrasos();
	
		// vericicacao
		
		// verifica se sera executado 3x (locacao atrasada) para qualquer usuario
		Mockito.verify(emailService , Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		
		Mockito.verify(emailService).notificarAtraso(usuario);
		// verifica se enviou pelo menos 1 vez o email
		Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		// verifica se a noficicação nao ocorreu
		Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(emailService);
	}
	
	@Test
	public void deveTratarErroNoSPC() throws Exception {
		// cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//espectativa
		Mockito.when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrofica !!!"));
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente !!!");
		
		//acao
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	
	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Locacao locacao = LocacaoBuilder.umLocacao().agora();
		
		//acao
		locacaoService.prorrogarLocacao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao> argCaptor = ArgumentCaptor.forClass(Locacao.class); 
		Mockito.verify(dao).salvar(argCaptor.capture()); // captura o mesmo objeto (locacao) criado dentro do metodo locacaoService.prorrogarLocacao()
		Locacao locacaoRetornada = argCaptor.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(3));
	}
	
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double) metodo.invoke(locacaoService, filmes);
		
		// verificacao
		Assert.assertThat(valor, is(4.0));
	}
}
