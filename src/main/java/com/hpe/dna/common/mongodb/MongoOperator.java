/**
 * 
 */
package com.hpe.dna.common.mongodb;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bson.Document;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chee-kin.lim@hpe.com
 *
 */
public class MongoOperator {
	private static final String DOT = ".";
	private static final String RE_DOT = "\\.";	
	private static final String DOLLAR_SIGN = "$";
	private static final String UNDERSCORE = "_";
	
	public static Map<String, Object> eq(Object o1, Object o2) {
		return ImmutableMap.of("$eq", ImmutableList.of(o1, o2));
	}
	
	public static Map<String, Object> lte (Object o1, Object o2) {
		return ImmutableMap.of("$lte", ImmutableList.of(o1, o2));
	}	

	public static Map<String, Object> and (Object o1, Object o2) {
		return ImmutableMap.of("$and", ImmutableList.of(o1, o2));
	}
	
    public static Map<String, Object> cond(Map<String, Object> booleanExpression, Object trueCase, Object falseCase) {
    	return ImmutableMap.of("$cond", ImmutableList.of(booleanExpression, // if
            	trueCase,   // then
            	falseCase // else
        ));
    }
    
    public static Document include(String[] fieldNames) {
    	Map<String, Object> include = new LinkedHashMap<String, Object>(fieldNames.length);
    	
        for (String fieldName: fieldNames) {
        	if (fieldName.indexOf(DOT) == -1) {
        		include.put(fieldName, true);
        	} else {
        		include.put(fieldName.replaceAll(RE_DOT, UNDERSCORE), 
        				DOLLAR_SIGN.concat(fieldName));
        	}
        }
        
        return new Document(include);
    }   
}
