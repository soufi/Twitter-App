package partie1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Permet de detecter les capitaliste sociaux à partir d'un fichier contenant toutes les relations des individus que nous devons traité
 * le forme du fichier qu'on peut traité avec cette class est : source -> destination
 * c'est-à-dire que source suit destination
 * @author Souf
 *
 */
public class AnalyseData {
	
	/**
	 * permet de detecter les capitalistes sociaux contenus dans le fichie file passé en parametre 
	 * @param file_in : fichier des individus et leurs relation sous la forme source -> destination
	 */
	public static void detect (String file_in){
				
		Date d = new Date (System.currentTimeMillis());
		System.out.println("Début ...  \t"+d.toString());
		
		//les collections de stockage 
		HashSet <String> followers = new HashSet <String>();
		HashSet <String> folloween = new HashSet <String> ();
		//liste des capitaliste trié par catégorie
		HashSet <Integer> fmify = new HashSet <Integer>();
		HashSet <Integer> ifyfm = new HashSet <Integer>();
		HashSet <Integer> both = new HashSet <Integer>();
		
		try {
			execShellScript("sortNcopy.sh", file_in); //preparation du fichier
			//fichier de tous les Folloween
			BufferedReader flwn_file = new BufferedReader (new FileReader(file_in));
			//fichier de tous les followers
			BufferedReader flwrs_file = new BufferedReader (new FileReader("inverse.txt"));
			//fichier de sortie de résultats
			String file_out = "CapitalistesSociaux14.txt";
			BufferedWriter product = new BufferedWriter (new FileWriter(file_out));
			String tmp_flwn = ""; //ligne du fichier de followeens temporaire
			Integer id_user; //l'id de l'individu traité pour chaque itération.
			/* pour pourvoir extraire le contenu d'un utilisateur on est obliger de regarder une ligne en avance
			 * puisqu'on lit une ligne en avance, vaut mieux pas la lire deux fois.. donc on va la stocker dans rez_tmp_flwn pour les folloween
			 * et rez_tmp_flwrs
			 */
			String[] rez_tmp_flwn = null; 
			String[] rez_tmp_flwrs = null;
			//tableau de resultat de la fonction split de String
			String [] spl = null;
			//tant qu'on a pas fini de lire le fichier en entier
			while((tmp_flwn = flwn_file.readLine()) != null){	
				spl = tmp_flwn.trim().split("\\s+"); // extraire les deux entier
				id_user = Integer.parseInt(spl[0]);
				folloween.add(spl[1]);
				//extraction du fichier de folloween
				rez_tmp_flwn = extract(flwn_file, folloween, id_user);
				//extraction du fichier de followers
				rez_tmp_flwrs = extract(flwrs_file, followers, id_user);
				//traitement des données.
				category(both, ifyfm, fmify, followers, folloween, id_user);
				if(rez_tmp_flwn != null){
					//dans le cas ou le cursor est déréglé
					if(! rez_tmp_flwn[0].equals(rez_tmp_flwrs[0])){
						//appel de la fonction qui permet de donner les coordonnées du point ou on peut commencer la detection
						String [] ret_correction = correctCursor(flwn_file, flwrs_file, rez_tmp_flwn, rez_tmp_flwrs);
						id_user = Integer.parseInt(ret_correction[0]);
						folloween.add(ret_correction[1]);
						followers.add(ret_correction[2]);
					//si tous va bien
					}else{
						id_user = Integer.parseInt(rez_tmp_flwn[0]);
						folloween.add(rez_tmp_flwn[1]);
						followers.add(rez_tmp_flwrs[1]);
					}
				}
			//on continue tant qu'on a pas fini de lire les deux fichiers
			}
			//Ecriture sur le fichier de sortie 
			write(product, fmify, "FMIFY");
			write(product, ifyfm, "IFYFM");
			write(product, both, "BOTH");
			//fermeture des flux d'entrés sortie
			product.close();
			flwrs_file.close();
			flwn_file.close();	
			//on trie le fichier de sortie
			execShellScript("sort.sh", file_out);	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e1) {
			e1.printStackTrace();
		}catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Date f = new Date (System.currentTimeMillis());
		System.out.println("Fin ...  \t"+f.toString());
		//affichage d'informations
		System.out.println("FMIFY : "+ fmify.size());
		System.out.println("IFYFM : "+ ifyfm.size());
		System.out.println("Both : "+ both.size());
		fmify.clear();
		ifyfm.clear();
		both.clear();
	}
	
