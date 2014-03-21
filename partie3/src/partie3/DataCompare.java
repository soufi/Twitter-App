package partie3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import partie1.AnalyseData;

/**
 * Cette classe fournit les outils necessaire a la comparaison de quatre fichier  : 
 * 	- deux fichiers de followees d'annes differentes
 * 	- deux fichiers de followers d'années differentes 
 *
 */
public class DataCompare {
	
	/**
	 * Renvoi une nouvelle HashSet qui represente les elements de la set1 exclusivement (dans set1 et pas dans set2)
	 * @param set1 
	 * @param set2
	 * @return HashSet <String> : contenant les elements dans set1 et pas dans set2
	 */
	protected static HashSet <String> sub (HashSet <String> set1, HashSet <String> set2){
		String n;
		Iterator <String> iter = set1.iterator();
		//le resultat 
		HashSet <String> result = new HashSet <String> ();
		//parcour de tous les elements de la premiere HashSet
		while(iter.hasNext()){
			n = iter.next();
			if(!set2.contains(n))
				result.add(n);
		}
		return result;
	}
	
	/**
	 * retourne une nouvelle list qui contient l'intersection entre set1 et set2
	 * @param set1
	 * @param set2
	 * @return HashSet <String> : contenant l'intersection entre les deux Set en param
	 */
	protected static HashSet <String> intersection (HashSet <String> set1, HashSet <String> set2){
		HashSet <String> clone = new HashSet <String> (set1);
		clone.retainAll(set2);
		return clone;
	}
	
