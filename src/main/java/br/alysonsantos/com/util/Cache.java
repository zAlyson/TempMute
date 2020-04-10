package br.alysonsantos.com.util;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

public abstract class Cache<T> {

    @Getter
    private Set<T> elements = new HashSet<>();

    public boolean contains(T element) {
        return elements.contains(element);
    }

    public void addElement(T toAdd) {
        elements.add(toAdd);
    }

    public boolean removeElement(T toRemove) {
        return elements.remove(toRemove);
    }

    public T get(Predicate<T> predicate) {
        for (T element : elements)
            if (predicate.test(element)) return element;

        return null;
    }

    public T getAndRemove(Predicate<T> predicate) {
        T element = get(predicate);
        if (element != null) removeElement(element);

        return element;
    }

    public T[] getAll(Predicate<T> predicate) {
        List<T> array = new LinkedList<>();

        for (T element : elements) {
            if (predicate.test(element)) array.add(element);
        }

        return (T[]) array.toArray();
    }

    public Optional<T> find(Predicate<T> predicate) {
        return Optional.ofNullable(get(predicate));
    }

    public Optional<T> findAndRemove(Predicate<T> predicate) {
        Optional<T> optional = find(predicate);
        optional.ifPresent(this::removeElement);

        return optional;
    }

    public ImmutableList<T> toImmutable() {
        return ImmutableList.copyOf(elements);
    }

    public Iterator<T> iterator() {
        return elements.iterator();
    }

    public int size() {
        return elements.size();
    }

}
