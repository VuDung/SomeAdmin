package com.some.admin.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Utils {
	

	public static void compareLists(List firstList, List secondList){
		Map mapForFirstList = new HashMap();
		Map mapForSecondList = new HashMap();
		Iterator firstListIterator = firstList.iterator();
		Iterator secondListIterator = secondList.iterator();
		while(firstListIterator.hasNext()){
			String firstListKeyValue = firstListIterator.next().toString();
			/**
             * Put the value from the list into the map, only if the same value
             * already does not exists. That means if there are duplicates, we
             * put only one instance into the hashmap.
             */
            if (!mapForFirstList.containsKey(firstListKeyValue)) {
                mapForFirstList.put(firstListKeyValue, firstListKeyValue);
            }
		}
		while (secondListIterator.hasNext()) {
	        String secondListKeyValue = secondListIterator.next().toString();
	        /**
	         * Put the value from the list into the map, only if the same value
	         * already does not exists. That means if there are duplicates, we
	         * put only one instance into the hashmap.
	         */
			if (!mapForSecondList.containsKey(secondListKeyValue)) {
			    mapForSecondList.put(secondListKeyValue, secondListKeyValue);
			}
	 
        }
		compareAndPrintResults(mapForFirstList, mapForSecondList);
	}
	
	private static	void compareAndPrintResults(Map mapForFirstList,
	            Map mapForSecondList) {
	        /** Compare first map against the second one and print the difference **/
	        printItemsFromFirstListThatAreNotOnSecondList(mapForFirstList,
	                mapForSecondList);
	        /** Compare second map against the first and print the difference */
	        printItemsFromSecondListThatAreNotOnFirstList(mapForSecondList,
	                mapForFirstList);
	 
	    }
	private static void printItemsFromFirstListThatAreNotOnSecondList(Map mapA, Map mapB) {
		System.out.println("***Items from first list that are not in second list");
        doComparisionAndPrint(mapA, mapB);
 
    }
	 
    private static void printItemsFromSecondListThatAreNotOnFirstList(Map mapA, Map mapB) {
        System.out.println("***Items from second list that are not in firstList list");
        doComparisionAndPrint(mapA, mapB);
    }
	 
	/**
	 * @param mapA
	 * @param mapB
	 *
	 * This method compares two hashmaps and prints out the values
	 * from the first one that are not in the second one.
	 */
	private static void doComparisionAndPrint(Map mapA, Map mapB) {
	    // both maps should be non-empty for comparison.
        if (mapA != null && mapB != null) {
            Iterator mapAIterator = mapA.keySet().iterator();
            while (mapAIterator.hasNext()) {
                String key = mapAIterator.next().toString();
                if (!mapB.containsKey(key)) {
                    System.out.println(key);
                }
            }
        }
    }
}
