package pdp.model.Api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Translations{
	private String translatedText;

	public String getTranslatedText(){
		return translatedText;
	}
}
