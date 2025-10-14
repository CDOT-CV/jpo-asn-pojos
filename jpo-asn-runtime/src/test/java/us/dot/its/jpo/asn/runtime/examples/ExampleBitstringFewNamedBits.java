package us.dot.its.jpo.asn.runtime.examples;

import us.dot.its.jpo.asn.runtime.types.Asn1Bitstring;

/**
 * Example of a BITSTRING where the number of named bits is smaller
 * than the size
 */
public class ExampleBitstringFewNamedBits extends Asn1Bitstring {

  public boolean isSidewalk_RevocableLane() {
    return get(0);
  }

  public void setSidewalk_RevocableLane(boolean sidewalk_RevocableLane) {
    set(0, sidewalk_RevocableLane);
  }

  public boolean isBicyleUseAllowed() {
    return get(1);
  }

  public void setBicyleUseAllowed(boolean bicyleUseAllowed) {
    set(1, bicyleUseAllowed);
  }

  public boolean isIsSidewalkFlyOverLane() {
    return get(2);
  }

  public void setIsSidewalkFlyOverLane(boolean isSidewalkFlyOverLane) {
    set(2, isSidewalkFlyOverLane);
  }

  public boolean isWalkBikes() {
    return get(3);
  }

  public void setWalkBikes(boolean walkBikes) {
    set(3, walkBikes);
  }

  public ExampleBitstringFewNamedBits() {
    super(
        16,
        false,
        new String[] {
            "sidewalk-RevocableLane", "bicyleUseAllowed", "isSidewalkFlyOverLane", "walkBikes"
        });
  }

}
