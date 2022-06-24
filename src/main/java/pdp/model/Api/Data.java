package pdp.model.Api;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Data{
	private Translations translations;

	public Translations getTranslations(){
		return translations;
	}
}
