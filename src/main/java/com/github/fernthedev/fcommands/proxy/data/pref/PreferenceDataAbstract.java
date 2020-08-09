package com.github.fernthedev.fcommands.proxy.data.pref;

import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PreferenceDataAbstract<T> {

    protected String name;

    protected T value;

    private final Class<T> tClass;

    public PreferenceDataAbstract(Class<T> tClass) {
        this.tClass = tClass;
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
        this.tClass = tClass;
        setValue(value);
    }

    public PreferenceDataAbstract(String name, @NonNull T value) {
        this.name = name;
        this.tClass = (Class<T>) value.getClass();
        setValue(value);
    }


    public abstract List<T> possibleValues();

    public List<String> possibleValuesString() {
        return possibleValues().parallelStream().map(Object::toString).collect(Collectors.toList());
    }

    protected void invalidFormat(String s) throws IllegalArgumentException {
        throw new IllegalArgumentException(s + " is not of type " + tClass.getSimpleName() + ". Possible values: " + possibleValuesString().toString());
    }

    public abstract void isValid(Object value) throws Exception;

    public abstract T parse(String value) throws Exception;


    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public Class<T> getTClass() {
        return this.tClass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PreferenceDataAbstract)) return false;
        final PreferenceDataAbstract<?> other = (PreferenceDataAbstract<?>) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
        final Object this$tClass = this.getTClass();
        final Object other$tClass = other.getTClass();
        if (this$tClass == null ? other$tClass != null : !this$tClass.equals(other$tClass)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PreferenceDataAbstract;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        final Object $tClass = this.getTClass();
        result = result * PRIME + ($tClass == null ? 43 : $tClass.hashCode());
        return result;
    }

    public String toString() {
        return "PreferenceDataAbstract(name=" + this.getName() + ", value=" + this.getValue() + ", tClass=" + this.getTClass() + ")";
    }
}
