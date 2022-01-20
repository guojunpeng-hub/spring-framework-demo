package com.spring;

import com.spring.annotation.AutoWired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoguo
 * @date2022/1/20 0020 16:17
 */
public class SpringApplicationContext {

    private Class config;

    private Map<String, BeanDefination> beanDefinationMap = new HashMap<>();
    private Map<String, Object> singletonMap = new HashMap<>();


    public SpringApplicationContext(Class config) throws ClassNotFoundException {
        this.config = config;
        //模拟扫描
        scan(config);

        for (Map.Entry<String, BeanDefination> entry : beanDefinationMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefination beanDefination = beanDefinationMap.get(beanName);
            String scope = beanDefination.getScope();
            if ("singleton".equals(scope)) {
                Object bean = createBean(beanName, beanDefination);
                singletonMap.put(beanName, bean);
            }
        }
    }

    public Object createBean(String beanName, BeanDefination beanDefination) {
        Class clazz = beanDefination.getType();
        Object instance = null;
        try {
            instance = clazz.getConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                if(field.isAnnotationPresent(AutoWired.class)){
                    AutoWired annotation = field.getAnnotation(AutoWired.class);
                    String value = annotation.value();
                    field.setAccessible(true);
                    field.set(instance,getBean(value));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public Object getBean(String beanName) {
        if (!beanDefinationMap.containsKey(beanName)) {
            //在未加载的情况下
            throw new NullPointerException();
        }
        BeanDefination beanDefination = beanDefinationMap.get(beanName);
        String scope = beanDefination.getScope();
        if ("singleton".equals(scope)) {
            Object o = singletonMap.get(beanName);
            if(o==null){
                singletonMap.put(beanName,createBean(beanName,beanDefination));
            }
            return singletonMap.get(beanName);
        } else {
            return createBean(beanName, beanDefination);
        }
    }

    public void scan(Class config) throws ClassNotFoundException {
        if (config.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan annotation = (ComponentScan) config.getAnnotation(ComponentScan.class);
            String path = annotation.value();
            path = path.replace(".", "/");
            System.out.println(path);
            ClassLoader classLoader = SpringApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);

            //找到需要加载的目录
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                    absolutePath = absolutePath.replace("\\", ".");
                    System.out.println(absolutePath);
                    Class<?> clazz = classLoader.loadClass(absolutePath);
                    if (clazz.isAnnotationPresent(Component.class)) {
                        System.out.println(clazz);
                        Component annotation1 = clazz.getAnnotation(Component.class);
                        String beanName = annotation1.value();
                        if("",equals(beanName)){
                            beanName = Introspector.decapitalize(clazz.getSimpleName());
                        }
                        BeanDefination beanDefination = new BeanDefination();
                        beanDefination.setType(clazz);
                        //判断是否为原型还是单例
                        if (clazz.isAnnotationPresent(Scope.class)) {
                            Scope scope = clazz.getAnnotation(Scope.class);
                            String value = scope.value();
                            beanDefination.setScope(value);
                        } else {
                            //仅单例
                            beanDefination.setScope("singleton");
                        }
                        beanDefinationMap.put(beanName, beanDefination);
                        //创建bean
                    }

                }
            }
        }
    }


}
