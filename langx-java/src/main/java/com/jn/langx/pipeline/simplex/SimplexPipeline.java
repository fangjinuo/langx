package com.jn.langx.pipeline.simplex;


public interface SimplexPipeline {
    void addFirst(SimplexHandler handler);

    void addLast(SimplexHandler handler) ;

     void clear();

    public Object handle(Object message);

}
