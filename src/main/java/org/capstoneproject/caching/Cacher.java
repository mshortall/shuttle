package org.capstoneproject.caching;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.capstoneproject.logging.Logger;

public class Cacher {
    
    //Declare Global Variables
    private static int maxCache;
    private Logger logger;
    private final Map<String, byte[]> map;
    
    //Get Method To Return Logger Object For RequestHandler
    public Logger getLogger() { 
        return this.logger; }

    //Constructor
    public Cacher(final Logger logger, final int maxCache) {
        Cacher.maxCache = maxCache;
        this.logger = logger;
        this.map =
                Collections.synchronizedMap(new LinkedHashMap<String, byte[]>(Cacher.maxCache + 1, .75F, true) {
                    //Method to Implement LRU Algorithm
                    @Override
                    public boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
                        return size() > Cacher.maxCache;
                    }
                });
    }
    
    //Method Call from ReqhestHandler for Object in Cache
    public byte[] cacheRequest(final String name) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        byte[] array = null;
        if (map.containsKey(name)) {
            array = map.get(name);
            logger.log("Norm", "Cacher", "<INFO> HIT : Hit on key <" +name+ ">"
                    + " with value <" +map.get(name)+">");
						logger.close();
        } else {
            logger.log("Norm", "Cacher", "<INFO> MISS : Miss on key <" +name+ ">");
        		logger.close();
				}
        return array;
    }
    
    //Method to Add a Page to Cache (RequestHandler)
    public void cacheAdd(final String name, final byte[] content) {
        map.put(name,content);
    }
}
