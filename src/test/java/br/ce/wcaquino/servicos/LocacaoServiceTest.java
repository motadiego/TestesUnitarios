package br.ce.wcaquino.servicos;

/****
 * 
 * � uma boa pr�tica a classe de teste 'LocacaoServiceTest' ser criada no 
 * mesmo pacote 'br.ce.wcaquino.servicos'que a classe que ser� testada 'LocacaoService'.
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
		assertTrue(locacao.getValor() == 5.0);
		System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	
}