	/**
	 * compare les fichiers de 2010 avec les fichiers de 2013 (partie 3 projet) en lisant les 4 fichiers en même temps
	 * @param file_flwn10
	 * @param file_flwrs10
	 * @param file_flwn13
	 * @param file_flwrs13
	 */
	protected static void compare (String file_flwn10, String file_flwrs10, String file_flwn13, String file_flwrs13){
		//les contenus de stockage 2010
		HashSet <String> followers10 = new HashSet <String>();
		HashSet <String> folloween10 = new HashSet <String> ();
		//les contenus de stockage 2013
		HashSet <String> followers13 = new HashSet <String>();
		HashSet <String> folloween13 = new HashSet <String> ();
		//des variable utilitaire
		String[] spl = null;
		String line = "";
		String type = "";
		/*on suit le meme raisonnement que la class AnalyseData, on lit une ligne en avance
		*donc pour chaque fichier il faut garder les information de la ligne lu en avance*/
		String[] nextFlwn10 = null; 
		String[] nextFlwrs10 = null;
		String[] nextFlwn13 = null; 
		String[] nextFlwrs13 = null;
		try {
			//le buffer de sortie 
			FileWriter fw = new FileWriter("Comparaison14.txt", false);
			BufferedWriter bufOut = new BufferedWriter (fw);
			System.out.println("Trie des fichiers ...");
			//preparation des fichiers, avec le sort
			AnalyseData.execShellScript("sort.sh", file_flwn10);
			AnalyseData.execShellScript("sort.sh", file_flwrs10);
			AnalyseData.execShellScript("sort.sh", file_flwn13);
			AnalyseData.execShellScript("sort.sh", file_flwrs13);
			System.out.println("Fin de trie ... Début de traitements ...");
			//lecture des fichiers
			FileReader frFlwn10 =  new FileReader(file_flwn10);
			BufferedReader bufFlwn10 = new BufferedReader(frFlwn10);
			FileReader frFlwrs10 = new FileReader(file_flwrs10);
			BufferedReader bufFlwrs10 = new BufferedReader(frFlwrs10);
			FileReader frFlwn13 = new FileReader(file_flwn13);
			BufferedReader bufFlwn13 = new BufferedReader(frFlwn13);
			FileReader frFlwrs13 = new FileReader(file_flwrs13);
			BufferedReader bufFlwrs13 = new BufferedReader(frFlwrs13);
			while((line = bufFlwn13.readLine()) != null){
				spl = line.trim().split("\\s+"); // extraire les deux entier
				Integer id_user = Integer.parseInt(spl[0]);
				folloween13.add(spl[1]);
				/*afin de detourner les problemes de memeoire nous traitons les followees d'abord ensuite en supprime
				 * le fichier de folloween 2010 et on refait la meme chose avec les followers*/
				
				int [] newElem = new int[2];  //tableau contenant le nombre des nouveau element, case 1 pour les followees, et case 2 pour les followers
				int [] oldElem = new int[2]; //tableau contenant le nombre des anciens elments, case1 pour les followees, et case 2 pour les followers
				//extraction des information relative au folloween de l'individu dans les 2 fichiers de folloween
				nextFlwn13 = AnalyseData.extract(bufFlwn13, folloween13, id_user);
				nextFlwn10 = AnalyseData.extract(bufFlwn10, folloween10, id_user);
				 //calcule des nouveau elements followees
				newElem[0] = giveNew(folloween10, folloween13);
				// calcule des anciens elements followees
				oldElem[0]= giveOld(folloween10, folloween13);
				folloween10.clear(); //on a plus besoin des anciens elements
				
				//extraction des informations relative au followers de l'individu dans les 2 fichiers de followers
				nextFlwrs13 = AnalyseData.extract(bufFlwrs13, followers13, id_user);
				nextFlwrs10 = AnalyseData.extract(bufFlwrs10, followers10, id_user);
				//calcule des nouveau elements de followers
				newElem[1] = giveNew(folloween10, folloween13);
				// calcule des anciens elements de followers
				oldElem[1] = giveOld(folloween10, folloween13);
				followers10.clear(); //on a plus besoin des anciens elements
				//recuperation de type
				type = category(folloween13, followers13);
				//ecriture des resultats dans le fichier de sortie
				write (bufOut, type, newElem, oldElem, id_user);
				//traitement des prochaines lignes
				if(nextFlwn13 != null){
					folloween13.add(nextFlwn13[1]);
					if(nextFlwrs13 != null && nextFlwn13[0].equals(nextFlwrs13[0]))
						followers13.add(nextFlwrs13[1]);
					if(nextFlwn10 != null && nextFlwn13[0].equals(nextFlwn10[0]))
						folloween10.add(nextFlwn10[1]);
					if(nextFlwrs10 != null && nextFlwn13[0].equals(nextFlwrs10[0]))
						followers10.add(nextFlwrs10[1]);
				}
			}
			//fermeture des flux
			bufFlwn10.close();
			bufFlwn13.close();
			bufFlwrs10.close();
			bufFlwrs13.close();
			frFlwn10.close();
			frFlwn13.close();
			frFlwrs10.close();
			frFlwrs13.close();
			bufOut.close();
			fw.close();
		}catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	/**
	 * renvoi le nombre de nouveau éléments dans 2013 pour les followers et pour les folloween
	 * le resultat est renvoyer sous la forme d'un tableau à deux cases :
	 * case 1 : nombre de nouveau folloween
	 * case 2 : nombre d'ancien followers
	 * @param set2010 : HashSet <String> contenant les elements (followers ou followees) de 2010
	 * @param set2013 : HashSet <String> contenant les elements (followers ou followees) de 2013
	 * @return int : les elements de la set 2010 qui ne sont pas dans 2013
	 */
	protected static int giveNew (HashSet <String> set2010, HashSet <String> set2013){
		int result = 0;
		//les collections qui contiendrons les nouveau elements qui sont dans 2013 et pas dans 2010
		HashSet <String> nvFlwn = sub (set2013, set2010); 
		result = nvFlwn.size();
		nvFlwn.clear();
		return result;
	}
	
	
	/**
	 * renvoi le nombre d'ancien éléments qui appartenait au graphe de 2010 et pas au graphe de 2013
	 * le resultat renvoyer sous la forme d'un tableau à deux cases :
	 * case 1 : nombre d'ancien folloween
	 * case 2 : nombre d'ancien followers
	 * @param set2010 : HashSet <String> contenant les elements (followers ou followees) de 2010
	 * @param set2013 : HashSet <String> contenant les elements (followers ou followees) de 2013
	 * @return int : les elements de la set 2010 qui ne sont pas dans 2013
	 */
	protected static int giveOld (HashSet <String> set2010, HashSet <String> set2013){
		int result = 0;
		//les collections qui contiendrons les ancien elemets qui sont dans 2010 et pas dans 2010
		HashSet <String> oldFlwn = sub (set2010, set2013); 
		result= oldFlwn.size();
		oldFlwn.clear();
		return result;
	}
	
	/**
	 * retourne le type de capitaliste à partir des informations sur les folloween et followers
	 * supprime les informatios contenu dans les collections en parametre
	 * @param folloween
	 * @param followers
	 * @return String : le type de capitaliste + NONE
	 */
	protected static String category (HashSet <String> folloween, HashSet <String> followers){
		int nbr_flwrs = followers.size();
		int nbr_flwn = folloween.size();
		//calcule de l'intersection
		followers.retainAll(folloween);
		int inter = followers.size();
		followers.clear();
		folloween.clear();
		//si l'intersection est inferieur à 1000 on ne traite pas
		if(inter >= 1000){
			float ratio_flwn = (float)inter/(float)nbr_flwn;
			float ratio_flwrs = (float)inter/(float)nbr_flwrs;
			//si l'un des deux ration est supérieur à 0.8
			if((ratio_flwn >= 0.8) && (ratio_flwrs >= 0.8))
				return "BOTH";
			else if(ratio_flwrs >=0.8)
				return "FMIFY";
			else if(ratio_flwn >= 0.8)
				return "IFYFM";
		}
		
		return "NONE";
	}
	
	
	/**
	 * Ecrit dans le buffer de sortie les informations fourni en parametre, correspondant à la comparaison entre les fichier de 2010 et 2013
	 * @param bufWrite : buffer de sortie
	 * @param type : le type de capitaliste 
	 * @param newElm : tableau à deux cases avec nbr de nouveau followees a la case 1 et de followers a la case 2
	 * @param oldElm : tableau à deux cases avec nbr d'ancien followees à la case 1 et de followers à la case 2
	 * @param id : l'id de l'individu 
	 * @throws IOException
	 */
	protected static void write (BufferedWriter bufWrite, String type , int[] newElm, int[] oldElm, Integer id) throws IOException{
		//concaténation des information.
		StringBuffer str = new StringBuffer(id.toString()+"\t"+type.toString()+"\t"+newElm[0]+"\t"+oldElm[0]+"\t"+newElm[1]+"\t"+oldElm[1]);
		bufWrite.write(str.toString()); //ecriture des information
		bufWrite.newLine(); //saut de ligne pour le prochain user

	}
	
	
}
