package ocp.chapter.twelve;

public enum Season {
    WINTER("Low") {
        public void printExpectedVisitors() { System.out.println("CLOSED"); }
    },
    SPRING("Medium"),
    SUMMER("High"),
    FALL("Medium");
    
    private final String expectedVisitors;

    private Season(String expectedVisitors) {
        this.expectedVisitors = expectedVisitors;
    }

    public void printExpectedVisitors() {
        System.out.println(expectedVisitors);
    }
}