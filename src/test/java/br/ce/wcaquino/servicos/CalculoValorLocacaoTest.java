package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;

/***
 * 
 * @author Diego Mota
 * 
 * Classe que implementa testes genericos
 *
 */


@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spcService;
	
	@Mock
	private LocacaoDAO dao;
	
	
	// primeiro parametro
	@Parameter(value=0)
	public List<Filme> filmes;
	
	// segundo parametro
	@Parameter(value=1)
	public Double valorLocacao;
	
	// terceiro parametro
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	private static Filme filme1 = FilmeBuilder.umFilme().agora();
	private static Filme filme2 = FilmeBuilder.umFilme().agora();
	private static Filme filme3 = FilmeBuilder.umFilme().agora();
	private static Filme filme4 = FilmeBuilder.umFilme().agora();
	private static Filme filme5 = FilmeBuilder.umFilme().agora();
	private static Filme filme6 = FilmeBuilder.umFilme().agora();
	private static Filme filme7 = FilmeBuilder.umFilme().agora();

	
	@Parameters(name="{2}") // passando apenas o terceiro parâmetro
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem desconto"},
			{Arrays.asList(filme1, filme2 , filme3), 11.0, "3 Filmes: 25%"},
			{Arrays.asList(filme1, filme2 , filme3,filme4), 13.0, "4 Filmes: 50%"},
			{Arrays.asList(filme1, filme2 , filme3,filme4, filme5), 14.0, "5 Filmes: 75%"},
			{Arrays.asList(filme1, filme2 , filme3,filme4, filme5,filme6), 14.0, "6 Filmes: 100%"},
			{Arrays.asList(filme1, filme2 , filme3,filme4, filme5,filme6, filme7), 18.0, "7 Filmes: Sem desconto"}	
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		// cenario 
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// verificacao
		assertThat(locacao.getValor(),is(valorLocacao));
	}
}