	/**
	 * lit l'intersection, verifie s'il y a au moins 1000 utilisateurs, et ajoute l'utilisateur dans la collection result
	 * a noter que cette fonction supprime les information contenu dans les deux collection Set, après avoir effectué le traitement.
	 * @param both : HashSet <Integer> contenant que les individus capitaliste des deux types fmify et ifyfm
	 * @param ifyfm : HashSet <Integer> contenant que les individus de type "I Follow You Follow Me"
	 * @param fmify : HashSet <Integer> contenant que les individus de type "Follow Me I Follow You"
	 * @param followers : la liste des followers de l'individu traité
	 * @param folloween : la liste des folloween de l'individu traité
	 * @param user : l'individu traite
	 */
	private static void category (HashSet <Integer> both, HashSet <Integer> ifyfm, HashSet <Integer> fmify, HashSet <String> followers, HashSet <String> folloween, Integer user){
		int nbr_flwrs = followers.size();
		int nbr_flwn = folloween.size();
		followers.retainAll(folloween);	
		int inter = followers.size(); // intersection
		//si l'intersection est inferieur à 1000 on ne traite pas
		if(inter >= 1000){
			float ratio_flwn = (float)inter/(float)nbr_flwn;
			float ratio_flwrs = (float)inter/(float)nbr_flwrs;
			//si l'un des deux ration est supérieur à 0.8
			if((ratio_flwn >= 0.8) && (ratio_flwrs >= 0.8))
				both.add(user);
			else if(ratio_flwrs >=0.8)
				fmify.add(user);
			else if(ratio_flwn >= 0.8)
				ifyfm.add(user);
		}
		followers.clear();
		folloween.clear();
	}
	
	/**
	 * extrait du buffer toutes les lignes qui correspondent a l'individu element
	 * il faut noter que si le programme tombe sur des lignes qui correspondent à un individu avec un id inférieur à ce lui passé en paramètre
	 * le programme saute l'individu en esperant tomber sur le prochain qui correspondera à element. retourne la premiere ligne de l'individu suivant, ou supérieur 
	 * a l'element
	 * @param buf
	 * @param result
	 * @param element
	 * @return Integer [] : tableau avec deux cases contenant l'id et le follower de l'id
	 * @throws IOException
	 */
	public static String[] extract (BufferedReader buf, HashSet <String> result, Integer element) throws IOException{
		String line = "";
		//contient le resultat de l'extraction avec la regexp
		String[] spl = null;
		while ((line = buf.readLine()) != null){
			//extraction des deux entier de la ligne avec regexp
			spl = line.trim().split("\\s+");
			Integer id = Integer.parseInt(spl[0]);
			//si l'id qu'on est entrain de lire est inferieur à l'element qu'on cherche on zappe
			if(id < element){
				spl = nextID(buf, element);
				if(spl != null)
					id = Integer.parseInt(spl[0]);
				else //si on a plus d'elements on termine le traitement
					return null;
			}
			//si on est toujours entrain de lire les informations de l'utilisateur
			if(id.equals(element))
				result.add(spl[1]); //on ajoute
			else if(id > element) //si c'est la ligne est grande on zappe
				return spl;
		}
		return null;
	}
	
