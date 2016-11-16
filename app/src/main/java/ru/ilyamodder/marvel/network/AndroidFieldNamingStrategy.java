package ru.ilyamodder.marvel.network;

import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by ilya on 16.11.16.
 */

public class AndroidFieldNamingStrategy implements FieldNamingStrategy {

    @Override
    public String translateName(final Field f) {
        if (f.getName().startsWith("m")) {
            return Character.toLowerCase(f.getName().charAt(1)) + f.getName().substring(2);
        }
        else {
            throw new IllegalArgumentException("Don't know how to handle field not starting with m prefix: " + f.getName());
        }
    }
}
