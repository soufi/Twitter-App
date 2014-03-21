package Partie2;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Permet de charger les Followers et les Followees des Capitalistes contenu dans un interval d'utilisateur donné via l'API Twitter.
 * Ces données sont écrites dans deux fichiers distinct : Followers14.txt et Followees14.txt
 * @author simon_hubert
 * 
 */

public class Url {
	
	/**
	 * Permet de stocker dans une HashSet tous les utilisateurs Capitalistes de notre interval.
	 * Nous allons chercher ces Capitalistes dans le fichier source idsCapitalistesSociaux.txt
	 * @return la HashSet contenant les Capitalistes de notre interval.
	 */
	
	public static HashSet <Integer> recuperer_Capitaliste () {
		HashSet <Integer> nos_Capitalistes = new HashSet <Integer> () ;
		File fichier = new File("idsCapitalistesSociaux.txt");
		System.out.println(fichier.getAbsolutePath());
		Scanner scanLigne;
		try {
			scanLigne = new Scanner (fichier);
			while (scanLigne.hasNextInt()){
				int cap = scanLigne.nextInt();
				if ((cap>42601948) && (cap<70000000)) {
					nos_Capitalistes.add(cap);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return nos_Capitalistes ;
	}
	
	/**
	 * Permet d'ouvrir une Url et de charger son contenu.
	 * C'est donc ici que nous allons executer nos requetes sur l'API de Twitter et ainsi récupérer nos données.
	 * @param urlBis : l'adresse Url à charger
	 * @return Un String contenant le contenu de la page urlBis
	 * @see http://www.fobec.com/CMS/java/sources/ouvrir-une-url-charger-son-contenu-format-texte_908.html
	 */
	
	 public String getTextFile (String urlBis) {
		 BufferedReader reader = null ;
		 try {
			 URL url = new URL(urlBis);
	    	 URLConnection urlConnection = url.openConnection();
	    	 reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	    	 StringBuilder sb = new StringBuilder();
	    	 String line = null;
	    	 while ((line = reader.readLine()) != null) {
	    		 sb.append(line);
	    		 sb.append("\n");
	    	 }
	         return sb.toString();
		 } catch (IOException ex) {
			 // suppression de texte du contenu de l'URL pour pouvoir récupérer le code d'erreur
			 String supp = "java.io.IOException: Server returned HTTP response code: " ;
			 String code = ex.toString() ;
			 code = code.replace(supp,"");
			 Scanner scCode = new Scanner (code) ;
			 int num ;
			 if (scCode.hasNextBigInteger()) {
				 // recuperation du code d'erreur de la page
				 num = scCode.nextInt();
			 } else {
				 return "Effacer" ;
			 }
			 System.out.println(" Erreur : "+num);
			 if (num==401)
				 return "NoAutor" ;
			 else {
				 System.out.println(" Vous avez dépasser 150 requetes ");
				 return "Attente";
			 }
	     } finally {
	    	 try {
	    		 if (reader != null) {
	        		 reader.close();
	       		 }
	       	 } catch (IOException ex) {
	       	 }
	    }
	 }
	 
	 /**
	  * Permet d'ecrire dans les fichier Followers14.txt ou Followees14.txt les données d'une requete
	  * @param contenu : Contenu d'une requete executer via l'API Twitter
	  * @param id : Id de l'utilisateur en question
	  * @param type : Type de la requete pour l'utilisateur : Followers ou Followees
	  */
	 
	 public static void remplir (String contenu, BigInteger id, String type) {
		 String supp ; 
		 // suppression du texte inutile dans le contenu 
		 if (type.equals("followers")) {
			 supp = "{\"ids\":[" ;
		 } else {
			 supp = "{\"next_cursor_str\":\"0\",\"ids\":[" ;
		 }
		 contenu = contenu.replace(supp, "");
		 contenu = contenu.replaceAll("^(.*?]).*", "$1");
		 contenu = contenu.replace("]", ",");
		 Scanner scLigne = new Scanner (contenu);
		 scLigne.useDelimiter(",");
		 BufferedWriter bw;
		 FileWriter fw ;
		 try {
			 if (type.equals("followers")) {
				 fw = new FileWriter("Followers14.txt",true);
			 } else {
				 fw = new FileWriter("Followees14.txt",true);
			 }
			 bw = new BufferedWriter(fw);
			 PrintWriter fichSortie = new PrintWriter(bw);
			 while (scLigne.hasNextBigInteger()) {
				 // lecture de chaque followers (ou followees) de l'utilisateur en question
				 BigInteger followers = scLigne.nextBigInteger() ;
				 // ecriture dans le fichier de sortie 
				 fichSortie.println(id+" \t"+followers);
			 }
			 bw.close();
			 fichSortie.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	 /**
	  * Permet d'executer des requetes via l'API Twitter pour un Utilisateur.
	  * Le nombre de requetes est limité à 5 par utilisateur pour éviter d'avoir plus de 25 000 Followers ou Followees par utilisateur.
	  * @param tweetos : Id de l'utilisateur en question
	  * @param type : Le type des requetes que l'on veut executer : Followers ou Following
	  */
	 
	 public static void charger (BigInteger tweetos, String type) {
		 Url httpLoader = new Url();
		 String contenu, contenu_temp ;
		 BigInteger next_cursor = BigInteger.ZERO ;
		 // chargement de la requete dans contenu
		 contenu = httpLoader.getTextFile("https://api.twitter.com/1/"+type+"/ids.json?user_id="+tweetos);
		 contenu_temp = contenu ;
		 int counter = 0 ;
		 do {
			 // gestion de l'Attente car nous sommes restreint à 150 requetes par heure
			 while (contenu.equals("Attente")) {
				 try {
					 System.out.println(" Please wait 1 hour ...");
					 Thread.sleep(300000);
					 System.out.println(" Reprise du Chargement ");
					 if (counter == 0) {
						 contenu = httpLoader.getTextFile("https://api.twitter.com/1/"+type+"/ids.json?user_id="+tweetos);
						 contenu_temp = contenu ;
					 } else {
						 contenu = httpLoader.getTextFile("https://api.twitter.com/1/"+type+"/ids.json?user_id="+tweetos+"&cursor="+next_cursor);
						 contenu_temp = contenu ;
					 }
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			 }
			 // gestion des erreurs suite à une requetes, nous en avons rencontrer que 2 : 
			 //		401 : Pas d'Autorisation  	et		402 : L'utilisateur n'existe plus
			 if (contenu.equals("NoAutor") || contenu.equals("Effacer")) {
				 if (contenu.equals("NoAutor")) {
					 System.out.println(" Autorisation réfusée pour le Tweetos "+tweetos);
				 } else {
					 System.out.println(" Erreur : 0 ");
					 System.out.println(" Tweetos "+tweetos+" n'existe plus ");
				 }
			 } 
			 else {
				 // récuperation du curseur suivant si la requete n'a pas suffit à finir l'utilisateur
				 // car nous sommes limités à 5000 Followers ou Followees par requetes et nous en voulons au max 25000
				 String supp = "{\"ids\":" ;
				 contenu_temp = contenu_temp.replace(supp, "");
				 contenu_temp = contenu_temp.replaceAll("\\[[^\\]]*\\]","");
				 supp = "{\"next_cursor_str\":\"0\",\"ids\":,\"previous_cursor_str\":\"0\",\"next_cursor\":" ;
				 contenu_temp = contenu_temp.replace(supp,"");
				 supp = ",\"next_cursor\":" ;
				 contenu_temp = contenu_temp.replace(supp,"");
				 Scanner scTemp = new Scanner (contenu_temp) ;
				 scTemp.useDelimiter(",");
				 if (scTemp.hasNextBigInteger()) {
					 next_cursor = scTemp.nextBigInteger();
				 } else {
					 next_cursor = BigInteger.ZERO ;
				 }
				 remplir(contenu,tweetos,type);
				 counter++;
				 System.out.println("check"+counter+" "+type+" id:"+tweetos);
				 if (!next_cursor.equals(BigInteger.ZERO)) {
					 contenu = httpLoader.getTextFile("https://api.twitter.com/1/"+type+"/ids.json?user_id="+tweetos+"&cursor="+next_cursor);
					 contenu_temp = contenu ;
				 }
			 }
		// boucle permettant de relancé plusieurs requetes pour un utilisateur et ainsi pouvoir arriver à 25000 quand cela est possible
		} while ((!next_cursor.equals(BigInteger.ZERO))&&(counter<5));
	 }
	 
	 public static void main (String [] args) {
		 
		 HashSet <Integer> nos_Capitalistes = new HashSet <Integer> () ;
		 nos_Capitalistes = recuperer_Capitaliste() ;
		 System.out.println(nos_Capitalistes.toString());
		 Iterator <Integer> it = nos_Capitalistes.iterator();
		 
		 while (it.hasNext()) {
			charger(BigInteger.valueOf(it.next()),"followers");
			charger(BigInteger.valueOf(it.next()),"following");
		 }
			 
		 
		 System.out.println(" Chargement fini !!! ") ;
		 
	 }
}
