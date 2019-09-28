package br.ce.wcaquino.daos;

import java.util.List;

import br.ce.wcaquino.entidades.Locacao;

public class LocacaoDAOFake implements LocacaoDAO {

	public void salvar(Locacao locacao) {
		System.out.println("Salvou o obj ... ");
	}

	public List<Locacao> obterLocacoesPendentes() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
