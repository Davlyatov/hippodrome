import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class HorseTest {

    @Test
    public void nullNameException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse(null, 1, 1));
        assertEquals("Name cannot be null.", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    public void blankNameException(String string) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse(string, 1, 1));
        assertEquals("Name cannot be blank.", e.getMessage());
    }

    @Test
    public void negativeSpeedException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse("Horse", -1, 1));
        assertEquals("Speed cannot be negative.", e.getMessage());
    }

    @Test
    public void negativeDistanceException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse("Horse", 1, -1));
        assertEquals("Distance cannot be negative.", e.getMessage());
    }

    @Test
    public void getName() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Horse", 1, 1);
        Field field = Horse.class.getDeclaredField("name");
        field.setAccessible(true);
        String nameValue = (String) field.get(horse);
        assertEquals("Horse", nameValue);
    }

    @Test
    public void getSpeed() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Horse", 1, 1);
        Field field = Horse.class.getDeclaredField("speed");
        field.setAccessible(true);
        double speedValue = (double) field.get(horse);
        assertEquals(1, speedValue);
    }

    @Test
    public void getDistance() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Horse", 1, 1);
        Field field = Horse.class.getDeclaredField("distance");
        field.setAccessible(true);
        double distanceValue = (double) field.get(horse);
        assertEquals(1, distanceValue);
    }

    @Test
    public void defaultDistance() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Horse", 1);
        Field field = Horse.class.getDeclaredField("distance");
        field.setAccessible(true);
        double distanceValue = (double) field.get(horse);
        assertEquals(0, distanceValue);
    }

    @Test
    public void moveUsesRandom() {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            new Horse("Horse", 1, 1).move();

            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.2, 0.5, 0.9, 1.0, 999.999, 0.0})
    public void move(double random) throws NoSuchFieldException, IllegalAccessException {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            Horse horse = new Horse("Horse", 1, 1);
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(random);

            horse.move();

            Field field = Horse.class.getDeclaredField("distance");
            field.setAccessible(true);
            double distanceValue = (double) field.get(horse);
            assertEquals(1 + 1 * random, distanceValue);
        }
    }
}
