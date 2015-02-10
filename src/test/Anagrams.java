package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * This programs find all anagrams in a text file and print them with out duplicated words 
 * Assumptions:
 * -The input is a text file with words separated by a new line character
 * -In case the name of the input file is missing a dummy txt file will be generated for the test
 * -In case the output file name is missing the default output.txt is used
 * -The words are case sensitive so words like dog God are not considered anagrams
 * -All words are trimmed when read
 * 
 * @author Cesar Hidalgo
 *
 */
public class Anagrams {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Anagrams anagrams = new Anagrams();
		if(args.length == 2){
			anagrams.findAnagrams(args[0], args[1]) ;
		}
		else if(args.length == 1){
			anagrams.findAnagrams(args[0], "output.txt") ;
		}
		else if(args.length == 0){
			try {
				anagrams.create("test.txt", 300000, 7);
				anagrams.findAnagrams("test.txt", "output.txt") ;
			} catch (IOException e) {
				System.err.println("Can't create dummy file: "+e.getMessage());
			}
		}
		else{
			System.out.println("Usage: Anagrams [fileName]");
		}
		
			
		

	}
	
	
	/**
	 * Finds and prints the anagrams in a file
	 * @param file
	 * @throws IOException
	 */
	private void findAnagrams(String file, String outputFile){
		try{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			Set<String> s = readFile(file);
			String[] words = s.toArray(new String[s.size()]);
			String[] sortedChars= sortedChars(words);
			int[] indexes = sortWords(sortedChars);
			for(int i = 0; i<words.length-1; i++){
				if(sortedChars[i].equals(sortedChars[i+1])){
					writer.write(words[indexes[i]]+" ");
					i++;
					while(i<words.length-1 && sortedChars[i].equals(sortedChars[i+1])){
						writer.write(words[indexes[i]]+" ");
						i++;
					}
					writer.write(words[indexes[i]]+" ");
					writer.newLine();
				}
				
				
			}
			writer.close();
		}
		catch(IOException e){
			System.err.println("Error reading the file: "+e.getMessage());
		}
	}
	
	
	/**
	 * Given a word return an string with all the characters ordered alphabetically
	 * @param word
	 * @return the characters of the word ordered
	 */
	private String sortedWord(String word){
		StringBuilder sortedWord = new StringBuilder();
		for(int i = 0; i<word.length(); i++){
			char c = word.charAt(i);
			int j=0;
			for(; j<sortedWord.length(); j++){
				if(c<sortedWord.charAt(j)){
					break;
				}
			}
			sortedWord.insert(j, c);
		}
		return sortedWord.toString();
	}
	
	/**
	 * Creates an array where all the characters of every String ar ordered alphabetically 
	 * @return
	 */
	private String[] sortedChars(String[] words){
		String[] sortedChar = new String[words.length];
		
		for(int i=0; i<sortedChar.length; i++){
			sortedChar[i] = sortedWord(words[i]);
		}
		
		return sortedChar;
	}
	
	
	/**
	 * Reads the file with words avoiding duplicates
	 * @param file
	 * @return A set with the words found in the file
	 * @throws IOException
	 */
	private Set<String> readFile(String file) throws IOException{
		HashSet<String> result = new HashSet<String>();
		BufferedReader reader = null;
		
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String s = reader.readLine();
			while(s!=null){
				result.add(s.trim());
				s = reader.readLine();
			}
		}
		finally{
			if(reader!=null)
				reader.close();
		}
		
		return result;
	}
	
	/**
	 * Sort the words returning its the original index at indexes array
	 * @param words
	 * @param indexes
	 */
	private int[] sortWords(String[] words)
	{
		int[] indexes= new int[words.length];
		for(int i=0; i<indexes.length;i++){
			indexes[i]=i;
		}
		String[] tmp = new String[words.length];
		int[] indexesTmp = new int[indexes.length];
		mergeSort(words, tmp,  0,  words.length - 1, indexes, indexesTmp);
		return indexes;
	}


	/**
	 * This method sort an array of string with merge sort algorithm sorting keeping the original
	 * indexes in the indexes array
	 */
	private void mergeSort(String [] words, String[] tmp, int left, int right, int[] indexes, int[] indexesTmp)
	{
		if( left < right )
		{
			int center = (left + right) / 2;
			mergeSort(words, tmp, left, center, indexes, indexesTmp);
			mergeSort(words, tmp, center + 1, right, indexes, indexesTmp);
			merge(words, tmp, left, center + 1, right, indexes, indexesTmp);
		}
	}


	/**
	 * Implementation of the merge according to the merge sort algorithm
	 */
    private static void merge(String[] words, String[] wordsTmp, int left, int right, int rightEnd,  int[] indexes, int[] indexesTmp)
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(words[left].compareTo(words[right]) <= 0){
                wordsTmp[k] = words[left];
                indexesTmp[k++] = indexes[left++];
            }
            else{
                wordsTmp[k] = words[right];
                indexesTmp[k++] = indexes[right++];
            }

        while(left <= leftEnd) { 
            wordsTmp[k] = words[left];
            indexesTmp[k++] = indexes[left++];
        }

        while(right <= rightEnd) { 
            wordsTmp[k] = words[right];
            indexesTmp[k++] = indexes[right++];
        }
        
        for(int i = 0; i < num; i++, rightEnd--){
        	words[rightEnd] = wordsTmp[rightEnd];
        	indexes[rightEnd] = indexesTmp[rightEnd];
        }
    }
    
    
    /**
     * Creates a dummy test file for anagrams program
     * @param string
     * @throws IOException 
     */
    private void create(String string, int wordNumber, int wordLength) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(string)));
		for(int i = 0; i<wordNumber;i++){
			int length = (int)(Math.random()*wordLength);
			StringBuilder sb = new StringBuilder();
			for(int j = 0; j<length;j++){
				char c = (char)(97+((int)(Math.random()*26)));
				sb.append(c);
			}
			writer.write(sb.toString());
			writer.newLine();
			
		}
		writer.close();
		
		
	}

}
