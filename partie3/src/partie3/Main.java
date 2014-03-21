package partie3;

import java.util.Date;

import partie1.AnalyseData;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date d = new Date (System.currentTimeMillis());
		System.out.println("DŽbut ...  \t"+d.toString());
		
		/*BufferedWriter bufOut;
		try {
			FileWriter fw = new FileWriter("Comparaison14.txt", true);
			bufOut = new BufferedWriter (fw);
			bufOut.write("ID \t NEWFOLLOWEES \t OLDFOLLOWEES \t NEWFOLLOWERS \t OLDFOLLOWERS");
			bufOut.newLine();
			bufOut.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		DataCompare.compare("grapheProjet.txt", "inverse.txt", "Followees14.txt", "Followers14.txt");
		
		//AnalyseData.detect("Followees14.txt");
		
		Date f = new Date (System.currentTimeMillis());
		System.out.println("Fin ...  \t"+f.toString());

	}

}
