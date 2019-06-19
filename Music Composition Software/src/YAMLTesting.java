import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import GUI.CellularAutomataMusic;
import entities.MeasureDurations;

public class YAMLTesting {


	public static void main(String args[]) {
		Yaml yaml = new Yaml();
		try (InputStream in = CellularAutomataMusic.class.getResourceAsStream("/textFiles/medieval/durations/3_2_time.yaml")) {
			Map<String, Object> durationProbabilities = yaml.load(in);
			System.out.println(durationProbabilities);
			Set<String> durationNames = durationProbabilities.keySet();
			for (Object durationType: durationNames) {
				String temp = durationType.toString();
				System.out.println(durationType + " = " + durationProbabilities.get(durationType));
				//if (durationType instanceof Double)
					//temp = temp.substring(0, temp.length()-1);
				//measures.add(new MeasureDurations(temp, (Double)durationProbabilities.get(durationType)));								
			}
			in.close();									
		}
		catch(IOException ioe) {
			System.out.println("Sorry!");
		}
	}
}
