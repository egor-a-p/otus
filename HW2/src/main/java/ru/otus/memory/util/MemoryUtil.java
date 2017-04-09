package ru.otus.memory.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import sun.misc.Unsafe;


/**
 * @author Petrov Egor. Created 08.04.17.
 */
public class MemoryUtil {
    private static final Unsafe UNSAFE;
    private static final long MIN_SIZE;
    private static final long ALIGNMENT = 8;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
            MIN_SIZE = UNSAFE.objectFieldOffset(new Object() {
                byte aByte;
            }.getClass().getDeclaredField("aByte"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static long sizeOf(Object o) {
        if (o == null) {
            return 0;
        }

        Map<Object, Object> identity = new IdentityHashMap<>();
        Deque<Object> deque = new LinkedList<>();
        long size = 0;

        deque.push(o);
        identity.put(o, o);

        while (!deque.isEmpty()) {
            Object current = deque.pop();
            collect(current, deque, identity);
            size += count(current);
        }

        return size;
    }

    private static void collect(Object o, Deque<Object> deque, Map<Object, Object> visited) {
        Class<?> c = o.getClass();
        if (c.isArray() && !c.getComponentType().isPrimitive()) {
            IntStream.range(0, Array.getLength(o)).forEach(i -> {
                Object iV = Array.get(o, i);
                if (iV != null && visited.put(iV, iV) == null) {
                    deque.push(iV);
                }
            });
        } else {
            while (c != null) {
                Arrays.stream(c.getDeclaredFields())
                        .filter(f -> (f.getModifiers() & Modifier.STATIC) == 0 && !f.getType().isPrimitive())
                        .forEach(f -> {
                            f.setAccessible(true);
                            try {
                                Object fV = f.get(o);
                                if (fV != null && visited.put(fV, fV) == null) {
                                    deque.add(fV);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                c = c.getSuperclass();
            }
        }
    }

    private static long count(Object o) {
        Class<?> c = o.getClass();
        if (c.isArray()) {
            return pad(UNSAFE.arrayBaseOffset(c) + UNSAFE.arrayIndexScale(c) * Array.getLength(o));
        } else {
            AtomicLong size = new AtomicLong(MIN_SIZE);
            do {
                Arrays.stream(c.getDeclaredFields())
                        .filter(f -> (f.getModifiers() & Modifier.STATIC) == 0)
                        .forEach(f -> {
                            long offset = UNSAFE.objectFieldOffset(f);
                            size.set(offset >= size.get() ? offset + 1 : size.get());
                        });
                c = c.getSuperclass();
            } while (c != null);
            return pad(size.get());
        }
    }

    private static long pad(long size) {
        return (size + (ALIGNMENT - 1)) & ~(ALIGNMENT - 1);
    }

    private MemoryUtil() {
    }
}