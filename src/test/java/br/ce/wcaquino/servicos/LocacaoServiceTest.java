package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

/****
 * 
 * É uma boa prática a classe de teste 'LocacaoServiceTest' ser criada no 
 * mesmo pacote 'br.ce.wcaquino.servicos'que a classe que será testada 'LocacaoService'.
 * 
 * 
 * 
 */

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Test
	public void teste(){
		// cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario usuario = new Usuario("Pedro junior");
		Filme filme = new Filme("Filme 1",2 , 5.0);
		
		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);
		
		//verificacao
		assertEquals(5.0 ,locacao.getValor() , 0.1);
		assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	
}
