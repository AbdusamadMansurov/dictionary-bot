package pdp.model.Api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class Translate{
	private Data data;

	public Data getData(){
		return data;
	}
}
