package agh.ics.oop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionsParserTest {
    private final String errorMessage = "Test failed!";

    @Test
    public void parseTest(){
        String [] data = {"f", "forward", "b", "backward", "left", "rright",
                "Left", "FORWARD", "right", "rr" ,"r", "l", "fsaf","", " ", "0", "3123"};
        MoveDirection [] expected_data = {MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.BACKWARD,
                MoveDirection.BACKWARD, MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.RIGHT, MoveDirection.LEFT};

        MoveDirection [] parsed_data = OptionsParser.parse(data);
        Assertions.assertEquals(parsed_data.length, expected_data.length, errorMessage);
        for (int i = 0; i < parsed_data.length; i++) {
            Assertions.assertEquals(parsed_data[i], expected_data[i], errorMessage);
        }

    }
}
