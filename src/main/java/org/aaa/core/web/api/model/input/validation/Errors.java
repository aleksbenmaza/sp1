package org.aaa.core.web.api.model.input.validation;

import com.google.gson.annotations.SerializedName;

import org.aaa.core.web.app.model.Command;

import java.lang.reflect.Field;

import java.util.*;

/**
 * Created by alexandremasanes on 21/04/2017.
 */
public class Errors extends HashMap<String, String> {

    private static final HashMap<Class<? extends Command>, HashSet<Entry>> commandsFieldsErrors;

    private Class<? extends Command> clazz;

    static {
        commandsFieldsErrors = new HashMap<Class<? extends Command>,
                HashSet<Entry>>();
    }


    private static class Entry implements Map.Entry<Field, String> {
        private final Field key;
        private String value;

        public Entry(Field key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Field getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            if (key != null ? !key.equals(entry.key) : entry.key != null) return false;
            return value != null ? value.equals(entry.value) : entry.value == null;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    public Errors(Class<? extends Command> clazz) {
        super();
        this.clazz = clazz;
        init();
    }

    @Override
    public String put(String key, String value) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> errors) {
    }

    @Override
    public String remove(Object key) {
        return null;
    }

    @Override
    public void clear() {
    }

    public void rejectValue(String fieldName, String message) {

      for(Map.Entry<Class<? extends Command>, HashSet<Entry>> entry : commandsFieldsErrors.entrySet())
          if(entry.getKey().equals(clazz))
              for(Entry entry1 : entry.getValue())
                  if(entry1.getKey().getName().equals(fieldName))
                      putMessage(entry1.getValue(), message);

    }

    private void putMessage(String key, String value) {
        super.put(key, value);
    }

    private void init() {
        String fieldName;
        HashSet<Entry> fieldsNamesAndErrors;
        Entry fieldErrorEntry;
        boolean bool1, bool2;

        System.out.println("class: "+clazz);
        if(!commandsFieldsErrors.containsKey(clazz))
            fullFill(clazz);

    }

    private static void fullFill(Class<? extends Command> commandClass) {
        HashSet<Entry> fieldsNameAndErrors = new HashSet<Entry>();
        commandsFieldsErrors.put(commandClass, fieldsNameAndErrors);
        String fieldName;
        Entry fieldErrorEntry;
        boolean usingJackson;

        for(Field field : commandClass.getDeclaredFields()) {
            fieldErrorEntry = new Entry(field, null);
            fieldsNameAndErrors.add(fieldErrorEntry);
            if((usingJackson = field.isAnnotationPresent(SerializedName.class)) ^ field.isAnnotationPresent(SerializedName.class))
                if (usingJackson)
                    fieldErrorEntry.setValue(field.getAnnotation(SerializedName.class).value());

                else
                    fieldErrorEntry.setValue(field.getAnnotation(SerializedName.class).value());

            else
                fieldErrorEntry.setValue(field.getName());
        }
    }
}