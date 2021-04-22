package ocp.chapter.twelve;

public enum Season {
    WINTER("Low") {
        public void printExpectedVisitors() { System.out.println("CLOSED"); } // Will be used when called by WINTER enum value
    },
    SPRING("Medium"),
    SUMMER("High"),
    FALL("Medium");
    
    private final String expectedVisitors;

    private Season(String expectedVisitors) {
        this.expectedVisitors = expectedVisitors;
    }

    public void printExpectedVisitors() {  // Will be used when called by SPRING, SUMMER or FALL enum values
        System.out.println(expectedVisitors);
    }
}