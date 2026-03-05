package net.klayil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.SneakyThrows;
import net.klayil.veggycraft.VeggyCraft;
import org.jetbrains.annotations.NotNull;

import javax.naming.LimitExceededException;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.invoke.TypeDescriptor;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SequencedList<T> extends ArrayList<T> implements SequencedCollection<T> {
    public static class IncompatibleTypeError extends Exception implements Serializable {
        private Exception cause;
        private String msg;

        @Override
        public String toString() {
            String s = getClass(new Object()).getName();
            String message = getLocalizedMessage();

            assert msg != null;

            return s + ": " + message;
        }

        public static class MetaClass implements AnnotatedElement, GenericDeclaration, TypeDescriptor.OfField<Class<?>>, Type, Constable, Serializable {
            private final Class<IncompatibleTypeError> value;

            private MetaClass(Class<?>... params) {
                ArrayList<Class<?>> multableParams = new ArrayList<>(Arrays.stream(params).toList());
                multableParams.add(IncompatibleTypeError.class);

                try {
                    value = (Class<IncompatibleTypeError>) multableParams.getFirst();
                } catch (Exception e) {
                    throw new ClassCastException(e.getMessage());
                }
            }

            public String getName() {
                return value.getName();
            }

            @Override
            public Optional<? extends ConstantDesc> describeConstable() {
                return Optional.empty();
            }

            @Override
            public boolean isArray() {
                return value.isArray();
            }

            @Override
            public boolean isPrimitive() {
                return value.isPrimitive();
            }

            @Override
            public Class<?> componentType() {
                return value.componentType();
            }

            @Override
            public Class<?> arrayType() {
                return value.arrayType();
            }

            @Override
            public String descriptorString() {
                return value.descriptorString();
            }

            @Override
            public TypeVariable<?>[] getTypeParameters() {
                return value.getTypeParameters();
            }

            @Override
            public <T extends Annotation> T getAnnotation(@NotNull Class<T> annotationClass) {
                return value.getAnnotation(annotationClass);
            }

            @Override
            public @NotNull Annotation @NotNull [] getAnnotations() {
                return value.getAnnotations();
            }

            @Override
            public @NotNull Annotation @NotNull [] getDeclaredAnnotations() {
                return value.getDeclaredAnnotations();
            }
        }

        final public MetaClass getClass(Object... ignore) {
            getCause();
            getMessage();

            return new MetaClass();
        }

        @Override
        public IncompatibleTypeError getCause() {
            assert cause != null;
            return (IncompatibleTypeError) cause;
        }

        @Override
        public String getMessage() {
            assert msg != null;
            return msg;
        }

        @Override
        public String getLocalizedMessage() {
            return getMessage();
        }

        public IncompatibleTypeError(Object... params) {
            cause = this;
            msg = cause.getMessage();

            for (int i = 0; i < Math.min(2, params.length); i++) {
                Object param = params[i];

                if (param instanceof String) {
                    msg = (String) param;
                    continue;
                }

                if (!(param instanceof Exception c)) {
                    throw new ClassCastException("wrong parameter type");
                }
                cause = c;

                msg = cause.getMessage();
            }

            throw new RuntimeException(msg, cause);
        }
    }


    private SequencedCollection<T> value;

//    public class IncompatibleTypeError implements Serializable {
//    }

    SequencedList(SequencedCollection<T> parsedParam) throws IncompatibleTypeError {
        IncompatibleTypeError[] err = {null};

        parsedParam.stream().forEachOrdered(elem -> {
            if (elem != null && elem.getClass() == Object.class) {
                try {
                    throw new IncompatibleTypeError("SequencedList elements may not be primitive Object classes");
                } catch (IncompatibleTypeError e) {
                    err[0] = e;
                }
            }
        });

        if (err[0] != null) throw err[0];

        value = parsedParam;
    }

    @Override
    public String toString() {
        String res = "SequencedList{%s}";
        StringBuilder app = new StringBuilder();

        for (int i = 0; i < this.size(); i++) {
            String app2 = ", ";
            if (i + 1 == this.size()) app2 = "";

            Object objConv = this.get(i);
            if (objConv == null)
                objConv = "null";

            app.append(objConv.toString()).append(app2);
        }

        return res.formatted(app.toString());
    }

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        String msg = "parameter casted as a null value";

        T containVal;

        if (o != null && o.getClass().getName().equals(Object.class.getName()))
            throw new ClassCastException("parameter may not be an Object");

        containVal = (T) o;

        if (containVal != null) {
            return value.contains(containVal);
        }

        throw new AssertionError(msg);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return value.iterator();
    }

    private void limitExceeded(Integer index) throws LimitExceededException {
        throw new LimitExceededException("index exceeded the limit of %d, got index is %d".formatted(value.size(), index));
    }
    @SneakyThrows
    private int forEachLambdaParsing(T e, T[] res, int index) throws LimitExceededException {
        if (index+1 > value.size()) limitExceeded(index);

        res[index] = e;
        index++;

        if(index > value.size()) limitExceeded(index);
        
        return index;
    }
    @Override
    @SneakyThrows
    public @NotNull T @NotNull [] toArray() {
        T[] res;

        try {
            res = (T[]) new Object[value.size()];
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }

        SequencedList<T> valueToParse = new SequencedList<>(value);

        AtomicInteger index = new AtomicInteger();
        valueToParse.stream().forEachOrdered(e -> {
            try {
                index.set(forEachLambdaParsing(e, res, index.get()));
            } catch (LimitExceededException ex) {
                throw new RuntimeException(ex);
            }
        });

        return res;
    }

    @Override
    public @NotNull <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] paramArray) {
        final String msg = "the type %s of element cannot be cast";

        T[] otherArray = this.toArray();

        for (int i = 0; i < paramArray.length; i++) {
            if (otherArray[i] == null) {
                throw new AssertionError(msg.formatted("<NULL>"));
            } else {
                @NotNull T otherElement = otherArray[i];

                T1 outputElement;
                try {
                    outputElement = (T1) otherElement;
                } catch (ClassCastException e) {
                    throw new ClassCastException(e.getMessage());
                }

                if (outputElement == null) {
                    String type = "Object";

                    try {
                        type = otherElement.getClass().getName();
                    } catch (NullPointerException e) {
                        throw new AssertionError(e.getMessage() + " | " + msg.formatted(type));
                    }

                   if (!type.equals("Object")) {
                       throw new AssertionError(msg.formatted(type));
                   }
                } else {
                    paramArray[i] = outputElement;
                }
            }
        }

        return paramArray;
    }

    @Override
    public boolean add(T t) {
        int sizeBefore = value.size();
        boolean res = value.add(t);
        return (sizeBefore != value.size()) && res;
    }

    @Override
    public boolean remove(Object o) {
        boolean contained = this.contains(o);
        if (!contained) return false;

        return value.remove((T) o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> others) {
        boolean res;

        try {
            res = value.containsAll(new SequencedList<>((SequencedCollection<T>) others));
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }

        return res;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return this.addAll(value.size(), c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        try {
            Method method = value.getClass().getMethod("addAll", int.class, Collection.class);

            method.setAccessible(true);


            try {
                return (boolean) method.invoke(value, index, c);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        SequencedCollection<T> sequencedParam;

        try {
            sequencedParam = new SequencedList<>((SequencedCollection<T>) c);
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }

        return value.removeAll(sequencedParam);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        SequencedCollection<T> sequencedParam;

        try {
            sequencedParam = new SequencedList<>((SequencedCollection<T>) c);
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage());
        }

        return value.retainAll(sequencedParam);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @Override
    public T get(int index) {
        return toArray()[index];
    }

    @SneakyThrows
    @Override
    public T set(int index, T element) {
        T[] remadeArray = null;

        if (index < 0) {
            do {
                index = this.size() - index;
            } while (index < 0);
        }

        if (index > 0 && index < this.size()) remadeArray = toArray();

        Closeable closeable;
        try {
             closeable = (Closeable) Files.list(Path.of(VeggyCraft.workingDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (remadeArray == null) {
            Exception superclass = new Exception("could not instantiate a sequenced list...");

            throw ValueInstantiationException.from((JsonParser) closeable, "could not instantiate a sequenced list...", (JavaType) (Serializable) superclass, superclass);
//            try {
//            } catch (ValueInstantiationException e) {
//                e.printStackTrace(System.err);
//            }
        }

        try {
            remadeArray[index] = element;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        value = new SequencedList(Arrays.stream(remadeArray).toList());

        return element;
    }

    @Override
    public void add(int index, T element) {
        addAll(index, List.of(element));
    }

    @Override
    public T remove(int index) {
        T res = get(index);

        for (int i = index; i < this.size()-1; i++) {
            set(i, get(i+1));
        }

        value.removeLast();

        return res;
    }

    @Override
    public int indexOf(Object o) {
        if (o != null && o.getClass() == Object.class)
            throw new ClassCastException("parameter must not be an Object");

        for (int i = 0; i < this.size(); i++) {
            if (get(i) == o) return i;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o != null && o.getClass() == Object.class)
            throw new ClassCastException("parameter must not be an Object");

        int res = -1;

        for (int i = 0; i < this.size(); i++) {
            if (get(i) == o) res = i;
        }

        return res;
    }

    @Override
    public @NotNull ListIterator<T> listIterator() {
        return (ListIterator<T>) value.iterator();
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return ((ArrayList<T>) value).listIterator(index);
    }

    @Override
    public @NotNull List<T> subList(int fromIndex, int toIndex) {
        ArrayList<T> res = new ArrayList<>();
        for (int i = fromIndex; i <= toIndex; i++) {
            try {
                res.add(get(i));
            } catch (IndexOutOfBoundsException e) {
                if (i != toIndex) throw e;
            }
        }

        return res;
    }
}
