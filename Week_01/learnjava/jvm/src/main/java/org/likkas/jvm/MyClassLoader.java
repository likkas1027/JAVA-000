package org.likkas.jvm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

public class MyClassLoader extends ClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> clazz = new MyClassLoader().findClass("Hello");
        Method hello = clazz.getMethod("hello");
        hello.invoke(clazz.newInstance(),null);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        String postfix = ".xlass";
        String classPath = this.getClass().getClassLoader().getResource(name + postfix).getPath();
        try {
            byte[] byteArray = Files.readAllBytes(new File(classPath).toPath());
            if (byteArray != null) {
                byte[] classFileByteArray = new byte[byteArray.length];
                for (int i = 0; i < byteArray.length; i++) {
                    classFileByteArray[i] = (byte) (255 - byteArray[i]);
                }
                clazz = defineClass(name, classFileByteArray, 0,classFileByteArray.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clazz;
    }
}
