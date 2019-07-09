package eu.heroria.chat;

import java.util.ArrayList;

import eu.heroria.Main;

public class ProhibitedWordManager {
	private ArrayList<String> prohibitedWord = new ArrayList<String>();
	
	public ProhibitedWordManager() {}

	public void addRule(String word) {
		System.out.println("Word '" + word.toLowerCase() + "' has been prohibited in the chat.");
		prohibitedWord.add(word.toLowerCase());
	}
	
	public boolean containsProhibitedWord(String msg) {
		if(prohibitedWord.contains(msg.toLowerCase())) return true;
		return false;
	}
}
