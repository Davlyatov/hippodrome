import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HippodromeTest {

    @Test
    public void nullHorsesException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
        assertEquals("Horses cannot be null.", e.getMessage());
    }

    @Test
    public void emptyHorsesException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<>()));
        assertEquals("Horses cannot be empty.", e.getMessage());
    }

    @Test
    public void getHorses() throws NoSuchFieldException, IllegalAccessException {
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            horses.add(new Horse("Horse" + i, i, i));
        }

        Hippodrome hippodrome = new Hippodrome(horses);

        Field field = Hippodrome.class.getDeclaredField("horses");
        field.setAccessible(true);
        List<Horse> horses1 = (List<Horse>) field.get(hippodrome);
        assertEquals(horses, horses1);
    }

    @Test
    public void move() {
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            horses.add(mock(Horse.class));
        }

        new Hippodrome(horses).move();

        for (Horse horse : horses) {
            verify(horse).move();
        }
    }

    @Test
    public void getWinner() {
        Horse horse1 = new Horse("Horse1", 1, 1);
        Horse horse2 = new Horse("Horse2", 1, 2);
        Horse horse3 = new Horse("Horse3", 1, 3);
        Horse horse4 = new Horse("Horse4", 1, 4);
        Hippodrome hippodrome = new Hippodrome(List.of(horse1, horse2, horse3, horse4));

        assertSame(horse4,hippodrome.getWinner());
    }
}
