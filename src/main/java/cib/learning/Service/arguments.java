package cib.learning.Service;

import java.util.HashMap;
import java.util.Map;

public class arguments {
    public Map<String, String> get(String[] args) {
        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                //System.out.println("Found dash with command " +
                //        args[i] + " and value " + args[i + 1]);
                options.put(args[i], args[i + 1]);
                i = i + 1;
            } else {
                System.out.println("Parametr is bad " + args[i]);
            }
        }
        return options;
    }
}
