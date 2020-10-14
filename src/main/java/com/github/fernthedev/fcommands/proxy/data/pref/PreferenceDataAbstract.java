package com.github.fernthedev.fcommands.proxy.data.pref;

import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class PreferenceDataAbstract<T extends Serializable> implements Serializable {

    protected String name;

    protected T value;

    private String className;

    public PreferenceDataAbstract(Class<T> tClass) {
        this.className = tClass.getName();
    }

    public void setValue(T value) {
        this.value = value;

        try {
            isValid(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Value is not valid. " + value, e);
        }
    }

    public PreferenceDataAbstract(String name, T value, Class<T> tClass) {
        this.name = name;
        this.className = value.getClass().getName();
        setValue(value);
    }

    public PreferenceDataAbstract(String name, @NonNull T value) {
        this.name = name;
        this.className = value.getClass().getName();
        setValue(value);
    }


    public abstract List<T> possibleValues();

    public List<String> possibleValuesString() {
        return possibleValues().parallelStream().map(Object::toString).collect(Collectors.toList());
    }

    protected void invalidFormat(String s) throws IllegalArgumentException {
        throw new IllegalArgumentException(s + " is not of type " + className + ". Possible values: " + possibleValuesString().toString());
    }

    public abstract void isValid(Object value) throws Exception;

    public abstract T parse(String value) throws Exception;


    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public String getClassName() {
        return this.className;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreferenceDataAbstract)) return false;
        PreferenceDataAbstract<?> that = (PreferenceDataAbstract<?>) o;
        return name.equals(that.name) &&
                value.equals(that.value) &&
                className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, className);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PreferenceDataAbstract{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append(", className='").append(className).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
