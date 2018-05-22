import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Set;

public class YAMLTesting {
	public static void main(String[] args) throws IOException {

		int start;
		double uni;
		Yaml yaml = new Yaml();
		try (InputStream in = YAMLTesting.class.getResourceAsStream("/configFile.yaml")) {
			LinkedHashMap<String, LinkedHashMap<String, Object>> configs = yaml.load(in);
			System.out.println(configs);

			LinkedHashMap<String, Object> innerMap = configs.get("medieval");
			Set<String> keySet = innerMap.keySet();
		
			start = (Integer)innerMap.get("start");
			System.out.println(start);
			
			uni = (Double)innerMap.get("uni");
			System.out.println(uni);
		}
	}
}