	/**
	 * parcour le fichier et envoi la premiere ligne qui arrive après les informations de l'individu element
	 * les fichiers utilisé sont sensé etre trié
	 * @param buf
	 * @param element
	 * @return String [2] : case 1 contient l'id et la case 2 le follower ou folloween de l'id celon le buffer traité
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static String [] nextID (BufferedReader buf, Integer element) throws NumberFormatException, IOException{
		String line = "";
		String [] spl = null;
		while ((line = buf.readLine()) != null){
			spl = line.trim().split("\\s+");
			Integer id = Integer.parseInt(spl[0]);
			//si l'id est différent de element on renvoi la ligne
			if(!id.equals(element))
				return spl;
		}
		return null;
	}
	
	
	/**
	 * alligne les cursor des deux buffer sur la meme valeur de la ligne pour correspondre au même individu
	 * @param buf1 : buffer correspondant au spl1
	 * @param buf2 : buffer correspondant au spl2
	 * @param line_fich1 : tableau a deux case 
	 * case 0 : contient le numéro de l'individu
	 * case 1 : contient la destination pour le fichier de folloween
	 * @param line_fich2
	 * case 0 : contient le numéro de l'individu
	 * case 1 : contient le numéro de la destination pour le fichier de followers
	 * @return un tableau a 4 case qui est la somme de deux tableaux de deux cases de la meme forme que la fonction Extract.
	 * en d'autre mots, les deux premiere case sont les information srce -> dest de la permiere ligne d'un individu du fichier folloween, et
	 * les deux dernière cases de la même forme pour le fichier followers.
	 * @throws IOException 
	 */
	public static String[] correctCursor(BufferedReader buf1, BufferedReader buf2, String[] line_fich1, String[] line_fich2) throws IOException {
		boolean not_found; //permet d'arreter la boucle a l'endroit souhaité
		//on recupere les id differents des utilisateurs passé en parametre
		Integer id1 = Integer.parseInt(line_fich1[0]);
		Integer id2 = Integer.parseInt(line_fich2[0]);
		String[] spl = null;
		//on retourne les informations contenu dans la fourchette ou il faut commencer pour eviter de lire la ligne deux fois.
		String [] resultat = new String[3];
		resultat[1] = line_fich1[1]; //nous gardons les information contenu dans les lignes 
		resultat[2] = line_fich2[1]; // nous gardons les informations contenu dans les lignes recupéré
		while (! id1.equals(id2)){
			//rappellons que id1 et id2 correspondent à deux fichiers differents
			if(id1 < id2){
				not_found = true; //on début nous ne sommes pas au meme niveau 
				while(not_found){
					//on passe au prochain user si id1 < id2
					spl = nextID(buf1, id1);
					id1 = Integer.parseInt(spl[0]);
					if(id1 >= id2){
						resultat[0] = spl[0];
						resultat[1] = spl[1]; //on stock le resultat tamporaire au cas ou c'est la ligne qu'on cherche
						not_found = false;
					}
				}
			}
			//dans le cas ou c'est 
			if(id1 > id2){
				not_found = true; //on début nous ne sommes pas au meme niveau 
				while(not_found){
					spl = nextID(buf2, id2);
					id2 = Integer.parseInt(spl[0]);
					if(id2 >= id1){
						resultat[0] = spl[0];
						resultat[2] = spl[1];
						not_found = false;
					}
				}
			}
		}
		return resultat;
	}
	
	
	/**
	 * Ecrit sur le buffer de sortie avec la forme suivante :
	 * 		idCapitaliste1       typeCapitaliste1 
	 * 		idCapitaliste2		 typeCapitaliste2 
	 * 		...					 ...
	 * @param buf
	 * @param individus
	 * @param type
	 * @throws IOException
	 */
	private static void write (BufferedWriter buf, HashSet <Integer> individus, String type) throws IOException{
		Iterator <Integer> iter = individus.iterator();
		while(iter.hasNext()){
			buf.write(iter.next()+"\t"+type+"\n");
		}
	}

	/**
	 * Permet d'executer un script shell à partir de son chemin
	 * @param file : le script a executer 
	 * @param param : le parametre du script
	 * @return String : contenant le retour de l'execution du script
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static String execShellScript (String file , String param) throws IOException, InterruptedException{
		File script = new File (file);
		String response = "";
		ProcessBuilder pb;
		if(param == null || param.isEmpty())
			return "Pas de Paramètres, impossible de lancer processus";
		else{
			File file1 = new File(param);
			pb = new ProcessBuilder("bash", "-c", "./"+script.getName()+" "+file1.getAbsolutePath());
		}	
		pb.redirectErrorStream(true);
		Process shell = pb.start();
		InputStream shell_in = shell.getInputStream();
		int shellExitStatus = shell.waitFor();
		if(shellExitStatus != 0){
			System.out.println("Exit status" + shellExitStatus);
			return null;
		}
		response = convertStreamToString(shell_in);
		shell_in.close();
		return response;
	}
	
	
	/**
	 * Permet de convertir InputStream en String
	 * @param is
	 * @return String 
	 * @throws IOException
	 * @see http://www.dzone.com/snippets/convert-stream-string
	 */
	private static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {		
				is.close();
			}
			return writer.toString();
		}else {
			return "";
		}
	}
	
}
