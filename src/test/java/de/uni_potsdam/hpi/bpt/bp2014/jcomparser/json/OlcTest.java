package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class OlcTest {
    @Test
    public void TestOlcCreation() {
        File file = new File("src/test/resources/json/olc-example.json");
        try {
            String json = FileUtils.readFileToString(file);
            Olc olc = new Olc(json);
            List<String> expected = Arrays.asList("state2", "state3");
            assertEquals("The OLC couldn't parse outgoing behaviors correctly", expected, olc.allowedStateTransitions.get("state1"));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}