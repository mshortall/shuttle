
package Cacher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cacher {
    
    private static int maxCache;
    private Logger logger;
    private final Map<String, byte[]> map;
    
    protected Cacher(final Logger logger, final int maxCache) {
        Cacher.maxCache = maxCache;
        this.logger = logger;
        this.map =
                Collections.synchronizedMap(new LinkedHashMap<String, byte[]>(Cacher.maxCache + 1, .75F, true) {
                    @Override
                    public boolean removeEldestEntry(Map.Entry<String, byte[]> eldest) {
                        return size() > Cacher.maxCache;
                    }
                });
    }
    
    protected byte[] cacheRequest(final String name) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        byte[] array = null;
        if (map.containsKey(name)) {
            array = map.get(name);
            //TODO: Add Hit Logging
            //logger.write(dateFormat.format(date) + " : cacheRequest : <INFO> HIT : hit on key <" +name+ ">"
            //        + " with value <" +map.get(name)+">");
        } else {
            //TODO: Add Miss Logging
            //logger.write(dateFormat.format(date) + " : cacheRequest : <INFO> MISS : miss on key <" +name+ ">"
        }
        return array;
    }
    
    protected void cacheAdd(final String name, final byte[] content) {
        map.put(name,content);
    }
}