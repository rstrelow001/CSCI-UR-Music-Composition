package controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.Yaml;

import GUI.CellularAutomataMusic;
import entities.Epoch;
import entities.MeasureDurations;
import entities.MeasureIntervals;

public class InputController {	

    public static HashMap<String, Epoch> readInput()  {
	    
    	HashMap<String, Epoch> epochs = new HashMap<String, Epoch>();
    	
    	JSONParser jsonParser = new JSONParser();
    	 try (FileReader reader = new FileReader("bin/textFiles/jsonConfigFile.json"))
         {
             //Read JSON file
             Object obj = jsonParser.parse(reader);
  
             JSONArray employeeList = (JSONArray) obj;
             System.out.println(employeeList);

             //Iterate over employee array
             for (int i = 0; i < employeeList.size(); i ++) {
            	 JSONObject emp = (JSONObject)employeeList.get(i);
            	 Epoch newEpoch = parseEpochObject(emp);
            	 epochs.put(newEpoch.getEra(), newEpoch);
             }

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (ParseException e) {
             e.printStackTrace();
         }
    	 return epochs;
     }
  
     private static Epoch parseEpochObject(JSONObject epochObject)
     {
    	 ArrayList<MeasureDurations> measureTypes = new ArrayList<MeasureDurations>();
    	 HashMap<Integer, ArrayList<MeasureIntervals>> measureSizes = new HashMap<Integer, ArrayList<MeasureIntervals>>();
	    	
         JSONObject epoch = (JSONObject) epochObject.get("epoch");
         
         //Get the era for this epoch
         String era = (String) epoch.get("era");   
         System.out.println(era);
          
         //Get the path for the measure intervals
         String intervalLocation = (String) epoch.get("measureIntervals"); 
         measureSizes = readIntervalsYAML(intervalLocation);	
         System.out.println(intervalLocation);
          
         //Get the path for the measure durations
         String measureLocation = (String) epoch.get("measureDurations");  
         measureTypes = readDurationsYAML(measureLocation);	
         System.out.println(measureLocation);
     
         //Get the range that intervals follow
         int range = ((Long)epoch.get("range")).intValue();
         System.out.println(range);
         
         //Get the default length for a whole note
         int defaultDuration = ((Long)epoch.get("defaultDuration")).intValue();
         System.out.println(defaultDuration);
         
         Epoch newEpoch = new Epoch(measureTypes, measureSizes, range, defaultDuration, era);
         return newEpoch;
         //epochs.put(era, newEpoch);	
     }
    
    /*
     * Method to initially set up the epochs with their values by reading a YAML file
     */
//    public void setEpochMeasureValues() {
//	    
//	    // varialbes about the epoch
//	    int range = 0, defaultDuration = 2000;
//	    String era = "";
//	      
//    	epochs = new HashMap<String, Epoch>();
//    	
//		Yaml yaml = new Yaml();
//		try (InputStream in = CellularAutomataMusic.class.getResourceAsStream("../textFiles/configFile.yaml")) {
//			Map<String, Map<String, Object>> configs = yaml.load(in);
//			System.out.println(configs);
//
//			Set<String> epochNames = configs.keySet();
//			for (String currentEpochName : epochNames) {
//				System.out.println(currentEpochName);
//				Map<String, Object> epochVariables = configs.get(currentEpochName);
//				Set<String> epochVariableNames = epochVariables.keySet();
//				
//		    	ArrayList<MeasureDurations> measureTypes = new ArrayList<MeasureDurations>();
//		    	HashMap<Integer, ArrayList<MeasureIntervals>> measureSizes = new HashMap<Integer, ArrayList<MeasureIntervals>>();
//				
//				for (String currentEpochVariable : epochVariableNames) {
//					//all values under "measureDurations" in the YAML file will be read in here
//					if (currentEpochVariable.equals("measureDurations")) {
//						Object tempValue = epochVariables.get(currentEpochVariable);
//
//						if (tempValue instanceof String) {
//							String configLocation = epochVariables.get(currentEpochVariable).toString(); 
//							measureTypes = readDurationsYAML(configLocation);								
//						}
//						else {								
//							Map<String, Object> epochMeasureDurationValues = (Map<String, Object>)epochVariables.get(currentEpochVariable);
//							Set<String> durationNames = epochMeasureDurationValues.keySet();
//							for (Object durationType: durationNames) {
//								String temp = durationType.toString();
//								if (durationType instanceof Double)
//									temp = temp.substring(0, temp.length()-1);
//								measureTypes.add(new MeasureDurations(temp, (Double)epochMeasureDurationValues.get(durationType)));								
//							}
//						}
//					}
//					//all values under "measureIntervals" in the YAML file will be read in here
//					else if (currentEpochVariable.equals("measureIntervals")) {
//						Object tempValue = epochVariables.get(currentEpochVariable);
//
//						if (tempValue instanceof String) {
//							String configLocation = epochVariables.get(currentEpochVariable).toString(); 
//							measureSizes = readIntervalsYAML(configLocation);															
//						}
//						else {
//							Map<String, Object> epochMeasureIntervalValues = (Map<String, Object>)epochVariables.get(currentEpochVariable);
//							Set<String> intervalNames = epochMeasureIntervalValues.keySet();
//							for (Object intervalType: intervalNames) {
//								String temp = intervalType.toString();				
//								MeasureIntervals tempInterval = new MeasureIntervals(temp, (Double)epochMeasureIntervalValues.get(intervalType));
//								int intervalSize = tempInterval.getSize();
//								ArrayList<MeasureIntervals> currentAvailableIntervals = measureSizes.get(intervalSize);
//								if (currentAvailableIntervals == null) 
//									currentAvailableIntervals = new ArrayList<MeasureIntervals>();
//								currentAvailableIntervals.add(tempInterval);								
//								measureSizes.put(intervalSize, currentAvailableIntervals);									
//							}
//						}
//					}
//					//all values under "otherValues" in the YAML file will be read in here
//					else if (currentEpochVariable.equals("otherValues")) {
//						Map<String, Object> epochOtherValues = (Map<String, Object>)epochVariables.get(currentEpochVariable);
//						range = (Integer)epochOtherValues.get("range");
//						era = (String)epochOtherValues.get("era");
//						defaultDuration = (Integer)epochOtherValues.get("defaultDuration");
//					}
//				}
//
//				Epoch newEpoch = new Epoch(measureTypes, measureSizes, range, defaultDuration, era);
//				epochs.put(currentEpochName, newEpoch);	
//			}
//			in.close();
//		}
//		catch(IOException ioe) {
//			System.out.println("Sorry!");
//		}
//		running = true;
//    }
//    
    
    /*
     * Helper method to initially set up the epochs with their values by reading YAML Files
     * @param fileLocation  a string of where the yaml file is located
     * @return  the measure durations associated with the epoch
     */
    private static ArrayList<MeasureDurations> readDurationsYAML(String fileLocation) {
    	ArrayList<MeasureDurations> measures = new ArrayList<MeasureDurations>();
    	Yaml yaml = new Yaml();
		try (InputStream in = CellularAutomataMusic.class.getResourceAsStream(fileLocation)) {
			Map<String, Object> durationProbabilities = yaml.load(in);
			System.out.println(durationProbabilities);
			Set<String> durationNames = durationProbabilities.keySet();
			for (Object durationType: durationNames) {
				String temp = durationType.toString();
				if (durationType instanceof Double)
					temp = temp.substring(0, temp.length()-1);
				measures.add(new MeasureDurations(temp, (Double)durationProbabilities.get(durationType)));								
			}
			in.close();									
		}
		catch(IOException ioe) {
			System.out.println("Sorry!");
		}			
		return measures;
    }
    
    	    	    
    
    /*
     * Helper method to initially set up the epochs with their values by reading YAML Files
     * @param fileLocation  a string of where the yaml file is located
     * @return  the measure intervals associated with the epoch
     */
    private static HashMap<Integer, ArrayList<MeasureIntervals>> readIntervalsYAML(String fileLocation) {
    	HashMap<Integer, ArrayList<MeasureIntervals>> measureSizes = new HashMap<Integer, ArrayList<MeasureIntervals>>();
    	Yaml yaml = new Yaml();
		try (InputStream in = CellularAutomataMusic.class.getResourceAsStream(fileLocation)) {
			Map<String, Object> intervalProbabilities = yaml.load(in);
			System.out.println(intervalProbabilities);
			Set<String> intervalNames = intervalProbabilities.keySet();
			for (Object intervalType: intervalNames) {
				String temp = intervalType.toString();				
				MeasureIntervals tempInterval = new MeasureIntervals(temp, (Double)intervalProbabilities.get(intervalType));
				int intervalSize = tempInterval.getSize();
				ArrayList<MeasureIntervals> currentAvailableIntervals = measureSizes.get(intervalSize);
				if (currentAvailableIntervals == null) 
					currentAvailableIntervals = new ArrayList<MeasureIntervals>();
				currentAvailableIntervals.add(tempInterval);								
				measureSizes.put(intervalSize, currentAvailableIntervals);									
			}													
		}
		catch(IOException ioe) {
			System.out.println("Sorry!");
		}
		return measureSizes;
    }
	
}
