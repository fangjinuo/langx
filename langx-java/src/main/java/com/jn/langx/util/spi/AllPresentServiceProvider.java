package com.jn.langx.util.spi;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.reflect.annotation.OnClassesConditions;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class AllPresentServiceProvider<T> implements ServiceProvider<T> {
    private boolean defaultValueIfMissOnClassesAnnotation = true;

    public AllPresentServiceProvider() {

    }

    public AllPresentServiceProvider(boolean defaultValueIfMissOnClassesAnnotation) {
        this.defaultValueIfMissOnClassesAnnotation = defaultValueIfMissOnClassesAnnotation;
    }

    @Override
    public Iterator<T> get(Class<T> serviceClass) {
        ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
        Iterator<T> iter = loader.iterator();
        List<T> ret = Collects.emptyArrayList();
        while (iter.hasNext()) {
            T t = iter.next();
            if (OnClassesConditions.allPresent(t.getClass(), defaultValueIfMissOnClassesAnnotation)) {
                ret.add(t);
            }
        }
        return ret.iterator();
    }
}
