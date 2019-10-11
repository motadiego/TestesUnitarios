package br.ce.wcaquino.matchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private Integer diaSemana;
	
	public DataDiferencaDiasMatcher(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}
	
	public void describeTo(Description desc) {
		Date dataEsperada = DataUtils.obterDataComDiferencaDias(diaSemana);
		DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		desc.appendText(formatador.format(dataEsperada));
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data,  DataUtils.obterDataComDiferencaDias(diaSemana));
	}

}
