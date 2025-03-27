package us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// Unit test generated with ChatGPT - just tests getters and setters for line coverage

/**
 * Unit tests for the PersonalAssistive class.
 */
public class PersonalAssistiveTest {

  /**
   * Test that the default state of each flag is false.
   */
  @Test
  public void testDefaultState() {
    PersonalAssistive assistive = new PersonalAssistive();
    assertFalse(assistive.isUnavailable(), "Default unavailable should be false.");
    assertFalse(assistive.isOtherType(), "Default otherType should be false.");
    assertFalse(assistive.isVision(), "Default vision should be false.");
    assertFalse(assistive.isHearing(), "Default hearing should be false.");
    assertFalse(assistive.isMovement(), "Default movement should be false.");
    assertFalse(assistive.isCognition(), "Default cognition should be false.");
  }

  /**
   * Test the getter and setter for the unavailable flag.
   */
  @Test
  public void testSetUnavailable() {
    PersonalAssistive assistive = new PersonalAssistive();
    assistive.setUnavailable(true);
    assertTrue(assistive.isUnavailable(), "Unavailable should be true after setting to true.");
    assistive.setUnavailable(false);
    assertFalse(assistive.isUnavailable(), "Unavailable should be false after setting to false.");
  }

  /**
   * Test the getter and setter for the otherType flag.
   */
  @Test
  public void testSetOtherType() {
    PersonalAssistive assistive = new PersonalAssistive();
    assistive.setOtherType(true);
    assertTrue(assistive.isOtherType(), "OtherType should be true after setting to true.");
    assistive.setOtherType(false);
    assertFalse(assistive.isOtherType(), "OtherType should be false after setting to false.");
  }

  /**
   * Test the getter and setter for the vision flag.
   */
  @Test
  public void testSetVision() {
    PersonalAssistive assistive = new PersonalAssistive();
    assistive.setVision(true);
    assertTrue(assistive.isVision(), "Vision should be true after setting to true.");
    assistive.setVision(false);
    assertFalse(assistive.isVision(), "Vision should be false after setting to false.");
  }

  /**
   * Test the getter and setter for the hearing flag.
   */
  @Test
  public void testSetHearing() {
    PersonalAssistive assistive = new PersonalAssistive();
    assistive.setHearing(true);
    assertTrue(assistive.isHearing(), "Hearing should be true after setting to true.");
    assistive.setHearing(false);
    assertFalse(assistive.isHearing(), "Hearing should be false after setting to false.");
  }

  /**
   * Test the getter and setter for the movement flag.
   */
  @Test
  public void testSetMovement() {
    PersonalAssistive assistive = new PersonalAssistive();
    assistive.setMovement(true);
    assertTrue(assistive.isMovement(), "Movement should be true after setting to true.");
    assistive.setMovement(false);
    assertFalse(assistive.isMovement(), "Movement should be false after setting to false.");
  }

  /**
   * Test the getter and setter for the cognition flag.
   */
  @Test
  public void testSetCognition() {
    PersonalAssistive assistive = new PersonalAssistive();
    assistive.setCognition(true);
    assertTrue(assistive.isCognition(), "Cognition should be true after setting to true.");
    assistive.setCognition(false);
    assertFalse(assistive.isCognition(), "Cognition should be false after setting to false.");
  }

  /**
   * Test setting multiple flags simultaneously and verifying that each one retains its value.
   */
  @Test
  public void testMultipleFlags() {
    PersonalAssistive assistive = new PersonalAssistive();
    assistive.setUnavailable(true);
    assistive.setOtherType(false);
    assistive.setVision(true);
    assistive.setHearing(true);
    assistive.setMovement(false);
    assistive.setCognition(true);

    assertTrue(assistive.isUnavailable(), "Unavailable should be true.");
    assertFalse(assistive.isOtherType(), "OtherType should be false.");
    assertTrue(assistive.isVision(), "Vision should be true.");
    assertTrue(assistive.isHearing(), "Hearing should be true.");
    assertFalse(assistive.isMovement(), "Movement should be false.");
    assertTrue(assistive.isCognition(), "Cognition should be true.");
  }
}
