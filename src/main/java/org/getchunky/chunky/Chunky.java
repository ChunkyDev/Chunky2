package org.getchunky.chunky;

import org.getchunky.chunkyapi.persistence.EBeansUtil;

public class Chunky {

    private static ChunkyPlugin pluginInstance;
    private static EBeansUtil eBeansUtilInstance;

    public static void cleanUp() {

    }

    public static ChunkyPlugin getPlugin() {
        return Chunky.pluginInstance;
    }

    public static void setPlugin(ChunkyPlugin pluginInstance) {
        Chunky.pluginInstance = pluginInstance;
    }

    public static EBeansUtil getEBeansUtil() {
        return Chunky.eBeansUtilInstance;
    }

    public static void setEBeansUtil(EBeansUtil eBeansUtilInstance) {
        Chunky.eBeansUtilInstance = eBeansUtilInstance;
    }
}
