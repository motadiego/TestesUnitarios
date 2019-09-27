package br.ce.wcaquino.matchers;

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
		/*
		 * Calendar data = Calendar.getInstance(); data.set(Calendar.DAY_OF_WEEK,
		 * diaSemana); String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK,
		 * Calendar.LONG, new Locale("pt", "BR")); desc.appendText(dataExtenso);
		 */
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data,  DataUtils.obterDataComDiferencaDias(diaSemana));
	}

}
