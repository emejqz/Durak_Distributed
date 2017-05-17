/*
 * @author Eric M Evans
 * 
 * Has methods to relate KeyEvent codes to indices for defense and attack moves
 */
//package view;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DurakKeys {
	
	private final static Map<Integer, Integer>	attackIndexToKeyEventCode;
	private final static Map<Integer, Integer>	attackKeyEventCodeToIndex;
	private final static Map<Integer, Integer>	defendIndexToKeyEventCode;
	private final static Map<Integer, Integer>	defendKeyEventCodeToIndex;
	static {
		/*
		 * Attack Keys
		 */
		final int[] atackEvents = {KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E,
		        KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U,
		        KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_A,
		        KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G,
		        KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L,
		        KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V,
		        KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M};
		
		Map<Integer, Integer> atackIntToEventMap = new HashMap<>();
		Map<Integer, Integer> attackEventToIntMap = new HashMap<>();
		for (int i = 0; i < atackEvents.length; i++) {
			atackIntToEventMap.put(i, atackEvents[i]);
			attackEventToIntMap.put(atackEvents[i], i);
		}
		attackIndexToKeyEventCode = Collections
		        .unmodifiableMap(atackIntToEventMap);
		attackKeyEventCodeToIndex = Collections
		        .unmodifiableMap(attackEventToIntMap);
		
		/*
		 * Defense Keys
		 */
		final int[] defendEvents = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3,
		        KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7,
		        KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_0, KeyEvent.VK_MINUS,
		        KeyEvent.VK_EQUALS, KeyEvent.VK_BACK_QUOTE,
		        KeyEvent.VK_OPEN_BRACKET, KeyEvent.VK_CLOSE_BRACKET,
		        KeyEvent.VK_BACK_SLASH, KeyEvent.VK_SEMICOLON,
		        KeyEvent.VK_QUOTE, KeyEvent.VK_COMMA, KeyEvent.VK_PERIOD,
		        KeyEvent.VK_SLASH, KeyEvent.VK_TAB, KeyEvent.VK_BACK_SPACE,
		        KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_WINDOWS};
		
		Map<Integer, Integer> defendIntToEventMap = new HashMap<>();
		Map<Integer, Integer> defendEventToIntMap = new HashMap<>();
		for (int i = 0; i < defendEvents.length; i++) {
			defendIntToEventMap.put(i, defendEvents[i]);
			defendEventToIntMap.put(defendEvents[i], i);
		}
		defendIndexToKeyEventCode = Collections
		        .unmodifiableMap(defendIntToEventMap);
		defendKeyEventCodeToIndex = Collections
		        .unmodifiableMap(defendEventToIntMap);
	}
	
	
	
	/*
	 * return int index from key event code for playing attack cards via
	 * keyboard
	 */
	static int getAttackIndexForKeyEventCode(int i) {
		
		return attackKeyEventCodeToIndex.get(i);
	}
	
	
	
	/*
	 * return key code for playing attack cards via keyboard
	 */
	private static int getAttackKeyEventCodeForIndex(int i) {
		
		return attackIndexToKeyEventCode.get(i);
	}
	
	
	
	/*
	 * return key string for playing attack cards via keyboard
	 */
	static String getAttackKeyName(int i) {
		
		return KeyEvent.getKeyText(getAttackKeyEventCodeForIndex(i));
	}
	
	
	
	/*
	 * return int index from key event code for playing defend cards via
	 * keyboard
	 */
	static int getDefendIndexForKeyEventCode(int i) {
		
		return defendKeyEventCodeToIndex.get(i);
	}
	
	
	
	/*
	 * return key code for playing defend cards via keyboard
	 */
	private static int getDefendKeyEventCodeForIndex(int i) {
		
		return defendIndexToKeyEventCode.get(i);
	}
	
	
	
	/*
	 * return key string for playing defend cards via keyboard
	 */
	static String getDefendKeyName(int i) {
		
		return KeyEvent.getKeyText(getDefendKeyEventCodeForIndex(i));
	}
}
